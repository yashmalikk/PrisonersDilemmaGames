package edu.csc324.pdg;

/* This class will be the driver, and so it will be launched on start and kept updated */
public class Driver {
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
    // To-do: Implement
  }

  /*
  * Iterates through graph and uses a predefined formula (see paper) to calculate payoff for all agents
  * 1st: Will call each pair of agent to take an action
  * 2nd: Based on the action, will generate a payoff to the Agent
  * Visually: Simulation calls each pair --> Agents will chooseAction() --> Simulation will update the payoff field of agents.
  */
  public void calculatePayoffs(Graph graph) throws Exception {
    // To-do: Implement
  }

  /*
  * Iterates through graph and remove agents whose payoff < tolerance fields
  */
  public void removeAgents(Graph graph) throws Exception {
    // To-do: Implement
  }

  /*
  * Iterates through graph to set strategies for each survivor agent
  * Each agent will randomly choose a survived neighbor and imitate its strategy
  * There is a probability (see paper) that tells how the agent will imitate that neighbor*
  */
  public void updateStrategies(Graph graph) throws Exception {
    // To-do: Implement
  }

  /* As stated in the paper in II. 1) Game playing and payoffs, we will set
  * R (reward for cooperation) = 1;* T (reward for defect) = b (where b>1);
  * S (punishment for not defecting) = P (punishment for mutual defection) = 0.
  *
  * int id: Agent id, unique for all agents
  * double threshold: Minimum load required for agent to survive* double capacity: Maximum load capacity (beyond this agent wins nothing)
  * NOTE: current default load is 0.0
  */
  private void createAgent(int id, double threshold, double capacity){
    // To-do: Implement
  }
}