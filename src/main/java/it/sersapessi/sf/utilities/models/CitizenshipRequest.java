package it.sersapessi.sf.utilities.models;

import java.sql.Time;
import java.sql.Timestamp;

public class CitizenshipRequest {

    private int personId;
    private String personName;
    private Timestamp requestDate;

    public CitizenshipRequest(int personId, String personName) {
        this.personId=personId;
        this.personName = personName;
        this.requestDate = new Timestamp(System.currentTimeMillis());
    }

    public CitizenshipRequest(int personId, String personName, Timestamp requestDate) {
        this.personId=personId;
        this.personName = personName;
        this.requestDate = requestDate;
    }

    public int getPersonId() {
        return personId;
    }

    public String getPersonName() {
        return personName;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CitizenshipRequest){
            return ((CitizenshipRequest)obj).personName.equals(personName);
        }

        return false;
    }
}
