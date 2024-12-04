package model;

public class Transition {
    public String transitionName;
    public State fromState;
    public State toState;

    public Transition(State fromState, State toState) {
        this.transitionName = "";
        this.fromState = fromState;
        this.toState = toState;
    }

    public Transition(String transitionName, State fromState, State toState) {
        this.transitionName = transitionName;
        this.fromState = fromState;
        this.toState = toState;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Transition)) {
            return false;
        }

        Transition other = (Transition) obj;
        if (this.fromState == other.fromState && this.toState == other.toState) {
            return true;
        }
        return false;
    }
}