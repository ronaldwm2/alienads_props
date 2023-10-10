package com.marketingapps.classes;
import java.io.Serializable;

public class Campaign implements Serializable {
    private int id;
    private String title;
    private String description;
    private int pricing;
    private String action;

    public Campaign(int id, String title, String description, int pricing, String action) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pricing = pricing;
        this.action = action;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPricing() {
        return pricing;
    }

    public void setPricing(int pricing) {
        this.pricing = pricing;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
