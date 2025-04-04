package at.base10.state.observer;

public record StateChangeEvent<S>(S previous, S current) {
    public boolean didChange() {
        return previous != current;
    }
}
