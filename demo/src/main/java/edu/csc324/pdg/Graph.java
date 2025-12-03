package edu.csc324.pdg;

import java.util.ArrayList;
import java.util.HashMap;

/*
* Graph class implemented with hashmap
*/
public class Graph {
  private HashMap<Agent, ArrayList<Agent>> adjacency_list;

  public Graph() {
    this.adjacency_list = new HashMap<>();
  }

  public void addNode(Agent agent, ArrayList<Agent> connections) {
    this.adjacency_list.put(agent, new ArrayList<>(connections));

    for (Agent neighbor : connections) {
      this.adjacency_list.computeIfAbsent(neighbor, k -> new ArrayList<>()).add(agent);
    }
  }

  public void removeNode(Agent agent) throws Exception {
    if (!this.adjacency_list.containsKey(agent)) {
      throw new Exception("Can't remove node. Node not found in adjacency_list.");
    }

    for (Agent neighbor : getNeighbors(agent)) {
      this.adjacency_list.get(neighbor).remove(agent);
    }

    this.adjacency_list.remove(agent);
  }

  public ArrayList<Agent> getNeighbors(Agent agent) throws Exception {
    if (!this.adjacency_list.containsKey(agent)) {
      throw new Exception("Can't find node. Node not found in adjacency_list.");
    }

    return this.adjacency_list.get(agent);
  }

  public ArrayList<Agent> getAllAgents() {
    return new ArrayList<>(this.adjacency_list.keySet());
  }
  
  public ArrayList<Agent> getActiveAgents() {
    ArrayList<Agent> activeAgents = new ArrayList<>();
    for (Agent agent : this.adjacency_list.keySet()) {
      if (agent.isActive()) {
        activeAgents.add(agent);
      }
    }
    return activeAgents;
  }

  public void generate2DToroidalGrid(int size, double threshold, double capacity) {
    Agent[][] grid = new Agent[size][size];

    // Create agents
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        grid[i][j] = new Agent(i * size + j, threshold, capacity);
      }
    }

    // Connect with wrap-around neighbors
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {

        int up = (i - 1 + size) % size;
        int down = (i + 1) % size;
        int left = (j - 1 + size) % size;
        int right = (j + 1) % size;

        ArrayList<Agent> neighbors = new ArrayList<>();
        neighbors.add(grid[up][j]);
        neighbors.add(grid[down][j]);
        neighbors.add(grid[i][left]);
        neighbors.add(grid[i][right]);

        adjacency_list.put(grid[i][j], neighbors);
      }
    }
  }
}