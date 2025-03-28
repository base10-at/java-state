package at.base10.state;

public interface State<S> {
    StateMachine<S> transition(Class<? extends S> state);
}
