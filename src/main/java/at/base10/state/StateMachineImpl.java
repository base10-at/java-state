package at.base10.state;

import lombok.NonNull;
import lombok.experimental.PackagePrivate;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Log4j2
class StateMachineImpl<S> implements StateMachine<S> {

    private final Class<S> stateClass;

    Map<Class<? extends S>, S> states;

    @PackagePrivate
    S currentState;


    StateMachineImpl(Class<S> stateClass) {
        this.stateClass = stateClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StateMachine<S> transitionToState(@NonNull Class<? extends S> state) {
        var previousState = currentState;
        currentState = states.get(state);
        if (currentState == null) {
            throw new IllegalArgumentException("State " + state + " not found");
        }
        log.debug("Transition: [{} => {}]",
                () -> previousState.getClass().getSimpleName(),
                state::getSimpleName
        );

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public S currentState() {
        return currentState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public S asState() {
        return asState(stateClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> E asState(Class<E> state) {
        //noinspection unchecked
        return (E) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{state}, (p, method, args1) -> method.invoke(this.currentState(), args1));
    }

}
