package at.base10.state;

import at.base10.state.observer.Observer;
import at.base10.state.observer.StateChangeEvent;
import at.base10.state.observer.Subscription;
import lombok.NonNull;
import lombok.experimental.PackagePrivate;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Log4j2
class StateMachineImpl<S> implements StateMachine<S> {

    private final Class<S> stateClass;

    @PackagePrivate
    Map<Class<? extends S>, S> states;

    @PackagePrivate
    S currentState;

    private final Map<Observer<S>, Subscription<S>> subscriptions = new HashMap<>();

    StateMachineImpl(Class<S> stateClass) {
        this.stateClass = stateClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StateMachine<S> transitionToState(@NonNull Class<? extends S> state) {
        S previousState = currentState;

        currentState = states.get(state);
        if (currentState == null) {
            throw new IllegalArgumentException("State " + state + " not found");
        }

        var stateChangedEvent = new StateChangeEvent<>(previousState, currentState);
        notifyObservers(stateChangedEvent);
        log.debug("Transition: [{} => {}]",
                () -> stateChangedEvent.previous().getClass().getSimpleName(),
                () -> stateChangedEvent.current().getClass().getSimpleName()
        );

        return this;
    }

    private void notifyObservers(StateChangeEvent<S> stateChangeEvent) {
        subscriptions.keySet().forEach(observer -> observer.next(stateChangeEvent));
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
        return (E) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{state},
                (p, method, args1) -> method.invoke(this.currentState(), args1)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subscription<S> registerObserver(Observer<S> observer) {
        return subscriptions.computeIfAbsent(
                observer,
                k -> new Subscription<>(this, observer)
        );
    }

    /**
     * {@inheritDoc}
     */
    public boolean unregisterObserver(Observer<S> observer) {
        return this.subscriptions.remove(observer) != null;
    }

}
