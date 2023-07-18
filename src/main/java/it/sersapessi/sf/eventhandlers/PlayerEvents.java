package it.sersapessi.sf.eventhandlers;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;
import it.sersapessi.sf.utilities.models.ClaimSector;
import it.sersapessi.sf.utilities.models.PluginPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event){
        Player player = event.getPlayer();


        double px=player.getLocation().x();
        double pz=player.getLocation().z();

        int x1= (int)px;
        int z1= (int)pz;

        int x2;
        int z2;
        if(px>=x1){
            x2=x1+1;
        }else{
            x2=x1-1;
        }

        if(pz>=z1){
            z2=z1+1;
        }else{
            z2=z1-1;
        }
        String statePosition = StateFactions.claimsHandler.getClaimOwner(new ClaimSector(x1, z1, x2, z2));
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

        String statePosition = StateFactions.claimsHandler.getClaimOwner(new ClaimSector(player.getLocation().getBlockX(), player.getLocation().getBlockZ(), player.getLocation().getBlockX() + 1, player.getLocation().getBlockZ() + 1));

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

    @EventHandler
    public void onPlayerBlockPlacement(BlockPlaceEvent event){
        if(StateFactions.config.getBoolean(Constants.Configs.CLAIM_PROTECTION)){
            Player player = event.getPlayer();

            double bx=event.getBlockReplacedState().getLocation().x();
            double bz=event.getBlockReplacedState().getLocation().z();

            int x1= (int)bx;
            int z1= (int)bz;

            int x2;
            int z2;
            if(bx>=x1){
                x2=x1+1;
            }else{
                x2=x1-1;
            }

            if(bz>=z1){
                z2=z1+1;
            }else{
                z2=z1-1;
            }
            String statePosition = StateFactions.claimsHandler.getClaimOwner(new ClaimSector(x1, z1, x2, z2));

            if(!statePosition.isBlank()){
                if(StateFactions.statesHandler.checkIfPersonIsCitizen(statePosition,player.getName())){
                    event.setBuild(true);
                    event.setCancelled(false);
                }else{
                    event.setBuild(false);
                    event.setCancelled(true);

                    player.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Event.Error.BLOCK_PLACED_ON_OTHERS_CLAIM)+"\""+statePosition+"\"");
                }
            }
        }

    }

    @EventHandler
    public void onPlayerDirectlyDamagingBlocks(BlockDamageEvent event){
        if(StateFactions.config.getBoolean(Constants.Configs.CLAIM_PROTECTION)){
            Player player = event.getPlayer();

            double bx=event.getBlock().getLocation().x();
            double bz=event.getBlock().getLocation().z();

            int x1= (int)bx;
            int z1= (int)bz;

            int x2;
            int z2;
            if(bx>=x1){
                x2=x1+1;
            }else{
                x2=x1-1;
            }

            if(bz>=z1){
                z2=z1+1;
            }else{
                z2=z1-1;
            }
            String statePosition = StateFactions.claimsHandler.getClaimOwner(new ClaimSector(x1, z1, x2, z2));

            if(!statePosition.isBlank()){
                if(StateFactions.statesHandler.checkIfPersonIsCitizen(statePosition,player.getName())){
                    event.setCancelled(false);
                }else{
                    event.setCancelled(true);

                    player.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Event.Error.BREAKING_BLOCK_ON_OTHERS_CLAIM)+"\""+statePosition+"\"");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSettingThingsOnFire(BlockIgniteEvent event){
        if(StateFactions.config.getBoolean(Constants.Configs.CLAIM_PROTECTION)){
            if(event.getPlayer()!=null){
                Player player = event.getPlayer();

                double bx=event.getBlock().getLocation().x();
                double bz=event.getBlock().getLocation().z();

                int x1= (int)bx;
                int z1= (int)bz;

                int x2;
                int z2;
                if(bx>=x1){
                    x2=x1+1;
                }else{
                    x2=x1-1;
                }

                if(bz>=z1){
                    z2=z1+1;
                }else{
                    z2=z1-1;
                }
                String statePosition = StateFactions.claimsHandler.getClaimOwner(new ClaimSector(x1, z1, x2, z2));

                if(!statePosition.isBlank()){
                    if(StateFactions.statesHandler.checkIfPersonIsCitizen(statePosition,player.getName())){
                        event.setCancelled(false);
                    }else{
                        event.setCancelled(true);

                        player.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Event.Error.BURNING_THINGS_ON_OTHERS_CLAIM)+"\""+statePosition+"\"");
                    }
                }
            }
        }
    }
}
