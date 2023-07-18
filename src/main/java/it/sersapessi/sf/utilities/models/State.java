package it.sersapessi.sf.utilities.models;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class State {
    private int stateId;
    private String stateName;
    private String stateFounder;
    private Timestamp creationDate;
    private int numClaims;

    private ArrayList<Citizen> citizens;
    private ArrayList<CitizenshipRequest> citRequests;

    public State(int stateId, String stateName, String stateFounder, int numClaims, ArrayList<Citizen> citizens) {
        this.stateId=stateId;
        this.stateName = stateName;
        this.stateFounder=stateFounder;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.numClaims = numClaims;
        this.citizens=citizens;

        citRequests=new ArrayList<>();
    }

    public State(int stateId, String stateName, String stateFounder, Timestamp creationDate, int numClaims, ArrayList<Citizen> citizens) {
        this.stateId=stateId;
        this.stateName = stateName;
        this.stateFounder=stateFounder;
        this.creationDate = creationDate;
        this.numClaims = numClaims;
        this.citizens=citizens;

        citRequests=new ArrayList<>();
    }

    public State(State state){
        stateId=state.stateId;
        stateName=state.stateName;
        stateFounder=state.stateFounder;
        creationDate=state.creationDate;
        numClaims=state.numClaims;
        citizens=state.citizens;
        citRequests=state.citRequests;
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
    public void setNumClaims(int numClaims){
        this.numClaims=numClaims;
    }

    public int getNumCitizens() {
        return citizens.size();
    }
    public ArrayList<Citizen> getCitizens(){return citizens;}

    public ArrayList<CitizenshipRequest> getCitRequests() {
        return citRequests;
    }
    public void setCitRequests(ArrayList<CitizenshipRequest> citRequests){this.citRequests=citRequests;}

    @Override
    public String toString() {
        StringBuilder str_builder = new StringBuilder();

        str_builder.append(Constants.ChatStyling.Colors.BLUE).append(Constants.ChatStyling.Formatting.BOLD).append(Constants.ChatStyling.Templates.STATE_TITLE);
        str_builder.append(stateName).append(Constants.ChatStyling.Formatting.RESET).append("\n");

        str_builder.append(Constants.ChatStyling.Colors.BLUE).append(Constants.ChatStyling.Formatting.BOLD)
                    .append(StateFactions.translationManager.getString(Constants.Localization.Str.State.Info.ID))
                    .append(Constants.ChatStyling.Formatting.RESET).append(stateId).append("\n");
        str_builder.append(Constants.ChatStyling.Colors.BLUE).append(Constants.ChatStyling.Formatting.BOLD)
                    .append(StateFactions.translationManager.getString(Constants.Localization.Str.State.Info.FOUNDER))
                    .append(Constants.ChatStyling.Formatting.RESET).append(stateFounder).append("\n");

        //Date format operations
        String date = creationDate.toString().split(" ")[0];
        String years = date.split("-")[0];
        String months = date.split("-")[1];
        String days = date.split("-")[2];
        String actualDate;
        switch (StateFactions.dateFormat) {
            case Constants.Utility.DateFormats.AGF -> actualDate = months + "/" + days + "/" + years;
            case Constants.Utility.DateFormats.EGF -> actualDate = days + "/" + months + "/" + years;
            case Constants.Utility.DateFormats.IDF -> actualDate = date;
            default -> {    //JDF
                int actualMonth = Integer.parseInt(months);
                int actualYear = Integer.parseInt(years);
                int remainingDays = Integer.parseInt(days);
                if (actualMonth > 2) {
                    int february;
                    if ((actualYear % 4 == 0) && (actualYear % 100 != 0 || actualYear % 400 == 0)) {
                        february = 28;
                    } else {
                        february = 29;
                    }

                    int numDays = switch (actualMonth) {
                        case 3 ->     //March
                                february + 31 + remainingDays;
                        case 4 ->     //April
                                february + 31 * 2 + remainingDays;
                        case 5 ->     //May
                                february + 31 * 2 + 30 + remainingDays;
                        case 6 ->     //June
                                february + 31 * 3 + 30 + remainingDays;
                        case 7 ->     //July
                                february + 31 * 3 + 30 * 2 + remainingDays;
                        case 8 ->     //August
                                february + 31 * 4 + 30 * 2 + remainingDays;
                        case 9 ->     //September
                                february + 31 * 5 + 30 * 2 + remainingDays;
                        case 10 ->    //October
                                february + 31 * 5 + 30 * 3 + remainingDays;
                        case 11 ->    //November
                                february + 31 * 6 + 30 * 3 + remainingDays;
                        default ->    //December
                                february + 31 * 6 + 30 * 4 + remainingDays;
                    };

                    actualDate = years + "-" + numDays;
                } else {
                    if (actualMonth == 1) {
                        actualDate = years + days;
                    } else {
                        actualDate = years + (31 + remainingDays);
                    }
                }
            }
        }

        str_builder.append(Constants.ChatStyling.Colors.BLUE).append(Constants.ChatStyling.Formatting.BOLD)
                .append(StateFactions.translationManager.getString(Constants.Localization.Str.State.Info.CREATION_DATE))
                .append(Constants.ChatStyling.Formatting.RESET).append(actualDate).append("\n");

        str_builder.append(Constants.ChatStyling.Colors.BLUE).append(Constants.ChatStyling.Formatting.BOLD)
                    .append(StateFactions.translationManager.getString(Constants.Localization.Str.State.Info.CLAIMS_NUMBER))
                    .append(Constants.ChatStyling.Formatting.RESET).append(numClaims).append("\n");
        str_builder.append(Constants.ChatStyling.Colors.BLUE).append(Constants.ChatStyling.Formatting.BOLD)
                    .append(StateFactions.translationManager.getString(Constants.Localization.Str.State.Info.CITIZENS_NUMBER))
                    .append(Constants.ChatStyling.Formatting.RESET).append(citizens.size()).append("\n");

        return str_builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof State){
            return ((State) obj).stateName.equals(stateName);
        }

        return false;
    }
}
