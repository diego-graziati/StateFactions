package it.sersapessi.sf.utilities.datahandlers;

import it.sersapessi.sf.utilities.models.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PersonHandler {
    //Person instance, Pwd instance
    private HashMap<Person,String> registeredPlayers;

    public PersonHandler(HashMap<Person,String> registeredPlayers){
        this.registeredPlayers=registeredPlayers;
    }

    public boolean checkPwd(String playerName, String pwd){
        return registeredPlayers.get(new Person(-1,playerName)).equals(pwd);
    }

    public boolean checkIfPlayerIsRegistered(String playerName){
        return registeredPlayers.containsKey(new Person(-1,playerName));
    }

    public int getPlayerId(String playerName){
        Person playerInstance=null;

        Set<Person> playersSet = registeredPlayers.keySet();

        for(Person player : playersSet){
            if(player.equals(new Person(-1,playerName))){
                playerInstance=player;
                break;
            }
        }

        if(playerInstance==null){
            return -1;
        }else{
            return playerInstance.getPersonId();
        }
    }

    public String getPlayerName(int playerId){
        Person playerInstance=null;

        Set<Person> playersSet = registeredPlayers.keySet();

        for(Person player : playersSet){
            if(player.equalsId(new Person(playerId,""))){
                playerInstance=player;
                break;
            }
        }

        if(playerInstance==null){
            return null;
        }else{
            return playerInstance.getName();
        }
    }

    public void registerPerson(String playerName, String pwd){
        int newIndex = registeredPlayers.size();

        registeredPlayers.put(new Person(newIndex,playerName),
                                pwd);
    }

    public HashMap<Person, String> getRegisteredPlayers() {
        return registeredPlayers;
    }
}
