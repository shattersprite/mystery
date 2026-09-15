package com.shattersprite.mystery.mystery.trigger;

/**
 * Data for interaction-based triggers
 */
public class InteractionTriggerData {

    private String blockType;
    private String interactionType; // block, button, lever, pressure_plate, npc

    public InteractionTriggerData() {
        this.interactionType = "block";
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }
}
