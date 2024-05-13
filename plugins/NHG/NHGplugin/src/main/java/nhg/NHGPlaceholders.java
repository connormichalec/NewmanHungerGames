package nhg;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class NHGPlaceholders extends PlaceholderExpansion {

     private final NHG plugin;
    
    public NHGPlaceholders(NHG plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getAuthor() {
        return "Connor Michalec";
    }
    
    @Override
    public String getIdentifier() {
        return "nhg";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("score")){
            return ""+this.plugin.getPlayerHandler().getGamePlayer(player.getUniqueId()).getScore();
        }
        
        return null; // Placeholder is unknown by the Expansion
    }
}
