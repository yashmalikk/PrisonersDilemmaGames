package edu.csc324.pdg;

/**
 * Represents an agent in the network with load capacity and failure threshold
 */
public class Agent {
  private int id; // Unique identifier
  private boolean isActive; // Whether agent is operational
  private double threshold; // Load threshold before failure
  private double load; // Current load on agent
  private double capacity; // Maximum load capacity

  public Agent(int id, double threshold, double capacity) {
    this.id = id;
    this.threshold = threshold;
    this.capacity = capacity;
    this.isActive = true;
    this.load = 0.0;
  }

  public int getId() {
    return id;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    this.isActive = active;
  }

  public double getThreshold() {
    return threshold;
  }

  public double getLoad() {
    return load;
  }

  public void setLoad(double load) {
    this.load = load;
  }

  public double getCapacity() {
    return capacity;
  }

  // Checks if agent should fail based on load exceeding threshold
  public boolean shouldFail() {
    return load > threshold;
  }

  // Marks agent as failed/inactive
  public void fail() {
    this.isActive = false;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    Agent agent = (Agent) obj;
    return id == agent.id;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(id);
  }

  @Override
  public String toString() {
    return "Agent{id=" + id + ", active=" + isActive + ", load=" + load + ", threshold=" + threshold + "}";
  }
}