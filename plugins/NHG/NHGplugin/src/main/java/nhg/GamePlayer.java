package nhg;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GamePlayer {
    /**
     * Game player object and their corresponding data.
     */


    // TODO: Save these in YAML/JSON
    private int score = 0;      
    private boolean alive = false;      // if true, player is in the game alive, if false, player is dead in spectator mode, but STILL PART OF THE GAME
    private boolean inGame = false;     // if true player is either alive or spectating, but player IS apart of this game.
    private boolean spectator = false;  // if true player is spectating, BUT NOT a part of the game (joined late, not part of game)
    private boolean inCombat = false;   // if true player is fighting someone or something.
    private UUID playerUUID;
    private boolean ignore = false;     // if the game should ignore this player for certain things like auto add - useful for admins - use command.

    // do NOT save these in YAML
    private boolean inServer = false;   // is the player even in the server. (useful for timeSinceLeft counter)
    private Player player;
    public int timeSinceLeft = 0;      // time since player left game, they have 3 minutes to rejoin! longer they wait, the longer the penalty is.

    /**
     * Create a fresh game player!
     * Remember to call playerJoined when the player joins the server or playerLeft() when player quits.
     * @param uuid UUID of player to register
     */
    public GamePlayer(UUID uuid) {
        this.playerUUID = uuid;
    }

    public void playerJoined() {
        this.player = Bukkit.getPlayer(playerUUID);
        this.inServer = true;
    }

    public void playerQuit() {
        this.player = null;
        this.inServer = false;
        
    }

    public Player getPlayer() {
        return(this.player);
    }

    /**
     * Get whether or not player is in the game (NOT THE SERVER, they could be in the lobby and this is false)
     * @return bool
     */
    public boolean getInGame() {
        return(this.inGame);
    }

    public int getTimeSinceLeft() {
        return(this.timeSinceLeft);
    }

    public void setTimeSinceLeft(int time) {
        this.timeSinceLeft = time;
    }

    /**
     * Set whether or not the player is in game
     * @param inGame
     */
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    /**
     * Get whether or not player is currently fighting someone, this affects how penalty will be applied if the player leaves
     * @return entity player is in combat with
     */
    public boolean getInCombat() {
        return(this.inCombat);
    }

    /**
     * @return is player in this minecraft server
     */
    public boolean getInServer() {
        return(this.inServer);        
    }

    public UUID getPlayerUUID() {
        return(this.playerUUID);
    }

    public boolean getIgnore() {
        return(this.ignore);
    }
    
}
