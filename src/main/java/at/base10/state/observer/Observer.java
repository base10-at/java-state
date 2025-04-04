package at.base10.state.observer;

/**
 * Observer interface for receiving notifications about state changes.
 *
 * @param <S> the type representing the state
 */
public interface Observer<S> {

    /**
     * Called when a state change occurs.
     *
     * @param observable the event containing the previous and current states
     */
    void next(StateChangeEvent<S> observable);
}

