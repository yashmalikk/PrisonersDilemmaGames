package edu.csc324.pdg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {
  private Graph graph;
  private Agent agent1, agent2, agent3;

  @BeforeEach
  void setUp() {
    graph = new Graph();
    agent1 = new Agent(1, 0.8, 1.0);
    agent2 = new Agent(2, 0.7, 1.0);
    agent3 = new Agent(3, 0.9, 1.0);
  }

  @Test
  void testAddNodeBasic() {
    ArrayList<Agent> connections = new ArrayList<>();
    connections.add(agent2);

    graph.addNode(agent1, connections);

    try {
      assertEquals(1, graph.getNeighbors(agent1).size());
      assertTrue(graph.getNeighbors(agent2).contains(agent1));
    } catch (Exception e) {
      fail("Should not throw exception");
    }
  }

  @Test
  void testAddNodeSymmetry() throws Exception {
    graph.addNode(agent1, new ArrayList<>());
    graph.addNode(agent2, new ArrayList<>());

    ArrayList<Agent> neighbors = new ArrayList<>();
    neighbors.add(agent1);
    graph.addNode(agent2, neighbors);

    assertTrue(graph.getNeighbors(agent1).contains(agent2));
    assertTrue(graph.getNeighbors(agent2).contains(agent1));
  }

  @Test
  void testAddNodeMultipleNeighbors() throws Exception {
    graph.addNode(agent1, new ArrayList<>());
    graph.addNode(agent2, new ArrayList<>());
    graph.addNode(agent3, new ArrayList<>());

    ArrayList<Agent> neighbors = new ArrayList<>();
    neighbors.add(agent2);
    neighbors.add(agent3);

    graph.addNode(agent1, neighbors);

    assertEquals(2, graph.getNeighbors(agent1).size());
    assertTrue(graph.getNeighbors(agent1).contains(agent2));
    assertTrue(graph.getNeighbors(agent1).contains(agent3));

    assertEquals(1, graph.getNeighbors(agent2).size());
    assertEquals(1, graph.getNeighbors(agent3).size());
  }

  @Test
  void testRemoveNode() throws Exception {
    ArrayList<Agent> connections = new ArrayList<>();
    connections.add(agent2);
    graph.addNode(agent1, connections);

    graph.removeNode(agent1);

    assertThrows(Exception.class, () -> graph.getNeighbors(agent1));
  }

  @Test
  void testRemoveNodeUpdatesNeighbors() throws Exception {
    graph.addNode(agent1, new ArrayList<>());
    graph.addNode(agent2, new ArrayList<>());

    ArrayList<Agent> neighbors = new ArrayList<>();
    neighbors.add(agent2);
    graph.addNode(agent1, neighbors);

    assertEquals(1, graph.getNeighbors(agent1).size());
    assertEquals(1, graph.getNeighbors(agent2).size());

    graph.removeNode(agent1);

    assertTrue(graph.getNeighbors(agent2).isEmpty());
  }

  @Test
  void testRemoveNonExistentNode() {
    assertThrows(Exception.class, () -> graph.removeNode(agent1));
  }

  @Test
  void testGetNeighborsNonExistentNode() {
    assertThrows(Exception.class, () -> graph.getNeighbors(agent1));
  }
}
