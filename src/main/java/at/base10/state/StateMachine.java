package at.base10.state;

import java.util.Collection;

public interface StateMachine<S>  {


    static <S> StateMachineBuilder<S> builder(Class<S> stateClass) {
        return new StateMachineBuilder<>(stateClass);
    }

    StateMachine<S> transition(Class<? extends S> state);

    S state();

    Collection<S> states();

    S asProxy();

    <E> E asProxy(Class<E> state);

}
