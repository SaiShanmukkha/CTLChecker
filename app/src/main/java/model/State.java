package model;

import java.util.LinkedHashSet;
import java.util.Set;

public class State {
    public String stateName;
    public Set<String> atoms;

    public State(String stateName) {
        this.stateName = stateName;
        this.atoms = new LinkedHashSet<>();
    }
    public State(String stateName, KripkeStructure _kripke) {
        for(State state: _kripke.states){
            if(state.stateName.equals(stateName)){
                this.stateName = state.stateName;
                this.atoms = state.atoms;
            }
        }
    }


    
    
    public boolean equals(Object obj) {
        
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof State)) {
            return false;
        }

        State other = (State) obj;
        return this.stateName.equals(other.stateName);
    }
}