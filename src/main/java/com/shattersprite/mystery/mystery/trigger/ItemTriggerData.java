package com.shattersprite.mystery.mystery.trigger;

/**
 * Data for item-based triggers
 */
public class ItemTriggerData {

    private String material;
    private int amount;
    private String name;
    private boolean exactMatch;

    public ItemTriggerData() {
        this.amount = 1;
        this.exactMatch = false;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExactMatch() {
        return exactMatch;
    }

    public void setExactMatch(boolean exactMatch) {
        this.exactMatch = exactMatch;
    }
}
