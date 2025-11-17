package edu.csc324.pdg;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class AgentTest {
  @Test
  void testConstructorAndGetters() {
    Agent a = new Agent(1, 10.0, 20.0);

    assertEquals(1, a.getId());
    assertTrue(a.isActive());
    assertEquals(10.0, a.getThreshold());
    assertEquals(20.0, a.getCapacity());
    assertEquals(0.0, a.getLoad());
  }

  @Test
  void testSetLoad() {
    Agent a = new Agent(2, 10.0, 20.0);

    a.setLoad(5.5);
    assertEquals(5.5, a.getLoad());

    a.setLoad(15.0);
    assertEquals(15.0, a.getLoad());
  }

  @Test
  void testActiveState() {
    Agent a = new Agent(3, 10.0, 20.0);

    assertTrue(a.isActive());

    a.setActive(false);
    assertFalse(a.isActive());

    a.setActive(true);
    assertTrue(a.isActive());
  }

  @Test
  void testShouldFail() {
    Agent a = new Agent(4, 10.0, 20.0);

    a.setLoad(5.0);
    assertFalse(a.shouldFail());

    a.setLoad(10.0);
    assertFalse(a.shouldFail());

    a.setLoad(11.0);
    assertTrue(a.shouldFail());
  }

  @Test
  void testFailMethod() {
    Agent a = new Agent(5, 10.0, 20.0);

    assertTrue(a.isActive());
    a.fail();
    assertFalse(a.isActive());
  }

  @Test
  void testEqualsAndHashCode() {
    Agent a1 = new Agent(1, 10.0, 20.0);
    Agent a2 = new Agent(1, 5.0, 30.0);
    Agent a3 = new Agent(2, 10.0, 20.0);

    assertEquals(a1, a2);
    assertEquals(a1.hashCode(), a2.hashCode());

    assertNotEquals(a1, a3);
    assertNotEquals(a2, a3);
  }

  @Test
  void testEqualsWithDifferentObject() {
    Agent a = new Agent(1, 10.0, 20.0);

    assertNotEquals(a, null);
    assertNotEquals(a, "string");
  }

  @Test
  void testToStringFormat() {
    Agent a = new Agent(7, 10.0, 20.0);
    a.setLoad(5.0);

    String s = a.toString();

    assertTrue(s.contains("id=7"));
    assertTrue(s.contains("active=true"));
    assertTrue(s.contains("load=5.0"));
    assertTrue(s.contains("threshold=10.0"));
  }
}
