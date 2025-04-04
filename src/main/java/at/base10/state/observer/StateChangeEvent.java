package at.base10.state.observer;

/**
 * Represents an event of a state change, containing both the previous and current states.
 *
 * @param <S> the type representing the state
 * @param previous the previous state
 * @param current the current state
 */
public record StateChangeEvent<S>(S previous, S current) {

    /**
     * Indicates whether the state has actually changed.
     *
     * @return true if the previous and current states are different, false otherwise
     */
    public boolean didChange() {
        return previous != current;
    }
}