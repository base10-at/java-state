package at.base10.state;

/**
 * An abstract class that provides a base implementation for states
 * that support transitions within a state machine.
 *
 * @param <S> the type representing the states in the state machine
 */
public abstract class Transitional<S>{

    /**
     * The state machine associated with this state.
     */
    protected final StateMachine<S> stateMachine;

    /**
     * Constructs a new Transitional state with the given state machine.
     *
     * @param stateMachine the state machine instance
     */
    public Transitional(StateMachine<S> stateMachine) {
        this.stateMachine = stateMachine;
    }

    /**
     * Transitions to a new state within the state machine.
     *
     * @param state the class of the new state
     * @return the updated state machine instance
     */
    @SuppressWarnings("SameParameterValue")
    protected StateMachine<S> transitionToState(Class<? extends S> state) {
        return stateMachine.transitionToState(state);
    }
}