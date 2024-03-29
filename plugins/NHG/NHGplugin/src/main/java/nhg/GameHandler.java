package nhg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import nhg.Phase.Phase;
import nhg.Phase.Phase0;
import nhg.Phase.Phase1;

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

    private Phase phase0;
    private Phase phase1;

    public GameHandler(NHG basePlugin) {
        this.basePlugin = basePlugin;
        this.phase0 = new Phase0();
        this.phase1 = new Phase1();
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

                for(GamePlayer player : this.basePlugin.getPlayerHandler().getRegisteredPlayers()) {
                    if(!player.getIgnore() && player.getInGame()) {
                        if(player.getInServer()) {
                            player.getPlayer().resetTitle();
                        }
                    }
                }
                
            }
        }
    }

    /**
     * pause the game
     */
    public void pauseGame() {
        this.gamePause = true;

        // lock everything up until resume (handled by NHG event handlers)

        // display titles indefinitely
        for(GamePlayer p : this.basePlugin.getPlayerHandler().getRegisteredPlayers()) {
            if(p.getInGame() && p.getInServer()) {
                p.getPlayer().sendTitle(ChatColor.RED+"Game Paused", "", 20, 9999999, 0);
            }
        }

    }


    /**
     * Set game time manually, use if game is going to slow or something happens to manually set the phase
     * @param time
     */
    public void setTime(int time) {
        this.timer = time;
    }


    /**
     * Try adding a player to the game
     * @param player
     * @param forceAdd force add the player, even if auto adding is no longer allowed
     * @return whether the player was added to a RUNNING game, will still return true if failed to add to a game because a game hasent been started.
     */
    public boolean addPlayer(GamePlayer player, boolean forceAdd) {
        // clear titles, just in case they rejoin after a game was resumed:
        player.getPlayer().resetTitle();

        if(gameStarted) {
            if(((this.allowAutoJoin || forceAdd == true) && player.getInGame() == false) && player.getInServer()) {
                player.setInGame(true);
                this.basePlugin.setGamemode(GameMode.SURVIVAL, player.getPlayer());

                if(this.gamePause) {
                    // send title
                    player.getPlayer().sendTitle(ChatColor.RED+"Game Paused", "", 20, 9999999, 0);
                }
                return true;
            }
            else if(player.getInGame() && player.getInServer()) {
                // if the player was already in a game, if so, rejoin them to that. (PlayerHandler will manage countdown)

                if(this.gamePause) {
                    // send title
                    player.getPlayer().sendTitle(ChatColor.RED+"Game Paused", "", 20, 9999999, 0);
                }
                return(true);
            }

            return false;
        }
        
        return true;
    }

    /**
     * Adds a player as a spectator, this is different than them being dead.
     * @param player
     */
    public void addSpectator(GamePlayer player) {
        if(player.getInServer()) {
            this.basePlugin.setGamemode(GameMode.SPECTATOR, player.getPlayer());
        }
    }






    /**
     * keeps track of phases and events that should happen at certain times
     */
    private void timeKeeper() {

        // TODO: Make this cleaner and configurable by YAML
        if(timer >= 0 && timer < 6*20) {
            phase0();
        }
        else if(timer >= 6*20 && timer < 120*20) {
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

                for(GamePlayer player : this.basePlugin.getPlayerHandler().getRegisteredPlayers()) {
                    if(!player.getIgnore() && player.getInGame()) {
                        Bukkit.getLogger().info(""+player.getPlayerUUID());
                    }
                }
            }

            phase0.tick();
            phase1.tick();

            timer++;
        }
        else {
            // game is paused
        }
    }

    /**
     * get whether or not the game is paused.
     * @return
     */
    public boolean getGamePaused() {
        return(this.gamePause);
    }


    // TODO: Make managing this easier and more abstract.
    // PHASES //

    private void phase0() {
        if(!phase0.isSetup()) {
            Bukkit.getServer().getLogger().info("PHASE 0");

            phase = 0;
            phase0.toggleRunning();
            phase0.setSetup(true);
        }
    }

    private void phase1() {
        if(!phase1.isSetup()) {
            Bukkit.getServer().getLogger().info("PHASE 1");

            phase0.toggleRunning();     // phase 0 no longer should be running

            phase = 1;
            phase1.toggleRunning();
            phase1.setSetup(true);

            this.allowAutoJoin = false; // disable auto joining now.
            
        }
    }

    
}
