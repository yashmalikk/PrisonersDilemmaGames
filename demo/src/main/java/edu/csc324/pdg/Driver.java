package edu.csc324.pdg;

import java.util.ArrayList;
import java.util.Random;

/* This class will be the driver, and so it will be launched on start and kept updated */
public class Driver {
  private static final double R = 1.0; // Reward for cooperation
  private static final double T = 1.5; // Temptation to defect (b > 1)
  private static final double S = 0.0; // Sucker's payoff
  private static final double P = 0.0; // Punishment for mutual defection
  private static final Random random = new Random();

  public static void main(String[] args) {
    System.out.println("Hello world to me!");
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
        agent.fail();
        graph.removeNode(agent);
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
}