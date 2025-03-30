package at.base10.state;

/**
 * An abstract base class for states that are aware of their associated state machine.
 * <p>
 * This class provides a reference to the {@link StateMachine} instance, allowing states
 * to interact with and transition within the state machine.
 * </p>
 *
 * @param <S> the type representing the states in the state machine
 */
public abstract class ContextAwareState<S>{

    /**
     * The state machine associated with this state.
     */
    protected final StateMachine<S> stateMachine;

    /**
     * Constructs a new Transitional state with the given state machine.
     *
     * @param stateMachine the state machine instance
     */
    public ContextAwareState(StateMachine<S> stateMachine) {
        this.stateMachine = stateMachine;
    }

}