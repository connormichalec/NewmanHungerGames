package nhg.Phase;

import nhg.NHG;

public abstract class Phase {

    protected boolean running = false;
    protected int timer = 0;
    protected NHG basePlugin;
    protected int totalPhaseTime;   //total time this phase will run for

    // Phases are a doubly linked list of everything that needs to be running.
    protected Phase childPhase;
    protected Phase headPhase;


    /*
     * Phase class, handles the different sections of the game seperated by time.
     * set firstPhase to null to indicate first phase.
     */
    public Phase(NHG basePlugin, int totalPhaseTime, Phase firstPhase) {
        this.basePlugin = basePlugin;
        this.totalPhaseTime = totalPhaseTime;

        if(firstPhase!=null) {
            this.headPhase = firstPhase;
        }
        else {
            this.headPhase = this;
        }
    }

    /*
     * set nextPhase to null to indicate last phase in the game
     */
    public void setNextPhase(Phase nextPhase) {
        this.childPhase = nextPhase;
    }

    /*
     * Begin Phase
     * if initailizeAll is true it will initialize all of the phases that came before it, desirable in a case like a reset, not desirable for normal phase transitions.
     */
    public void startPhase(boolean initializeAll) {
        if(initializeAll) {
            // Initialize from the head to this
            headPhase.initializePhase(this);
        }
        this.initializePhase(this);
    }

    /*
     * Move to next phase
     */
    public void nextPhase() {
        this.toggleRunning();
        this.childPhase.startPhase(false);    // invoke the start method for the child phase.
    }


    protected void initializePhase(Phase endpoint) {
        // We should initialize all phases older than this one in case a game reset happens and we need to get to the current state all initialization variables will be in place.
        if(this != endpoint) { 
           this.initializeProcedure();
           childPhase.initializePhase(endpoint);
        }
        else {
             // stop initializing further child phases, this should be the currently active phase
            // Done initializing phases! we just need to initialize this one and we are done!
            this.initializeProcedure();

            // start Running the phase
            this.toggleRunning();
        }


    }

    /**
     * Tick all the phases starting at this one and going down, even if they are not running.
     */
    public void tickThisAndAllChildPhases() {
        this.tick();
        if(childPhase!=null) {
            childPhase.tickThisAndAllChildPhases();
        }
    }


    public void tick() {
        if(this.running) {
            timer++;
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
     * 
     * @return
     */
    public int getPassedPhasedTime() {
        return this.timer;
    }

    /**
     * What to do every tick
     */
    protected abstract void tickProcedure();

    /*
     * What should only run when the phase starts. 
     */
    protected abstract void initializeProcedure();

    
}
