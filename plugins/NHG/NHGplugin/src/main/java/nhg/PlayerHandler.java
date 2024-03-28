package nhg;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerHandler {

    // TODO: make playersRegistered periodically same to YAML/JSON for crashes.
    private ArrayList<GamePlayer> playersRegistered = new ArrayList<GamePlayer>();          // players that are registerd as having joined the server previously

    // these are more for convinience.
    private ArrayList<GamePlayer> playersInLobby = new ArrayList<GamePlayer>();             // players waiting in lobby, before game or after game.
    private ArrayList<GamePlayer> playersAlive = new ArrayList<GamePlayer>();    
    private ArrayList<GamePlayer> playersDead = new ArrayList<GamePlayer>();                // players that are in the game, but are dead
    private ArrayList<GamePlayer> playersSpectators = new ArrayList<GamePlayer>();          // players that are not part of this game, but watchings

    private NHG basePlugin;


    private int timer = 0;

    public PlayerHandler(NHG basePlugin) {
        this.basePlugin = basePlugin;
    }

    public void joinedPlayer(Player p) {
        // player joined the server.
        
        // Check if this player is already registered in the array
        for(GamePlayer player : playersRegistered) {
            if(player.getPlayerUUID() == p.getUniqueId()) {

                // player already in array, make sure to mark them as joined the server.

                player.playerJoined();

                // Check if that player was in a game, if they were check their time penalty and set their inGame status depending.
                if(player.getInGame()) {
                    // player was in a game, penalty for leaving will be bigger if they were in combat
                    if(player.getInCombat()) {
                        
                    }
                    else {

                    }

                    player.timeSinceLeft = 0;
                }
                else {
                    // player is not in game, restore them to spectate.

                }
                return;
            }
        }

        // player is not already registered (new player)

        GamePlayer player = new GamePlayer(p.getUniqueId());
        player.playerJoined();
        playersRegistered.add(player);
        // attempt to add them to the game
        basePlugin.getGameHandler().addPlayer(player, false);
    }

    public void quitPlayer(Player p) {
        for(GamePlayer player : playersRegistered) {
            if(player.getPlayer() == p) {
                player.playerQuit();
            }
        }
    }


    /**
     * Leave the game and teleport to lobby
     * @param p
     */
    public void leaveGame(GamePlayer p) {
        p.getPlayer().teleport(Bukkit.getServer().getWorld("lobby").getSpawnLocation());

        if(!playersInLobby.contains(p)) {playersInLobby.add(p);}

        // if they are in any game arrays, remove them:
        if(playersAlive.contains(p)) {playersAlive.remove(p);}
        if(playersDead.contains(p)) {playersDead.remove(p);}
        if(playersSpectators.contains(p)) {playersSpectators.remove(p);}

        // this will not start a penalty countdown, because players should not be able to do this manually, they should only be able to leave the server manually.
    }

    /**
     * Fetch gameplayer based on their uuid
     * @return GamePlayer obj
     */
    public GamePlayer getGamePlayer(UUID uuid) {

        for(GamePlayer p : playersRegistered) {
            if(p.getPlayerUUID() == uuid) {
                return(p);
            }
        }
        return(null);
    }


    public void tick() {
        // Go through all disconnected players and increment their timeSinceLeft
        
        if(timer%20 == 0) {
            for(GamePlayer p : this.playersRegistered) {
                if(!p.getInServer()) {
                    p.timeSinceLeft++;
                }
            }
        }

        timer++;
    }

    /**
     * 
     * @return players registered
     */
    public ArrayList<GamePlayer> getRegisteredPlayers() {
        return(this.playersRegistered);
    }
}
