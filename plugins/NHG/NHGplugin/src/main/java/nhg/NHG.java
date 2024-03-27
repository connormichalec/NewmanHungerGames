/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package nhg;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import nhg.PlayerHandler;

public class NHG extends JavaPlugin implements Listener {

    private PlayerHandler playerHandler = new PlayerHandler();

    @Override
    public void onEnable() {
        
    }

    @Override
    public void onDisable() {

    }


    // EVENTS //

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        playerHandler.joinedPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        playerHandler.quitPlayer(e.getPlayer());
    }
}
