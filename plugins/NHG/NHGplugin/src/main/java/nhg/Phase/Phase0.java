package nhg.Phase;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;
import nhg.GamePlayer;
import nhg.NHG;

public class Phase0 extends Phase {

    public Phase0(NHG basePlugin, int totalPhaseTime) {
        super(basePlugin, totalPhaseTime);
        //TODO Auto-generated constructor stub
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
    
}
