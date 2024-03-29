package nhg;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class CommandHandler implements CommandExecutor{

    private NHG basePlugin;

    public CommandHandler(NHG basePlugin) {
        this.basePlugin = basePlugin;
        this.basePlugin.getCommand("nhg").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length>0) {
            switch(args[0]) {
                case "startgame":
                    basePlugin.getGameHandler().startGame();
                    break;
                case "pausegame":
                    basePlugin.getGameHandler().pauseGame();
                    break;
                default:
                    sender.sendMessage(ChatColor.RED+"Invalid arg");
            }
            return true;
        }

        return false;
    }
    
}
