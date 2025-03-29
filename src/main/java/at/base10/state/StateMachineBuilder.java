package at.base10.state;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A builder class for constructing instances of {@link StateMachine}.
 *
 * @param <S> the type representing the states in the state machine
 */
@Log4j2
public class StateMachineBuilder<S> {
    final Collection<StateFactory<S>> statesBuilders = new ArrayList<>();
    final Class<S> stateClass;
    /**
     * Constructs a new StateMachineBuilder with the specified state class.
     *
     * @param stateClass the class type of the state
     */
    public StateMachineBuilder(@NonNull Class<S> stateClass) {
        this.stateClass = stateClass;
    }
    /**
     * Registers a new state factory in the state machine builder.
     *
     * @param stateBuilder the state factory to register
     * @return this builder instance
     * @throws NullPointerException if {@code stateBuilder} is null
     */
    public StateMachineBuilder<S> register(@NonNull StateFactory<S> stateBuilder) {
        statesBuilders.add(stateBuilder);
        return this;
    }

    /**
     * Registers multiple state factories in the state machine builder.
     *
     * @param stateBuilders the collection of state factories to register
     * @return this builder instance
     * @throws NullPointerException if an element is null
     * @throws NullPointerException if {@code stateBuilder} is null
     */
    public StateMachineBuilder<S> register(@NonNull Collection<StateFactory<S>> stateBuilders) {
        stateBuilders.forEach(this::register);
        return this;
    }

    /**
     * Builds a new state machine with the specified initial state.
     *
     * @param initialState the class of the initial state
     * @return the constructed {@link StateMachine} instance
     */
    public StateMachine<S> build(@NonNull Class<? extends S> initialState) {
        StateMachineImpl<S> stateMachine = new StateMachineImpl<>(stateClass);
        registerStates(stateMachine, buildStates(stateMachine));
        setInitialState(initialState, stateMachine);
        return stateMachine;
    }

    private static <S> void setInitialState(Class<? extends S> initialState,
                                            StateMachineImpl<S> stateMachine) {
        stateMachine.currentState = stateMachine.states.get(initialState);
        if (stateMachine.currentState == null) {
            throw new IllegalArgumentException("State " + initialState + " not found");
        }
    }


    private List<S> buildStates(StateMachineImpl<S> stateMachine) {
        return statesBuilders.stream()
                .map(e -> e.build(stateMachine))
                .toList();

    }

    private void registerStates(StateMachineImpl<S> stateMachine, List<S> states) {
        states.forEach(state -> registerState(stateMachine, state));
    }

    private void registerState(StateMachineImpl<S> stateMachine, S state) {
        validateClass(state);
        //noinspection unchecked
        Class<S> clazz = (Class<S>) state.getClass();
        stateMachine.states.put(clazz, state);
        log.debug("State<{}> registered at StateMachine<{}>", clazz::getSimpleName, stateClass::getSimpleName);
    }

    private void validateClass(S state) {
        var c = state.getClass();
        if (c.isAnonymousClass()) {
            throw new IllegalArgumentException("Invalid class (Anonymous) %s".formatted(c));
        } else if (c.isSynthetic()) {
            throw new IllegalArgumentException("Invalid class (Synthetic) %s".formatted(c));
        }
    }
}
