package at.base10.state;

import java.util.ArrayList;
import java.util.Collection;

public class StateMachineBuilder<S> {
    final Collection<StateFactory<S>> statesBuilders = new ArrayList<>();
    final Class<S> stateClass;

    public StateMachineBuilder(Class<S> stateClass) {
        this.stateClass = stateClass;
    }

    public StateMachineBuilder<S> register(StateFactory<S> stateBuilder) {
        if (stateBuilder == null) {
            throw new NullPointerException("stateBuilder is null");
        }
        statesBuilders.add(stateBuilder);
        return this;
    }

    public StateMachineBuilder<S> register(Collection<StateFactory<S>> stateBuilders) {
        stateBuilders.forEach(this::register);
        return this;
    }

    public StateMachine<S> build(Class<? extends S> initialState) {
        if (initialState == null) {
            throw new NullPointerException("initialState is null");
        }
        StateMachineImpl<S> stateMachine = getStateMachine(statesBuilders);

        stateMachine.currentState = stateMachine.states.get(initialState);
        if (stateMachine.currentState == null) {
            throw new IllegalArgumentException("Cannot find state " + initialState);
        }
        return stateMachine;
    }

    private StateMachineImpl<S> getStateMachine(Collection<StateFactory<S>> states) {
        StateMachineImpl<S> stateMachine = new StateMachineImpl<>(stateClass);
        states.stream()
                .map(e -> e.build(stateMachine))
                .forEach(state -> {
                    validateClass(state);
                    //noinspection unchecked
                    stateMachine.states.put((Class<S>) state.getClass(), state);
                });
        return stateMachine;
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
