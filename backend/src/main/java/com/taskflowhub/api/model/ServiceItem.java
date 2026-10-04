package com.taskflowhub.api.model;

public class ServiceItem {
    private Long id;
    private String title;
    private String category;
    private String description;
    private String price;
    private String cycle;
    private String icon;
    private String accent;
    private boolean featured;

    public ServiceItem() {
    }

    public ServiceItem(Long id, String title, String category, String description, String price, String cycle, String icon, String accent, boolean featured) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.price = price;
        this.cycle = cycle;
        this.icon = icon;
        this.accent = accent;
        this.featured = featured;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getCycle() {
        return cycle;
    }

    public String getIcon() {
        return icon;
    }

    public String getAccent() {
        return accent;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setId(Long v) {
        id = v;
    }

    public void setTitle(String v) {
        title = v;
    }

    public void setCategory(String v) {
        category = v;
    }

    public void setDescription(String v) {
        description = v;
    }

    public void setPrice(String v) {
        price = v;
    }

    public void setCycle(String v) {
        cycle = v;
    }

    public void setIcon(String v) {
        icon = v;
    }

    public void setAccent(String v) {
        accent = v;
    }

    public void setFeatured(boolean v) {
        featured = v;
    }
}
