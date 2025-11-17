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
}