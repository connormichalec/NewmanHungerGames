package nhg.Phase;

import org.bukkit.Bukkit;

import nhg.GameHandler;
import nhg.GamePlayer;
import nhg.NHG;

public class Phase1 extends Phase {

    GameHandler gameHandler;

    public Phase1(NHG basePlugin, int totalPhaseTime, Phase firstPhase, GameHandler gameHandler) {
        super(basePlugin, totalPhaseTime, firstPhase);
        this.gameHandler = gameHandler;

    }

    @Override
    protected void tickProcedure() {
    }

    @Override
    protected void initializeProcedure() {
        Bukkit.getServer().getLogger().info("PHASE 1");

        for(GamePlayer player : this.basePlugin.getPlayerHandler().getRegisteredPlayers()) {
                if(!player.getIgnore() && player.getInGame()) {
                    player.getPlayer().sendMessage("Phase 1, introduction and rules");
                }
            }


        gameHandler.allowAutoJoin = false; // disable auto joining now.
        gameHandler.tpLobby = false;
            
    }
    
}
