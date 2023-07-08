package it.sersapessi.sf.utilities.models;

import java.sql.Timestamp;

public class StateInfo {
    private int stateId;
    private String stateName;
    public String stateFounder;
    private Timestamp creationDate;
    private int numClaims;
    private int numCitizens;

    public StateInfo(int stateId, String stateName, String stateFounder, Timestamp creationDate, int numClaims, int numCitizens) {
        this.stateId=stateId;
        this.stateName = stateName;
        this.stateFounder=stateFounder;
        this.creationDate = creationDate;
        this.numClaims = numClaims;
        this.numCitizens = numCitizens;
    }

    public int getStateId() {return stateId;}
    public String getStateName() {
        return stateName;
    }

    public String getStateFounder(){return stateFounder;}
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public int getNumClaims() {
        return numClaims;
    }

    public int getNumCitizens() {
        return numCitizens;
    }
}
