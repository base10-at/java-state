package at.base10.state;

/**
 * {@inheritDoc}
 *
 * An abstract class that provides a base implementation for states
 * that support transitions within a state machine.
 *
 * @param <S> the type representing the states in the state machine
 */
public abstract class TransitionalState<S> extends ContextAwareState<S> {

    /**
     * Constructs a new Transitional state with the given state machine.
     *
     * @param stateMachine the state machine instance
     */
    public TransitionalState(StateMachine<S> stateMachine) {
        super(stateMachine);
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