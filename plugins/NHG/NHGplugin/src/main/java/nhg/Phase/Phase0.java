package nhg.Phase;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.md_5.bungee.api.ChatColor;
import nhg.BorderManager;
import nhg.GamePlayer;
import nhg.NHG;

public class Phase0 extends Phase {

    private BorderManager borderManager;

    public Phase0(NHG basePlugin, int totalPhaseTime, Phase firstPhase, BorderManager borderManager) {
        super(basePlugin, totalPhaseTime, firstPhase);
        this.borderManager = basePlugin.getBorderManager();
    }


    @Override
    protected void tickProcedure() {
        Bukkit.getLogger().info(""+timer);

        if(timer % 20 == 0) {
            if((totalPhaseTime / 20) - (timer / 20) < 5) {
                for(GamePlayer player : this.basePlugin.getPlayerHandler().getRegisteredPlayers()) {
                    if(!player.getIgnore() && player.getInGame()) {
                        player.getPlayer().sendTitle(ChatColor.GRAY+"Game starting in", ChatColor.GREEN+""+((totalPhaseTime / 20) - (timer / 20) + 1), 10, 10, 10);
                    }
                }
            }
        }
    }


    @Override
    protected void initializeProcedure() {

        this.borderManager.setBorder(new Location(Bukkit.getWorld("world"), 892, 201, -1231), 100, 0);

        Bukkit.getServer().getLogger().info("PHASE 0");

        for(GamePlayer player : this.basePlugin.getPlayerHandler().getRegisteredPlayers()) {
                if(!player.getIgnore() && player.getInGame()) {
                    player.getPlayer().sendMessage("Phase 0, lobby waiting period");
                }
            }
    }
    
}
