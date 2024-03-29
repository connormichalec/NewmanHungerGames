package nhg.Phase;

import org.bukkit.Bukkit;

public class Phase0 extends Phase {

    @Override
    protected void tickProcedure() {
        Bukkit.getLogger().info(""+timer);
    }
    
}
