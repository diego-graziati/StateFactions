package it.sersapessi.sf.utilities.models;

import org.bukkit.entity.Player;

public class PluginPlayer {
    private Player bukkitPlayer;
    private String statePosition;

    public PluginPlayer(Player bukkitPlayer,String statePosition){
        this.bukkitPlayer=bukkitPlayer;
        this.statePosition=statePosition;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public String getStatePosition() {
        return statePosition;
    }

    public void setStatePosition(String statePosition) {
        this.statePosition = statePosition;
    }
}
