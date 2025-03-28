package at.base10.state;

public abstract class Transitional<S> implements State<S> {
    protected final StateMachine<S> stateMachine;

    public Transitional(StateMachine<S> stateMachine) {
        this.stateMachine = stateMachine;
    }

    public StateMachine<S> transition(Class<? extends S> state) {
        return stateMachine.transition(state);
    }
}
