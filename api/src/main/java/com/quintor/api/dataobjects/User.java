package com.quintor.api.dataobjects;

public class User {
    private int id;
    private String email;
    private int roleId;
    private String userName;

    public User(int id, String email, int roleId, String userName) {
        this.id = id;
        this.email = email;
        this.roleId = roleId;
        this.userName = userName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRoleId() {
        return this.roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
