package at.base10.state;

import java.util.Collection;

/**
 * Represents a generic state machine that manages states of type {@code S}.
 *
 * @param <S> the type representing the states in the state machine
 */
public interface StateMachine<S> {

    /**
     * Creates a new {@link StateMachineBuilder} for building a state machine.
     *
     * @param <S> the type of the state
     * @param stateClass the class type of the state
     * @return a new {@link StateMachineBuilder} instance
     */
    static <S> StateMachineBuilder<S> builder(Class<S> stateClass) {
        return new StateMachineBuilder<>(stateClass);
    }

    /**
     * Transitions the state machine to a new state.
     *
     * @param state the class of the new state
     * @return the updated state machine
     */
    StateMachine<S> transition(Class<? extends S> state);

    /**
     * Retrieves the current state of the state machine.
     *
     * @return the current state
     */
    S state();

    /**
     * Retrieves all possible states within the state machine.
     *
     * @return a collection of all possible states
     */
    Collection<S> states();

    /**
     * Returns the current state as a proxy instance.
     *
     * @return the proxy representation of the current state
     */
    S asProxy();

    /**
     * Returns the current state as a proxy instance of the specified type.
     *
     * @param <E> the type of the proxy interface
     * @param state the class of the proxy interface
     * @return the proxy representation of the state as the specified type
     */
    <E> E asProxy(Class<E> state);
}
