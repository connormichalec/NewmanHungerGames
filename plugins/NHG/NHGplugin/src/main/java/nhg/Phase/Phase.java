package nhg.Phase;

public abstract class Phase {

    protected boolean setup = false;        // has this phase been setup yet.
    protected boolean running = false;
    protected int timer = 0;

    public void tick() {
        timer++;
        if(this.running) {
            tickProcedure();
        }
    }

    public boolean isSetup() {

        return(this.setup);
    }

    public void setSetup(boolean setup) {
        this.setup = setup;
    }

    public void toggleRunning() {
        this.running = !this.running;
    }

    /**
     * What to do every tick
     */
    protected abstract void tickProcedure();
    
}
