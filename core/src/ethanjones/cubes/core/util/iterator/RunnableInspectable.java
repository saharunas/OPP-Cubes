package ethanjones.cubes.core.util.iterator;

public class RunnableInspectable implements Inspectable {

    private final Runnable runnable;

    public RunnableInspectable(Runnable runnable) {
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    @Override
    public String inspect() {
        if (runnable == null) return "<null runnable>";
        return "Runnable: " + runnable.getClass().getName();
    }
}