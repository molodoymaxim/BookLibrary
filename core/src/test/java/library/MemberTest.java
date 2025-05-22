package library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemberTest {

    @Test
    void testConstructorAndGetters() {
        Member m = new Member("M01", "Name");
        assertEquals("M01", m.getMemberId());
        assertEquals("Name", m.getName());
    }

    @Test
    void testToString() {
        Member m = new Member("M02", "Имя");
        String s = m.toString();
        assertTrue(s.contains("M02"));
        assertTrue(s.contains("Имя"));
    }
}
