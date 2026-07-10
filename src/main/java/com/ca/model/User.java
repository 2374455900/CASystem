package com.ca.model;

public class User {
    private int id;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private String username;
    private String password;
    private String email;
    private String authority;

    public User(String created_at, String updated_at, String deleted_at, String username, String password, String email, String authority) {
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authority = authority;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String updated_at, String password, String username) {
        this.updated_at = updated_at;
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
