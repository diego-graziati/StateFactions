package it.sersapessi.sf.utilities.models;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;

import java.sql.Timestamp;

public class StateInfo {
    private int stateId;
    private String stateName;
    private String stateFounder;
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

    @Override
    public String toString() {
        StringBuilder str_builder = new StringBuilder();

        str_builder.append(Constants.ChatStyling.Colors.BLUE).append(Constants.ChatStyling.Formatting.BOLD).append(Constants.ChatStyling.Templates.STATE_TITLE);
        str_builder.append(stateName).append(Constants.ChatStyling.Formatting.RESET).append("\n");

        str_builder.append(StateFactions.translationManager.getString(Constants.Localization.Str.State.Info.ID)).append(stateId).append("\n");
        str_builder.append(StateFactions.translationManager.getString(Constants.Localization.Str.State.Info.FOUNDER)).append(stateFounder).append("\n");
        str_builder.append(StateFactions.translationManager.getString(Constants.Localization.Str.State.Info.CREATION_DATE)).append(creationDate.toString()).append("\n");
        str_builder.append(StateFactions.translationManager.getString(Constants.Localization.Str.State.Info.CLAIMS_NUMBER)).append(numClaims).append("\n");
        str_builder.append(StateFactions.translationManager.getString(Constants.Localization.Str.State.Info.CITIZENS_NUMBER)).append(numCitizens).append("\n");

        return str_builder.toString();
    }
}
