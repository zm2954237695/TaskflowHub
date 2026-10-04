package com.taskflowhub.api.model;

public class Order {
    private Long id;
    private String project;
    private String client;
    private String service;
    private String amount;
    private String status;
    private String deadline;
    private String avatar;

    public Order() {
    }

    public Order(Long id, String project, String client, String service, String amount, String status, String deadline, String avatar) {
        this.id = id;
        this.project = project;
        this.client = client;
        this.service = service;
        this.amount = amount;
        this.status = status;
        this.deadline = deadline;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public String getProject() {
        return project;
    }

    public String getClient() {
        return client;
    }

    public String getService() {
        return service;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setId(Long v) {
        id = v;
    }

    public void setProject(String v) {
        project = v;
    }

    public void setClient(String v) {
        client = v;
    }

    public void setService(String v) {
        service = v;
    }

    public void setAmount(String v) {
        amount = v;
    }

    public void setStatus(String v) {
        status = v;
    }

    public void setDeadline(String v) {
        deadline = v;
    }

    public void setAvatar(String v) {
        avatar = v;
    }
}
