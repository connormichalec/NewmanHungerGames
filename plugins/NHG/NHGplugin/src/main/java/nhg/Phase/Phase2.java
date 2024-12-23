package nhg.Phase;


import org.bukkit.Bukkit;
import org.bukkit.Location;

import nhg.BorderManager;
import nhg.GamePlayer;
import nhg.NHG;

public class Phase2 extends Phase{

    BorderManager borderManager;

    public Phase2(NHG basePlugin, int totalPhaseTime, Phase firstPhase, BorderManager borderManager) {
        super(basePlugin, totalPhaseTime, firstPhase);
        this.borderManager = borderManager;
    }

    @Override
    protected void tickProcedure() {
    }

    @Override
    protected void initializeProcedure() {
        Bukkit.getServer().getLogger().info("PHASE 2 : BORDER TEST");

        borderManager.setBorder(25, 30 * 20);

        for(GamePlayer player : this.basePlugin.getPlayerHandler().getRegisteredPlayers()) {
                if(!player.getIgnore() && player.getInGame()) {
                    player.getPlayer().sendMessage("PHASE 2 : BORDER TEST");
                }
            }


        
    }
    
}
