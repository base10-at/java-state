package at.base10.state;

/**
 * Represents a state within a state machine.
 *
 * @param <S> the type representing the states in the state machine
 */
public interface State<S> {

    /**
     * Transitions to a new state within the state machine.
     *
     * @param state the class of the new state
     * @return the updated state machine instance
     */
    StateMachine<S> transition(Class<? extends S> state);
}