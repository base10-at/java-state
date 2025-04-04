package at.base10.state.observer;

public interface Observer<S> {

    void next(StateChangeEvent<S> observable);
}
