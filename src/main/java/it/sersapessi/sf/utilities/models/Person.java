package it.sersapessi.sf.utilities.models;

import java.sql.Timestamp;

public class Person {
    private int personId;
    private String name;
    private Timestamp joinDate;

    public Person(int personId, String name){
        this.personId=personId;
        this.name=name;

        joinDate = new Timestamp(System.currentTimeMillis());
    }

    public Person(int personId, String name, Timestamp joinDate){
        this.personId=personId;
        this.name=name;

        this.joinDate = joinDate;
    }

    public int getPersonId() {
        return personId;
    }

    public String getName() {
        return name;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Person){
            return ((Person)obj).name.equals(name);
        }
        return false;
    }

    public boolean equalsId(Object obj){
        if(obj instanceof Person){
            return ((Person)obj).personId==personId;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
