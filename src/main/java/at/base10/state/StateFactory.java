package at.base10.state;

public interface StateFactory<S> {
    S build(StateMachine<S> stateMachine);
}
