package nhg;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class PlayerHandler {

    //TODO: Make these all save periodically to a YML, use command to restore game if needed.
    private ArrayList<GamePlayer> playersInLobby = new ArrayList<GamePlayer>();             // players waiting in lobby, before game or after game.
    private ArrayList<GamePlayer> playersAlive = new ArrayList<GamePlayer>();    
    private ArrayList<GamePlayer> playersDead = new ArrayList<GamePlayer>();                // players that are in the game, but are dead
    private ArrayList<GamePlayer> playersSpectators = new ArrayList<GamePlayer>();          // players that are not part of this game, but watching
    private ArrayList<GamePlayer> playersRegistered = new ArrayList<GamePlayer>();   // players that are registerd in the game

    public void joinedPlayer(Player p) {
        // player joined the server
        
        // Check if that player is already in a game, if they are, rejoin them to that game.
        
        // Check if this player

    }

    public void quitPlayer(Player p) {

    }
}
