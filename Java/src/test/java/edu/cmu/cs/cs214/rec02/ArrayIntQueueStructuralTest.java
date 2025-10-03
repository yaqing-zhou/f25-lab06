package edu.cmu.cs.cs214.rec02;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link ArrayIntQueue} class with structural testing method
 * - cover clear()
 * - cover ensureCapacity() when head == 0
 * - cover ensureCapacity() when head > 0 (wrapped around)
 * which are not covered in IntQueueTest.java
 * 
 * NOTE: These tests assume the initial capacity is 10.
*/
public class ArrayIntQueueStructuralTest {

    private ArrayIntQueue q;

    @Before
    public void setUp() {
        q = new ArrayIntQueue();
    }

    @Test
    public void clearResetsState() {
        // Enqueue a few values, then clear and verify empty state.
        for (int i = 0; i < 5; i++) q.enqueue(i);
        q.clear();
        assertTrue(q.isEmpty());
        assertEquals(0, q.size());
        assertNull(q.peek());
        assertNull(q.dequeue());

        // Ensure queue can be reused after clear.
        q.enqueue(99);
        assertEquals(Integer.valueOf(99), q.peek());
        assertEquals(Integer.valueOf(99), q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    public void ensureCapacity_headZero() {
        // Fill to initial capacity (10), then one more enqueue triggers growth with head == 0.
        for (int i = 0; i < 10; i++) q.enqueue(i);
        q.enqueue(10); // triggers ensureCapacity()

        // Verify FIFO order is preserved after growth.
        for (int i = 0; i <= 10; i++) {
            assertEquals(Integer.valueOf(i), q.dequeue());
        }
        assertTrue(q.isEmpty());
    }

    @Test
    public void ensureCapacity_wrapped() {
        // Make head > 0 by dequeuing some items (wrap-around scenario).
        for (int i = 0; i < 8; i++) q.enqueue(i);  // [0..7]
        for (int i = 0; i < 4; i++) assertEquals(Integer.valueOf(i), q.dequeue()); // remove 0..3; head advanced

        // Fill to capacity again: size becomes 10, but the buffer is wrapped.
        for (int i = 8; i < 14; i++) q.enqueue(i);  // queue now "full" with wrap-around

        // Next enqueue triggers growth while head > 0.
        q.enqueue(14);

        // Verify FIFO order after growth: remaining should be 4..14.
        for (int i = 4; i <= 14; i++) {
            assertEquals(Integer.valueOf(i), q.dequeue());
        }
        assertTrue(q.isEmpty());
    }
}