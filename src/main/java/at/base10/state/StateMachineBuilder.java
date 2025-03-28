package at.base10.state;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log4j2
public class StateMachineBuilder<S> {
    final Collection<StateFactory<S>> statesBuilders = new ArrayList<>();
    final Class<S> stateClass;

    public StateMachineBuilder(@NonNull Class<S> stateClass) {
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

    public StateMachine<S> build(@NonNull Class<? extends S> initialState) {
        StateMachineImpl<S> stateMachine = new StateMachineImpl<>(stateClass);
        registerStates(stateMachine, buildStates(stateMachine));
        setInitialState(initialState, stateMachine);
        return stateMachine;
    }

    private static <S> void setInitialState(@NonNull Class<? extends S> initialState,
                                            @NonNull StateMachineImpl<S> stateMachine) {
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
