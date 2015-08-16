package get.wordy.app.impl  ;


import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {

    private final Timer timer = new Timer();
    private TimerTask timerTask;

    private final int ms = 60 * 1000;

    public Scheduler(TimerTask task) {
        this.timerTask = task;
    }

    public void start(final int minutes) {
        timer.schedule(timerTask, minutes * ms);
    }

    public void postpone(final int delayMinutes) {
        timer.cancel();
        timer.schedule(timerTask, delayMinutes * ms);
    }

}