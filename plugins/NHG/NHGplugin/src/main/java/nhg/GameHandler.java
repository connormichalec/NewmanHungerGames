package nhg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import nhg.Phase.Phase;
import nhg.Phase.Phase0;
import nhg.Phase.Phase1;
import nhg.Phase.Phase2;

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

    public boolean tpLobby = false;    // whether to tp new players to the lobby when they join
    public boolean allowAutoJoin = true;
    private boolean gamePause = true;            // paused until game has started

    private Phase phase0;
    private Phase phase1;
    private Phase phase2;

    private Location lobbyLocation;


    public GameHandler(NHG basePlugin) {
        this.basePlugin = basePlugin;
        this.phase0 = new Phase0(this.basePlugin, 8*20, null, this.basePlugin.getBorderManager());
        this.phase1 = new Phase1(this.basePlugin, 10*20, this.phase0, this);
        this.phase2 = new Phase2(this.basePlugin, 30*20, this.phase0, this.basePlugin.getBorderManager());
        this.phase0.setNextPhase(phase1);
        this.phase1.setNextPhase(phase2);

        this.lobbyLocation = new Location(Bukkit.getWorld("world"), 892, 201, -1231);
    }

    /**
     * start the game
     */
    public void startGame() {

        if(this.gameStarted == false) {
            // game is being started for first time.

            this.gameStarted = true;
            this.gamePause = false;

            // allow auto joining and tp auto to lobby
            this.tpLobby = true;
            this.allowAutoJoin = true;

            // reset everything:

            this.phase = 0;
            this.timer = 0;
        

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

                if(this.tpLobby) {
                    player.getPlayer().teleport(this.lobbyLocation);
                }

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
        if(timer == 0) {
            phase = 0;
            phase0.startPhase(false); // obviously no need to initialize previous states if starting from phase 0.
        }

        if(phase == 0 && phase0.getPassedPhasedTime() >= phase0.getTotalPhaseTime()) {
            phase = 1;
            phase0.nextPhase();
        }
        if(phase == 1 && phase1.getPassedPhasedTime() >=  phase1.getTotalPhaseTime()) {
            phase = 2;
            phase1.nextPhase();
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


            // ticks only register for active phases
            /* 
            phase0.tick();
            phase1.tick();*/
            // Above replaced by this:
            phase0.tickThisAndAllChildPhases();

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


    
}
