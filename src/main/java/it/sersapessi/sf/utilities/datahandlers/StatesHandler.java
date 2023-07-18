package it.sersapessi.sf.utilities.datahandlers;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.models.Citizen;
import it.sersapessi.sf.utilities.models.CitizenshipRequest;
import it.sersapessi.sf.utilities.models.State;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class StatesHandler {

    //All states are listed here
    private ArrayList<State> states;

    public StatesHandler(ArrayList<State> states){
        this.states=states;
    }

    public int getStateId(String stateName){
        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        return states.get(index).getStateId();
    }

    public String getStateName(int stateId){

        for(State state : states){
            if(state.getStateId()==stateId){
                return state.getStateName();
            }
        }

        return "";
    }

    public boolean checkIfStateExists(String stateName){
        return states.contains(new State(-1,stateName,"",0,new ArrayList<>()));
    }

    public void createState(String stateName, String founder){
        int newIndex = states.size();

        ArrayList<Citizen> citizens = new ArrayList<>();
        citizens.add(new Citizen(StateFactions.peopleHandler.getPlayerId(founder), founder,true,true));

        states.add(new State(newIndex,stateName,founder,0,citizens));
    }

    public void addCitizen(String stateName, String citizenName){
        State selectedState = null;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        selectedState.getCitizens().add(new Citizen(StateFactions.peopleHandler.getPlayerId(citizenName), citizenName,false,false));
    }

    public void addStateOwner(String stateName, String citizenName){
        State selectedState;

        State test=new State(-1,stateName,"",0,new ArrayList<>());

        StateFactions.logger.log(new LogRecord(Level.INFO,"states contains test: "+states.contains(test)));

        int index = states.indexOf(test);

        selectedState = states.get(index);

        int citIndex = selectedState.getCitizens().indexOf(new Citizen(StateFactions.peopleHandler.getPlayerId(citizenName), citizenName,false,false));

        Citizen selectedCit = selectedState.getCitizens().get(citIndex);

        selectedCit.setStateOwner(true);
        selectedCit.setClaimResponsible(true);
    }

    public void addClaimResponsible(String stateName, String citizenName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        int citIndex = selectedState.getCitizens().indexOf(new Citizen(StateFactions.peopleHandler.getPlayerId(citizenName), citizenName,false,false));

        Citizen selectedCit = selectedState.getCitizens().get(citIndex);

        selectedCit.setClaimResponsible(true);
    }

    public void removeCitizen(String stateName, String citizenName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        selectedState.getCitizens().removeIf(cit -> cit.getName().equals(citizenName));
    }

    public void removeStateOwner(String stateName, String citizenName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        int citIndex = selectedState.getCitizens().indexOf(new Citizen(StateFactions.peopleHandler.getPlayerId(citizenName), citizenName,false,false));

        Citizen selectedCit = selectedState.getCitizens().get(citIndex);

        selectedCit.setStateOwner(false);
    }

    public void removeClaimResponsible(String stateName, String citizenName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        int citIndex = selectedState.getCitizens().indexOf(new Citizen(StateFactions.peopleHandler.getPlayerId(citizenName), citizenName,false,false));

        Citizen selectedCit = selectedState.getCitizens().get(citIndex);

        selectedCit.setClaimResponsible(false);
    }

    public boolean checkIfPersonIsCitizen(String stateName, String personName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        return selectedState.getCitizens().contains(new Citizen(StateFactions.peopleHandler.getPlayerId(personName), personName,false,false));
    }

    public boolean checkIfCitIsStateOwner(String stateName, String personName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        int citIndex = selectedState.getCitizens().indexOf(new Citizen(StateFactions.peopleHandler.getPlayerId(personName), personName,false,false));

        Citizen selectedCit = selectedState.getCitizens().get(citIndex);

        return selectedCit.isStateOwner();
    }

    public boolean checkIfCitIsClaimResponsible(String stateName, String personName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        int citIndex = selectedState.getCitizens().indexOf(new Citizen(StateFactions.peopleHandler.getPlayerId(personName), personName,false,false));

        Citizen selectedCit = selectedState.getCitizens().get(citIndex);

        return selectedCit.isClaimResponsible();
    }

    public void addCitRequest(String stateName, String personName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        selectedState.getCitRequests().add(new CitizenshipRequest(StateFactions.peopleHandler.getPlayerId(personName), personName));

    }

    public void removeCitRequest(String stateName, String personName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        selectedState.getCitRequests().removeIf(citReq -> citReq.getPersonName().equals(personName));

    }

    public boolean checkIfCitRequestExists(String stateName, String personName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        return selectedState.getCitRequests().contains(new CitizenshipRequest(StateFactions.peopleHandler.getPlayerId(personName), personName));
    }

    public void incrementClaims(String stateName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        selectedState.setNumClaims(selectedState.getNumClaims()+1);
    }

    public void decrementClaims(String stateName){
        State selectedState;

        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        selectedState = states.get(index);

        selectedState.setNumClaims(selectedState.getNumClaims()-1);
    }

    public State getStateInfo(String stateName){
        int index = states.indexOf(new State(-1,stateName,"",0,new ArrayList<>()));

        return states.get(index);
    }

    public ArrayList<State> getStates() {
        return states;
    }
}
