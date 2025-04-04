package at.base10.state;

import at.base10.state.observer.StateChangeEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObservableEventTest {

    static class Some {
    }

    private final Some a = new Some();
    private final Some b = new Some();
    private final Some c = new Some();

    @Test
    void previous() {
        assertEquals(a, new StateChangeEvent<>(a, b).previous());
    }

    @Test
    void current() {
        assertEquals(b, new StateChangeEvent<>(a, b).current());
    }

    @Test
    void didChange() {
        assertTrue(new StateChangeEvent<>(a, b).didChange());
    }

    @Test
    void didNotChange() {
        assertFalse(new StateChangeEvent<>(a, a).didChange());
    }

    @Test
    void equals() {
        assertEquals(new StateChangeEvent<>(a, b), new StateChangeEvent<>(a, b));
    }

    @Test
    void notEquals() {
        assertNotEquals(new StateChangeEvent<>(a, b), new StateChangeEvent<>(b, a));
        assertNotEquals(new StateChangeEvent<>(a, b), new StateChangeEvent<>(b, c));
    }
}