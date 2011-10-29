package net.n4th4.bukkit.nuxsigns;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.getspout.spoutapi.player.SpoutPlayer;

public class NSPlayerListener extends PlayerListener {
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().hasPermission("nuxsigns.use") && event.getPlayer().getItemInHand().getType() == Material.AIR) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                ((SpoutPlayer) event.getPlayer()).openSignEditGUI((Sign) event.getClickedBlock().getState());
            }
        }
    }
}
