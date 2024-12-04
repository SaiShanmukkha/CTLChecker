package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class KripkeStructure {
    public List<Transition> transitions;
    public Set<State> states;
    public Set<String> atoms;
    
    public KripkeStructure(String kripkeStructureDefinition) {
        transitions = new ArrayList<>();
        states = new LinkedHashSet<>();
        atoms = new LinkedHashSet<>();

        try {
            List<String> parsedStructure = new ArrayList<>(Arrays.asList(kripkeStructureDefinition.replaceAll("[\\t\\n\\r]+"," ").split(";")));
            int i = 0;
            while (i < parsedStructure.size()) {
                System.out.println(parsedStructure.get(i));
                i++;
            }
            System.out.println(parsedStructure.toString());

            if (parsedStructure.size() != 3)
                throw new IllegalArgumentException("The input file lacks suitable segments to build the Kripke structure.");

            List<String> stateNames = Arrays.asList(parsedStructure.get(0)
                    .replace(" ", "")
                    .split(","));
            List<String> transitions = Arrays.asList(parsedStructure.get(1)
                    .replace(" ", "")
                    .split(","));
            List<String> stateAtomStructures = Arrays.asList(parsedStructure.get(2)
                    .split(","));

            
            for (String stateName : stateNames) {
                State state = new State(stateName);
                if (!states.contains(state))
                    states.add(new State(stateName));
                else
                    throw new IllegalArgumentException(String.format("State %s is defined more than once", stateName));
            }

            
            for (String transition : transitions) {
                List<String> parsedTransition = Arrays.asList(transition.split(":"));

                if (parsedTransition == null || parsedTransition.size() != 2)
                    throw new IllegalArgumentException("Transition format is not valid.");

                String transitionName = parsedTransition.get(0);
                List<String> parsedFromToStates = Arrays.asList(parsedTransition.get(1).split("-"));

                if (parsedFromToStates == null || parsedFromToStates.size() != 2)
                    throw new IllegalArgumentException(String.format("Transition %s is not in [from state] - [to state] format", transitionName));

                String fromStateName = parsedFromToStates.get(0);
                String toStateName = parsedFromToStates.get(1);
                State fromState = FindStateByName(fromStateName);
                State toState = FindStateByName(toStateName);

                if (fromState == null || toState == null)
                    throw new IllegalArgumentException(String.format("Invalid state is detected in transition %s", transitionName));

                Transition transitionObj = new Transition(transitionName, fromState, toState);
                if (!this.transitions.contains(transitionObj))
                    this.transitions.add(transitionObj);
                else {
                    throw new IllegalArgumentException(String.format("Transitions from state %s to state %s are defined more than once"
                            , fromStateName, toStateName));
                }
            }

            
            for (String stateAtomStructure : stateAtomStructures) {
                List<String> parsedStateAtomStructure = Arrays.asList(stateAtomStructure.split(":"));

                if (parsedStateAtomStructure == null || parsedStateAtomStructure.size() !=2)
                    throw new IllegalArgumentException(String.format("%s is not a valid state: atoms definition", stateAtomStructure));
                String stateName = parsedStateAtomStructure.get(0).replace(" ", "");
                String atomNames = parsedStateAtomStructure.get(1).trim();
                List<String> parsedAtoms = Arrays.asList(atomNames.split(" "));

                Set<String> stateAtoms = new LinkedHashSet<>();
                for (String atom : parsedAtoms) {
                    if (atom == null && atom.length() == 0) {
                    } else if (!stateAtoms.contains(atom))
                        stateAtoms.add(atom);
                    else
                        throw new IllegalArgumentException(String.format("Atom %s is defined more than once for state %s"
                                , atom, stateName));
                }

                State stateObj = FindStateByName(stateName);
                if (stateObj == null)
                    throw new IllegalArgumentException(String.format("State %s is not defined", stateName));
                stateObj.atoms = stateAtoms;

                
                for (String atom : stateAtoms) {
                    if (!atoms.contains(atom))
                        atoms.add(atom);
                }
            }
        } catch (IllegalArgumentException fe) {
            throw fe;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid kripke input structure detected.");
        }
    }

    
    
    
    
    
    public State FindStateByName(String stateName) {
        for (State state : states) {
            if (state.stateName.equals(stateName))
                return state;
        }

        return null;
    }

    
    
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("STATES\n");
        sb.append("\n###################\n");
        sb.append(StatesToString());
        sb.append("\n");
        sb.append("\n");
        sb.append("TRANSITIONS\n");
        sb.append("##############\n");
        sb.append(transitionsToString());

        return sb.toString();
    }

    public String StatesToString() {
        StringBuilder sb = new StringBuilder();

        List<String> stateStrings = new ArrayList<>();
        for (State state : states) {
            String atomNames = String.join(", ", state.atoms);
            stateStrings.add(String.format("%s(%s)", state.stateName, atomNames));
        }

        sb.append(String.join(", ", stateStrings));
        return sb.toString();
    }

    public String transitionsToString() {
        StringBuilder sb = new StringBuilder();

        List<String> transitionString = new ArrayList<>();
        for (Transition transition : transitions) {
            transitionString.add(String.format("%s(%s -> %s)", transition.transitionName
                    , transition.fromState.stateName, transition.toState.stateName));
        }

        sb.append(String.join(", ", transitionString));
        return sb.toString();
    }

    public List<String> getStates() {
        List<String> ls = new ArrayList<>();
        for (State state: states) {
            ls.add(state.stateName);
        }
        return ls;
    }
}

