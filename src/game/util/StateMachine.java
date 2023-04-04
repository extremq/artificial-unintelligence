package game.util;

import java.security.InvalidParameterException;
import java.util.Objects;

public class StateMachine {
    private final String[] states;
    private String currentState;

    /**
     * @param states The states of the state machine. Once set, they are read-only.
     */
    public StateMachine(String[] states, String startState) {
        this.states = states;
        this.currentState = startState;
    }

    /**
     * @param stateToTransitionTo The state to transition the state machine to.
     *                            If the state does not exist, throws InvalidParameterException.
     */
    public void transitionTo(String stateToTransitionTo) {
        for (String state : states) {
            if (Objects.equals(state, stateToTransitionTo)) {
                this.currentState = state;
                return;
            }
        }

        throw new InvalidParameterException();
    }

    /**
     * @return Possible states of the state machine.
     */
    public String[] getStates() {
        return states;
    }

    /**
     * @return Current state of the state machine.
     */
    public String getCurrentState() {
        return currentState;
    }
}
