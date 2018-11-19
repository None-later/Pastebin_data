package br.org.rnp.pastebinminerator.minner;

import java.util.List;
import java.util.TimerTask;

/**
 * @author felipevr
 *
 */
public class DataManageCreator extends TimerTask {

    private int numberOfPastes;
    private List<String> languages;

    /**
     * @param numbersOfPastes
     * @param languages
     */
    public DataManageCreator(int numberOfPastes, List<String> languages) {
        this.numberOfPastes = numberOfPastes;
        this.languages = languages;
    }

    /* (non-Javadoc)
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        DataManage dataManage = new DataManage(this.numberOfPastes, this.languages);
        dataManage.start();
    }
}