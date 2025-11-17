package edu.csc324.pdg;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

  @Test
  void testAddNodeAndNeighbors() throws Exception {
    Graph graph = new Graph();

    Agent a = new Agent(1, 10, 20);
    Agent b = new Agent(2, 10, 20);
    Agent c = new Agent(3, 10, 20);

    graph.addNode(a, new ArrayList<>());
    graph.addNode(b, new ArrayList<>());
    graph.addNode(c, new ArrayList<>());

    ArrayList<Agent> neighbors = new ArrayList<>();
    neighbors.add(b);
    neighbors.add(c);

    graph.addNode(a, neighbors);

    assertEquals(2, graph.getNeighbors(a).size());
    assertTrue(graph.getNeighbors(a).contains(b));
    assertTrue(graph.getNeighbors(a).contains(c));

    assertEquals(1, graph.getNeighbors(b).size());
    assertTrue(graph.getNeighbors(b).contains(a));

    assertEquals(1, graph.getNeighbors(c).size());
    assertTrue(graph.getNeighbors(c).contains(a));
  }

  @Test
  void testRemoveNode() throws Exception {
    Graph graph = new Graph();

    Agent a = new Agent(1, 10, 20);
    Agent b = new Agent(2, 10, 20);

    graph.addNode(a, new ArrayList<>());
    graph.addNode(b, new ArrayList<>());

    ArrayList<Agent> neighborsA = new ArrayList<>();
    neighborsA.add(b);
    graph.addNode(a, neighborsA);

    assertEquals(1, graph.getNeighbors(a).size());
    assertEquals(1, graph.getNeighbors(b).size());

    graph.removeNode(a);

    assertTrue(graph.getNeighbors(b).isEmpty());

    Exception ex = assertThrows(Exception.class, () -> graph.getNeighbors(a));
    assertTrue(ex.getMessage().contains("Node not found"));
  }

  @Test
  void testGetNeighborsThrowsForMissingNode() {
    Graph graph = new Graph();
    Agent a = new Agent(1, 10, 20);

    Exception ex = assertThrows(Exception.class, () -> graph.getNeighbors(a));
    assertTrue(ex.getMessage().contains("Node not found"));
  }

  @Test
  void testRemoveMissingNodeThrows() {
    Graph graph = new Graph();
    Agent a = new Agent(1, 10, 20);

    Exception ex = assertThrows(Exception.class, () -> graph.removeNode(a));
    assertTrue(ex.getMessage().contains("Node not found"));
  }

  @Test
  void testAddNodeSymmetry() throws Exception {
    Graph graph = new Graph();

    Agent a = new Agent(1, 10, 20);
    Agent b = new Agent(2, 10, 20);

    graph.addNode(a, new ArrayList<>());
    graph.addNode(b, new ArrayList<>());

    ArrayList<Agent> neighborsB = new ArrayList<>();
    neighborsB.add(a);
    graph.addNode(b, neighborsB);

    assertTrue(graph.getNeighbors(a).contains(b));
    assertTrue(graph.getNeighbors(b).contains(a));
  }
}