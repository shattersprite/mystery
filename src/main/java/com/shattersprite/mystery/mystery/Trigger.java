package com.shattersprite.mystery.mystery;

/**
 * Represents a trigger for a stage
 * This class will be fully implemented in Phase 2
 */
public class Trigger {

    private String type;
    private Object data;

    public Trigger() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
