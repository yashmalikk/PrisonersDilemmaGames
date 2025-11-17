import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import edu.csc324.pdg.Graph;
import edu.csc324.pdg.Agent;

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
    void testAddNode() {
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
    void testRemoveNode() throws Exception {
        ArrayList<Agent> connections = new ArrayList<>();
        connections.add(agent2);
        graph.addNode(agent1, connections);
        
        graph.removeNode(agent1);
        
        assertThrows(Exception.class, () -> graph.getNeighbors(agent1));
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