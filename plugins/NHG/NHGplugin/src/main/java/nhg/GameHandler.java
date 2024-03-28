package nhg;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class GameHandler implements Listener {


    /**
     * Handles all things related to the status of the game
     */

     private NHG basePlugin;

    // TODO: Save these to YAML/JSON in case crash

    private boolean gameStarted = false;

    /**
     * Phases:
     * 0 - Countdown, pre-game start, players will still be automatically added to the game in this stage.
     * 1 - Rules, Introduction.
     */
    private int phase;
    private int timer;

    private boolean gamePause = true;            // paused until game has started
    private boolean allowAutoJoin = true;

    // TODO: Make this cleaner and automatic
    private boolean[] phaseSetup =               // has this phase already ran its setup method?
    {false, false};   
    
    
    public GameHandler(NHG basePlugin) {
        this.basePlugin = basePlugin;
    }

    /**
     * start the game
     */
    public void startGame() {

        if(this.gameStarted == false) {
            // game is being started for first time.

            this.gameStarted = true;
            this.gamePause = false;

            // reset everything:

            this.phase = 0;
            this.timer = 0;
            this.allowAutoJoin = true;
        

            // add all players EXECPT those marked as ignore in the server to game
            for(GamePlayer player : this.basePlugin.getPlayerHandler().getRegisteredPlayers()) {
                if(!player.getIgnore() && player.getInServer()) {
                    this.addPlayer(player, false);
                }
            }
        }
        else {
            if(this.gamePause == true) {
                // game has been paused, resume and unlock everyone.
                this.gamePause = false;
            }
        }
    }

    /**
     * pause the game
     */
    public void pauseGame() {
        this.gamePause = true;

        // lock everything up until resume
    }


    /**
     * Set game time manually, use if game is going to slow or something happens to manually set the phase
     * @param time
     */
    public void setTime(int time) {

    }


    /**
     * Try adding a player to the game
     * @param player
     * @param forceAdd force add the player, even if auto adding is no longer allowed
     * @return whether the player was added to the game (true) or not (false)
     */
    public boolean addPlayer(GamePlayer player, boolean forceAdd) {
        if(gameStarted) {
            if(this.allowAutoJoin || forceAdd == true && player.getInGame() == false) {
                player.setInGame(true);
                Bukkit.getLogger().info("Player joined successfully");
                return true;
            }
            else if(player.getInGame()) {
                // determine if the player was already in a game, if so, rejoin them to that. (PlayerHandler will manage countdown)
                Bukkit.getLogger().info("Player joined successfully");
                return(true);
            }
        }
        
        Bukkit.getLogger().info("Player DIDFNT joined successfully");
        return false;
    }

    /**
     * keeps track of phases and events that should happen at certain times
     */
    private void timeKeeper() {

        // TODO: Make this cleaner and configurable by YAML
        if(timer >= 0 && timer < 60*20) {
            phase0();
        }
        else if(timer >= 60*20 && timer < 120*20) {
            phase1();
        }
        
        
    }


    /**
     * Tick this every tick
     */
    public void tick() {
        if(!gamePause) {

            // only run the timekeeper every second, no need to do it every tick
            if(timer % 20 == 0) {
                this.timeKeeper();
            }

            timer++;
        }
    }


    // PHASES //

    private void phase0() {
        if(!phaseSetup[0]) {
            // run phase setup
            Bukkit.getServer().getLogger().info("PHASE 0");
            phase = 0;
            phaseSetup[0] = true;
        }
    }

    private void phase1() {
        if(!phaseSetup[1]) {
            // run phase setup
            Bukkit.getServer().getLogger().info("PHASE 1");
            phase = 1;
            phaseSetup[1] = true;

            this.allowAutoJoin = false;

            
        }
    }

    
}
