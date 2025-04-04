package at.base10.state.observer;

import at.base10.state.StateMachine;


/**
 * Represents a subscription to a state machine's observer mechanism.
 * Allows for unregistering the observer from the state machine.
 *
 * @param <S> the type representing the state
 * @param stateMachine the state machine instance
 * @param observer the observer instance subscribed to state changes
 */
public record Subscription<S>(StateMachine<S> stateMachine, Observer<S> observer) {

    /**
     * Unsubscribes the observer from the state machine.
     *
     * @return true if the observer was successfully unregistered, false otherwise
     */
    public boolean unsubscribe(){
        return stateMachine.unregisterObserver(observer);
    }
}
