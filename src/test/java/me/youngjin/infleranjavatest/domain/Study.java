package me.youngjin.infleranjavatest.domain;

public class Study {

    private int limit;

    private String name;

    public Study(int limit, String name) {
        this.limit = limit;
        this.name = name;
    }

    public void setOwner(Member member) {
    }
}
