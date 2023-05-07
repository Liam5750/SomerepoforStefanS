package ravenNPlus.client.utils;

public class CoolDown {

    private long start, lasts;
    private boolean checkedFinish;

    public CoolDown(long lasts) {
        this.lasts = lasts;
    }

    public void start() {
        this.start = System.currentTimeMillis();
        checkedFinish = false;
    }

    /**  !WARNING! : this can destroy the cooldown util **/
    public void end() {
        checkedFinish = true;
    }

    public boolean hasFinished() {
        return System.currentTimeMillis() >= start + lasts;
    }

    public boolean firstFinish() {
        if (System.currentTimeMillis() >= start + lasts && !checkedFinish) {
            checkedFinish = true;
            return true;
        }
        return false;
    }

    public void setCooldown(long time) {
        this.lasts = time;
    }

    public long getCooldownTime() {
        return lasts;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.start;
    }

    public long getTimeLeft() {
        return lasts - (System.currentTimeMillis() - start);
    }
}