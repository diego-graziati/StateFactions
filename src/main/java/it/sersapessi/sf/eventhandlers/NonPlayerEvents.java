package it.sersapessi.sf.eventhandlers;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class NonPlayerEvents implements Listener {

    @EventHandler
    public void onBlocksExploding(BlockExplodeEvent event){
        if(StateFactions.config.getBoolean(Constants.Configs.DISABLE_EXPLOSIONS)){
            event.setCancelled(true);
        }
        event.setCancelled(false);
    }
}
