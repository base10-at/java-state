package at.base10.state.observer;

import at.base10.state.StateMachine;

public record Subscription<S> (StateMachine<S> stateMachine, Observer<S> observer) {
    public boolean unsubscribe(){
       return stateMachine.unregisterObserver(observer);
    }

}
