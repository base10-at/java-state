package at.base10.state;

import lombok.NonNull;
import lombok.experimental.PackagePrivate;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Log4j2
class StateMachineImpl<S> implements StateMachine<S> {

    private final Class<S> stateClass;

    final Map<Class<? extends S>, S> states = new HashMap<>();

    @PackagePrivate
    S currentState;


    StateMachineImpl(Class<S> stateClass) {
        this.stateClass = stateClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StateMachine<S> transition(@NonNull Class<? extends S> state) {
        var previousState = currentState;
        currentState = states.get(state);
        if (currentState == null) {
            throw new IllegalArgumentException("State " + state + " not found");
        }
        log.trace("Transition: [{} => {}]", () -> previousState.getClass().getSimpleName(), state::getSimpleName);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public S state() {
        return currentState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<S> states() {
        return states.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public S asProxy() {
        return asProxy(stateClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> E asProxy(Class<E> state) {
        //noinspection unchecked
        return (E) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{state},
                (p, method, args1) -> method.invoke(this.state(), args1)
        );
    }

}
