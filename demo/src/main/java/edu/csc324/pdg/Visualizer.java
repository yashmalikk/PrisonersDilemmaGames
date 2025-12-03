package edu.csc324.pdg;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import java.util.ArrayList;

public class Visualizer {
    private Graph gsGraph;
    private Viewer viewer;
    
    public Visualizer() {
        System.setProperty("org.graphstream.ui", "swing");
        gsGraph = new SingleGraph("Prisoner's Dilemma");
        gsGraph.setAttribute("ui.stylesheet", 
            "node { " +
            "  size: 40px; " +
            "  text-alignment: center; " +
            "  text-size: 5000px; " +
            "  text-color: white; " +
            "  fill-color: red; " +
            "  stroke-mode: plain; " +
            "  stroke-color: black; " +
            "  stroke-width: 2px; " +
            "} " +
            "node.cooperator { fill-color: blue; } " +
            "node.dead { fill-color: gray; } " +
            "edge { fill-color: gray; size: 2px; }");
        gsGraph.setAttribute("ui.antialias");
        gsGraph.setAttribute("ui.quality");
    }
    
    public void display() {
        viewer = gsGraph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }
    
    public void updateGraph(edu.csc324.pdg.Graph pdgGraph) {
        gsGraph.clear();
        
        ArrayList<Agent> agents = pdgGraph.getAllAgents();
        if (agents.isEmpty()) return;
        
        // Calculate grid size from number of agents
        int totalAgents = agents.size();
        int gridSize = (int) Math.sqrt(totalAgents);
        
        // Add nodes with grid positions
        for (Agent agent : agents) {
            Node node = gsGraph.addNode(String.valueOf(agent.getId()));
            node.setAttribute("ui.label", String.valueOf(agent.getId()));
            
            // Calculate grid position
            int row = agent.getId() / gridSize;
            int col = agent.getId() % gridSize;
            node.setAttribute("xy", col * 2, -row * 2); // Spread out more
            
            // Set color based on agent status and strategy
            if (!agent.isActive()) {
                node.setAttribute("ui.class", "dead");
                node.setAttribute("ui.style", "fill-color: gray;");
            } else if (agent.getStrategy()) {
                node.setAttribute("ui.class", "cooperator");
                node.setAttribute("ui.style", "fill-color: blue;");
            } else {
                node.removeAttribute("ui.class");
                node.setAttribute("ui.style", "fill-color: red;");
            }
        }
        
        // Add edges
        for (Agent agent : agents) {
            try {
                ArrayList<Agent> neighbors = pdgGraph.getNeighbors(agent);
                for (Agent neighbor : neighbors) {
                    String edgeId = agent.getId() + "-" + neighbor.getId();
                    if (gsGraph.getEdge(edgeId) == null && gsGraph.getEdge(neighbor.getId() + "-" + agent.getId()) == null) {
                        gsGraph.addEdge(edgeId, String.valueOf(agent.getId()), String.valueOf(neighbor.getId()));
                    }
                }
            } catch (Exception e) {
                // Skip if agent not found
            }
        }
    }
    
    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}