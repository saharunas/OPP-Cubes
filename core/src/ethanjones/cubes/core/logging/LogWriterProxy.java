package ethanjones.cubes.core.logging;

public class LogWriterProxy implements LogWriter {

    private LogWriter realWriter;
    private final LogWriterFactory factory;
    private final LogLevel minLogLevel;

    public LogWriterProxy(LogWriterFactory factory, LogLevel minLogLevel) {
        this.factory = factory;
        this.minLogLevel = minLogLevel;
    }

    private LogWriter getRealWriter() {
        if (realWriter == null) {
            realWriter = factory.create();
        }
        return realWriter;
    }

    private boolean checkAccess(LogLevel level) {
        return level.ordinal() >= minLogLevel.ordinal();
    }

    @Override
    public void log(LogLevel level, String message) {
        if (!checkAccess(level)) return;
        getRealWriter().log(level, message);
    }

    @Override
    public void log(LogLevel level, String message, Throwable throwable) {
        if (!checkAccess(level)) return;
        getRealWriter().log(level, message, throwable);
    }

    @Override
    public void log(LogLevel level, Throwable throwable) {
        if (!checkAccess(level)) return;
        getRealWriter().log(level, throwable);
    }

    @Override
    public void dispose() {
        if (realWriter != null) {
            realWriter.dispose();
        }
    }
}