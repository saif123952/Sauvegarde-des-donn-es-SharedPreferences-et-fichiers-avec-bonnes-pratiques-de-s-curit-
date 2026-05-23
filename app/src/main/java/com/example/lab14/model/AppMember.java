package com.example.lab14.model;

public class AppMember {
    public final int identifier;
    public final String fullName;
    public final int seniority;

    public AppMember(int identifier, String fullName, int seniority) {
        this.identifier = identifier;
        this.fullName = fullName;
        this.seniority = seniority;
    }
}