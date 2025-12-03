package edu.csc324.pdg;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

/* This class will be the driver, and so it will be launched on start and kept updated */
public class Driver {
  private static final double R = 1.0; // Reward for cooperation
  private static final double T = 1.05; // Temptation to defect (b = 1.05)
  private static final double S = 0.0; // Sucker's payoff
  private static final double P = 0.0; // Punishment for mutual defection
  private static final Random random = new Random();

  public static void main(String[] args) {
    try {
      Driver driver = new Driver();
      Graph graph = new Graph();
      Visualizer viz = new Visualizer();
      Scanner scanner = new Scanner(System.in);
      
      // Get user input for grid size
      System.out.print("Enter grid size (e.g., 5 for 5x5): ");
      int gridSize = scanner.nextInt();
      if (gridSize < 2) gridSize = 2;
      
      int totalAgents = gridSize * gridSize;
      
      // Create grid network with alpha=0.4 threshold
      graph.generate2DToroidalGrid(gridSize, 0.4, 10.0);
      
      // Get user input for number of defectors
      System.out.print("Enter number of defectors (0-" + totalAgents + "): ");
      int numDefectors = scanner.nextInt();
      if (numDefectors < 0) numDefectors = 0;
      if (numDefectors > totalAgents) numDefectors = totalAgents;
      
      // Get user input for number of rounds
      System.out.print("Enter number of rounds: ");
      int numRounds = scanner.nextInt();
      if (numRounds < 1) numRounds = 1;
      
      // Randomly assign defectors
      driver.assignDefectors(graph, numDefectors);
      
      int initialCooperators = totalAgents - numDefectors;
      
      System.out.println("Starting simulation with " + totalAgents + " agents (" + gridSize + "x" + gridSize + " grid)");
      System.out.println("Initial: " + initialCooperators + " cooperators, " + numDefectors + " defectors");
      System.out.println("Rounds: " + numRounds);
      System.out.println("Blue = Cooperator, Red = Defector, Grey = Dead");
      
      // Show initial state
      viz.updateGraph(graph);
      viz.display();
      viz.sleep(2000);
      
      // Run simulation with visualization
      for (int round = 0; round < numRounds; round++) {
        System.out.println("\nRound " + (round + 1));
        driver.calculatePayoffs(graph);
        
        int beforeRemoval = graph.getActiveAgents().size();
        driver.removeAgents(graph);
        int afterRemoval = graph.getActiveAgents().size();
        
        System.out.println("Agents removed: " + (beforeRemoval - afterRemoval));
        System.out.println("Survivors: " + afterRemoval);
        
        if (afterRemoval == 0) {
          System.out.println("All agents eliminated!");
          break;
        }
        
        driver.updateStrategies(graph);
        
        // Update visualization
        viz.updateGraph(graph);
        viz.sleep(1500);
      }
      
      // Print final results
      ArrayList<Agent> survivors = graph.getActiveAgents();
      int cooperators = 0;
      for (Agent agent : survivors) {
        if (agent.getStrategy()) cooperators++;
      }
      
      System.out.println("\nFinal Results:");
      System.out.println("Survivors: " + survivors.size() + "/" + totalAgents);
      if (survivors.size() > 0) {
        System.out.println("Cooperators: " + cooperators + "/" + survivors.size());
      }
      
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /*
  * This function runs the game logic iteratively
  * As mentioned in the paper part II, the game logic includes:
  * 1) Game Playing and Payoffs --> calculatePayoffs(graph)
  * 2) Failure and Agent Removal --> removeAgents(graph)
  * 3) Strategy updating --> updateStrategies(graph)
  */
  public void runPDGRounds(Graph graph, int numberOfRounds) throws Exception {
    for (int round = 0; round < numberOfRounds; round++) {
      calculatePayoffs(graph);
      removeAgents(graph);
      updateStrategies(graph);
    }
  }

  /*
  * Iterates through graph and uses a predefined formula (see paper) to calculate payoff for all agents
  * 1st: Will call each pair of agent to take an action
  * 2nd: Based on the action, will generate a payoff to the Agent
  * Visually: Simulation calls each pair --> Agents will chooseAction() --> Simulation will update the payoff field of agents.
  */
  public void calculatePayoffs(Graph graph) throws Exception {
    ArrayList<Agent> agents = graph.getAllAgents();
    
    // Reset payoffs
    for (Agent agent : agents) {
      agent.setPayoff(0.0);
    }
    
    // Calculate payoffs for each agent pair
    for (Agent agent : agents) {
      if (!agent.isActive()) continue;
      
      ArrayList<Agent> neighbors = graph.getNeighbors(agent);
      for (Agent neighbor : neighbors) {
        if (!neighbor.isActive()) continue;
        
        boolean agentAction = agent.chooseAction();
        boolean neighborAction = neighbor.chooseAction();
        
        double payoff = getPayoff(agentAction, neighborAction);
        agent.addPayoff(payoff);
      }
    }
  }

  /*
  * Iterates through graph and remove agents whose payoff < tolerance fields
  */
  public void removeAgents(Graph graph) throws Exception {
    ArrayList<Agent> agents = new ArrayList<>(graph.getAllAgents());
    
    for (Agent agent : agents) {
      if (agent.isActive() && agent.getPayoff() < agent.getThreshold()) {
        agent.fail(); // Just mark as failed, don't remove from graph
      }
    }
  }

  /*
  * Iterates through graph to set strategies for each survivor agent
  * Each agent will randomly choose a survived neighbor and imitate its strategy
  * There is a probability (see paper) that tells how the agent will imitate that neighbor*
  */
  public void updateStrategies(Graph graph) throws Exception {
    ArrayList<Agent> agents = graph.getAllAgents();
    
    for (Agent agent : agents) {
      if (!agent.isActive()) continue;
      
      ArrayList<Agent> neighbors = graph.getNeighbors(agent);
      ArrayList<Agent> activeNeighbors = new ArrayList<>();
      
      for (Agent neighbor : neighbors) {
        if (neighbor.isActive()) {
          activeNeighbors.add(neighbor);
        }
      }
      
      if (!activeNeighbors.isEmpty()) {
        Agent randomNeighbor = activeNeighbors.get(random.nextInt(activeNeighbors.size()));
        if (randomNeighbor.getPayoff() > agent.getPayoff()) {
          agent.setStrategy(randomNeighbor.getStrategy());
        }
      }
    }
  }

  /* As stated in the paper in II. 1) Game playing and payoffs, we will set
  * R (reward for cooperation) = 1;* T (reward for defect) = b (where b>1);
  * S (punishment for not defecting) = P (punishment for mutual defection) = 0.
  *
  * int id: Agent id, unique for all agents
  * double threshold: Minimum load required for agent to survive* double capacity: Maximum load capacity (beyond this agent wins nothing)
  * NOTE: current default load is 0.0
  */
  private Agent createAgent(int id, double threshold, double capacity){
    return new Agent(id, threshold, capacity);
  }
  
  private double getPayoff(boolean agentCooperates, boolean neighborCooperates) {
    if (agentCooperates && neighborCooperates) {
      return R; // Both cooperate
    } else if (!agentCooperates && neighborCooperates) {
      return T; // Agent defects, neighbor cooperates
    } else if (agentCooperates && !neighborCooperates) {
      return S; // Agent cooperates, neighbor defects
    } else {
      return P; // Both defect
    }
  }
  
  private void assignDefectors(Graph graph, int numDefectors) {
    ArrayList<Agent> agents = new ArrayList<>(graph.getAllAgents());
    Collections.shuffle(agents);
    
    for (int i = 0; i < numDefectors && i < agents.size(); i++) {
      agents.get(i).setStrategy(false); // Set to defector
    }
  }
}