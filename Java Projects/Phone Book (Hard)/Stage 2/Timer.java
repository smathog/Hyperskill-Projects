package phonebook;

import java.util.concurrent.TimeUnit;

public class Timer {
    private long startTime;
    private long endTime;
    private long elapsedTime;
    private long timeElapsedMinutes;
    private long timeElapsedSeconds;
    private long timeElapsedMS;

    public Timer() { }

    public Timer(Timer t1, Timer t2) {
        long elapseTime = t1.getTimeElapsedTotalMS() + t2.getTimeElapsedTotalMS();
        elapsedTime = elapseTime;
        timeElapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(elapseTime);
        elapseTime -= TimeUnit.MINUTES.toMillis(timeElapsedMinutes);
        timeElapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(elapseTime);
        elapseTime -= TimeUnit.SECONDS.toMillis(timeElapsedSeconds);
        timeElapsedMS = elapseTime;
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
        long elapseTime = endTime - startTime;
        elapsedTime = elapseTime;
        timeElapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(elapseTime);
        elapseTime -= TimeUnit.MINUTES.toMillis(timeElapsedMinutes);
        timeElapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(elapseTime);
        elapseTime -= TimeUnit.SECONDS.toMillis(timeElapsedSeconds);
        timeElapsedMS = elapseTime;
    }

    public long timeSinceStartMS() {
        return System.currentTimeMillis() - startTime;
    }

    public long getTimeElapsedMinutes() {
        return timeElapsedMinutes;
    }

    public long getTimeElapsedSeconds() {
        return timeElapsedSeconds;
    }

    public long getTimeElapsedMS() {
        return timeElapsedMS;
    }

    public long getTimeElapsedTotalMS() {
        return elapsedTime;
    }

    public String elapsedTime() {
        return (getTimeElapsedMinutes() + " min. " + getTimeElapsedSeconds() + " sec. "
                + getTimeElapsedMS() + " ms.");
    }
}
