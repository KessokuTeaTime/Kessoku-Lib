package band.kessoku.lib.data.impl.structure;

import band.kessoku.lib.data.api.AbstractNBTStructure;
import band.kessoku.lib.data.api.MutableData;
import band.kessoku.lib.data.impl.base.IntData;

public final class ProgressStructure extends AbstractNBTStructure {
    public final MutableData<Integer> progressTime;
    public final MutableData<Integer> progressTimeTotal;

    private ProgressStructure(String id, int timeTotal) {
        super(id);
        progressTime = integrate(IntData.create("Time", 0));
        progressTimeTotal = integrate(IntData.create("TimeTotal", timeTotal));
    }

    public static ProgressStructure create(String id, int timeTotal) {
        return new ProgressStructure(id, timeTotal);
    }

    public int proportion() {
        return (int) (percentage() * 100);
    }

    public double percentage() {
        return (double) progressTime.get() / (double) progressTimeTotal.get();
    }

    public void setTotalTime(int totalTime) {
        progressTimeTotal.set(totalTime);
    }

    public boolean isCompleted() {
        return progressTime.get() >= progressTimeTotal.get();
    }

    public void reset() {
        progressTime.set(0);
    }

    public void increase() {
        increase(1);
    }

    public void increase(int time) {
        progressTime.set(progressTime.get() + time);
    }

    public void decrease(int time) {
        progressTime.set(progressTime.get() - time);
    }
}
