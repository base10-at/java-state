package at.base10.state;

/**
 * A factory interface for creating state instances within a state machine.
 *
 * @param <S> the type representing the states in the state machine
 */
public interface StateFactory<S> {
    /**
     * Builds a new state instance associated with the given state machine.
     *
     * @param stateMachine the state machine instance
     * @return a new state instance
     */
    S build(StateMachine<S> stateMachine);
}
