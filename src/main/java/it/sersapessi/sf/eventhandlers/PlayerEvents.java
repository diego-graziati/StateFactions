package it.sersapessi.sf.eventhandlers;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;
import it.sersapessi.sf.utilities.models.ClaimSector;
import it.sersapessi.sf.utilities.models.PluginPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event){
        Player player = event.getPlayer();

        String statePosition = StateFactions.db.getStateNameByPosition(new ClaimSector(player.getLocation().getBlockX(),player.getLocation().getBlockZ(),player.getLocation().getBlockX()+1,player.getLocation().getBlockZ()+1));
        PluginPlayer pluginPlayer = StateFactions.getPlayer(player.getName());

        if(pluginPlayer!=null){
            if(!pluginPlayer.getStatePosition().equals(statePosition)){
                String pastStatePosition = pluginPlayer.getStatePosition();

                if(pastStatePosition.equals("")){
                    player.sendTitle(Constants.ChatStyling.Colors.GOLD+statePosition,"",5,20,5);
                }else if(statePosition.equals("")){
                    player.sendTitle(Constants.ChatStyling.Colors.GOLD+StateFactions.translationManager.getString(Constants.Localization.Str.Title.EXITING_STATE)+"\""+pastStatePosition+"\"","",5,20,5);
                }else{
                    player.sendTitle(Constants.ChatStyling.Colors.GOLD+statePosition,Constants.ChatStyling.Colors.BLUE+StateFactions.translationManager.getString(Constants.Localization.Str.Title.EXITING_STATE)+"\""+pastStatePosition+"\"",5,20,5);
                }
                pluginPlayer.setStatePosition(statePosition);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        String statePosition = StateFactions.db.getStateNameByPosition(new ClaimSector(player.getLocation().getBlockX(),player.getLocation().getBlockZ(),player.getLocation().getBlockX()+1,player.getLocation().getBlockZ()+1));

        StateFactions.addOnlinePlayer(new PluginPlayer(player,statePosition));
    }

    @EventHandler
    public void onPlayerKicked(PlayerKickEvent event){
        StateFactions.loggedInPlayers.remove(event.getPlayer().getName());

        StateFactions.removePlayer(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerLoseConnection(PlayerConnectionCloseEvent event){
        StateFactions.loggedInPlayers.remove(event.getPlayerName());

        StateFactions.removePlayer(event.getPlayerName());
    }

    @EventHandler
    public void onPlayerQuitting(PlayerQuitEvent event){
        StateFactions.loggedInPlayers.remove(event.getPlayer().getName());

        StateFactions.removePlayer(event.getPlayer().getName());
    }
}
