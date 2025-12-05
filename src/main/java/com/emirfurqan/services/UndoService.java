package com.emirfurqan.services;

import java.util.Stack;

/**
 * Generic Undo service to save and restore previous states.
 * Can be used for Users, Contacts, or other objects.
 */
public class UndoService {

    private final Stack<Object> history = new Stack<>();

    /**
     * Save the previous state for undo
     * @param previousState the object representing the previous state
     */
    public void saveState(Object previousState) {
        if (previousState != null) {
            history.push(previousState);
        }
    }

    /**
     * Undo the last operation by returning the previous state
     * @return the last saved state, or null if none exists
     */
    public Object undoLastOperation() {
        if (!history.isEmpty()) {
            return history.pop();
        } else {
            System.out.println("Nothing to undo.");
            return null;
        }
    }

    /**
     * Check if undo is possible
     * @return true if history is not empty
     */
    public boolean canUndo() {
        return !history.isEmpty();
    }
}
