package edu.csc324.pdg;

/* This class will be the driver, and so it will be launched on start and kept updated */
public class Driver {
  public static void main(String[] args) {
    System.out.println("Hello world to me!");
  }

  /*
   * As stated in the paper in II. 1) Game playing and payoffs, we will set 
   * R (reward for cooperation) = 1;
   * T (reward for defect) = b (where b>1);
   * S (punishment for not defecting) = P (punishment for mutual defection) = 0.
   * 
   * int id: Agent id, unique for all agents
   * double threshold: Minimum load required for agent to survive
   * double capacity: Maximum load capacity (beyond this agent wins nothing)
   * NOTE: current default load is 0.0
   */
  private void createAgent(int id, double threshold, double capacity){
    // To-do: Implement
  }
}