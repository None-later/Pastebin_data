package br.org.rnp.pastebinminerator.minner;

import java.util.List;
import java.util.Timer;

/**
 * @author felipevr
 *
 */
public class ExecutionManager {
    private int numberOfPastes;
    private int timeToRun;
    Timer timer;
    private List<String> languages;

    /**
     * Creats a default ExecutionManager, with 50 pastes retrieved every 1
     * minut.
     */
    public ExecutionManager() {
        this(50, 60, null);
    }

    /**
     * Creats a default ExecutionManager, with <code>numberOfPastes</code>
     * pastes retrieved every <code>timeToRun</code> seconds.
     * 
     * @param i
     *            number of pastes
     * @param j
     *            quantity of seconds
     */
    public ExecutionManager(int i, int j, List<String> languages) {
        super();
        this.numberOfPastes = i;
        this.timeToRun = j * 1000;
        this.languages = languages;
        this.timer = new Timer();
    }

    ExecutionManager(List<String> languages) {
        this(50, 60, languages);
    }

    public void start() {
        new Thread(DataSaver.getInstance()).start();
        DataManageCreator dataManage = new DataManageCreator(this.numberOfPastes, this.languages);
        this.timer.scheduleAtFixedRate(dataManage, 0, this.timeToRun);
    }

    public void cancel() {
        this.timer.cancel();
    }
}