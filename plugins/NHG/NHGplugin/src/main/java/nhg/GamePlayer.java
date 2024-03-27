package nhg;

public class GamePlayer {
    /**
     * Keep track of each game game player and their corresponding data
     */


    private int score = 0;      
    private boolean alive;              // if true, player is in the game alive, if false, player is dead in spectator mode, but STILL PART OF THE GAME
    private boolean inGame;             // if true player is either alive or spectating, but player IS apart of this game.
    private boolean spectator;          // if true player is spectating, BUT NOT a part of the game (joined late, not part of game)
    private boolean inLobby;            // if true player is in the lobby and not 
    private boolean inCombat;           // if true player is fighting someone or something.
    private boolean timeSinceLeft;      // time since player left game, they have 3 minutes to rejoin! longer they wait, the longer the penalty is.
    private boolean inServer;           // is the player even in the server. (useful for timeSinceLeft counter)

    public GamePlayer() {
        
    }
    
}
