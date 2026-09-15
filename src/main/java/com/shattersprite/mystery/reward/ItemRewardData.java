package com.shattersprite.mystery.reward;

/**
 * Data for item rewards
 */
public class ItemRewardData {

    private String material;
    private int amount;
    private String name;
    private String[] lore;

    public ItemRewardData() {
        this.amount = 1;
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

    public String[] getLore() {
        return lore;
    }

    public void setLore(String[] lore) {
        this.lore = lore;
    }
}
