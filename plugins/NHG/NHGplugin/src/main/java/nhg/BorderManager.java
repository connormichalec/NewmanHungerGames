package nhg;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

/*
 * Manages the world border for the game
 */
public class BorderManager {

    private NHG basePlugin;

    private World gameWorld;
    private WorldBorder wb;

    private Location targetCenter;
    private double targetSize;

    // how much does the border need to move each step to arrive to its new position in time
    private double centerVectorX = 0;
    private double centerVectorZ = 0;
    private double sizeVector = 0;

    private int transitionTimePassed;
    private int targetTransitionTime;
    
    public BorderManager(NHG basePlugin) {
        this.basePlugin = basePlugin;
        this.gameWorld = this.basePlugin.getGameWorld();
        this.wb = this.gameWorld.getWorldBorder();

    }

    // needed for border transitions.
    public void tick() {

        if(transitionTimePassed!=targetTransitionTime) {
        
            double x = wb.getCenter().getX();
            double z = wb.getCenter().getZ();

            x += centerVectorX;
            z += centerVectorZ;
            wb.setCenter(new Location(gameWorld, x, 0, z));

            double nsize = wb.getSize();
            nsize += sizeVector;
            wb.setSize(nsize);
            transitionTimePassed++;
        }
        else {
            transitionTimePassed = 0;
            targetTransitionTime = 0;
            sizeVector = 0;
            centerVectorX = 0;
            centerVectorZ = 0;
        }
    }

    /*
     * Reset border to its initial state.
     */
    public void reset() {
        wb.reset();
    }


    public void setInnerParticles(Location center) {

    }

    // time being the time it takes to move worldborder to that new size and position.

    public void setBorder(Location center, double size, int transitionTime) {
        if(transitionTime == 0) {transitionTime = 1;}
        targetCenter = center;
        targetSize = size;

        // distance/time w/ direction in mind (negatives)
        centerVectorX = (targetCenter.getX() - wb.getCenter().getX()) / transitionTime;
        centerVectorZ = (targetCenter.getZ() - wb.getCenter().getZ()) / transitionTime;

        sizeVector = (targetSize - wb.getSize()) / transitionTime;

        this.targetTransitionTime = transitionTime;
        this.transitionTimePassed = 0;

}

    public void setBorder(double size, int transitionTime) {
        if(transitionTime == 0) {transitionTime = 1;}

        targetCenter = wb.getCenter();
        targetSize = size;

        sizeVector = (targetSize - wb.getSize()) / transitionTime;

        this.targetTransitionTime = transitionTime;
        this.transitionTimePassed = 0;

    }
    
    
}
