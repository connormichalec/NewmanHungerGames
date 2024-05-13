package nhg.Phase;

import nhg.NHG;

public abstract class Phase {

    protected boolean running = false;
    protected int timer = 0;
    protected NHG basePlugin;
    protected int totalPhaseTime;

    public Phase(NHG basePlugin, int totalPhaseTime) {
        this.basePlugin = basePlugin;
        this.totalPhaseTime = totalPhaseTime;
    }

    public void tick() {
        timer++;
        if(this.running) {
            tickProcedure();
        }
    }

    public void toggleRunning() {
        this.running = !this.running;
    }

    /**
     * 
     * @return total expected time to run this phase for. -1 means indefinite 
     */
    public int getTotalPhaseTime() {
        return this.totalPhaseTime;
    }

    /**
     * What to do every tick
     */
    protected abstract void tickProcedure();
    
}
