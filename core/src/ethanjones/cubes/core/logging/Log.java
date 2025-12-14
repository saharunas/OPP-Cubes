package ethanjones.cubes.core.logging;

import ethanjones.cubes.core.logging.loggers.FileLogWriter;
import ethanjones.cubes.core.logging.loggers.SysOutLogWriter;
import ethanjones.cubes.core.lua.convert.LuaExclude;
import ethanjones.cubes.core.platform.Compatibility;

import java.io.File;

public class Log {

  private static LogWriter output;
  private static LogWriter file;

  static {
    try {
      if (Compatibility.get() != null) {
        LogWriter customLogWriter = Compatibility.get().getCustomLogWriter();

        if (customLogWriter != null) {
          final LogWriter custom = customLogWriter;
          output = new LogWriterProxy(
                  new LogWriterFactory() {
                    @Override
                    public LogWriter create() {
                      return custom;
                    }
                  },
                  LogLevel.info
          );
        } else {
          output = new LogWriterProxy(
                  new LogWriterFactory() {
                    @Override
                    public LogWriter create() {
                      return new SysOutLogWriter();
                    }
                  },
                  LogLevel.info
          );
        }
      } else {
        output = new LogWriterProxy(
                new LogWriterFactory() {
                  @Override
                  public LogWriter create() {
                    return new SysOutLogWriter();
                  }
                },
                LogLevel.info
        );
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      file = new LogWriterProxy(
              new LogWriterFactory() {
                @Override
                public LogWriter create() {
                  return new FileLogWriter(
                          new File(Compatibility.get().getBaseFolder().file(), "log.txt")
                  );
                }
              },
              LogLevel.debug
      );
    } catch (Exception e) {
      e.printStackTrace();
      try {
        file = new LogWriterProxy(
                new LogWriterFactory() {
                  @Override
                  public LogWriter create() {
                    return new FileLogWriter(
                            new File(System.getProperty("user.dir"), "log.txt")
                    );
                  }
                },
                LogLevel.debug
        );
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  // ERROR
  public static void error(String message) {
    log(LogLevel.error, message);
  }

  public static void log(LogLevel level, String message) {
    try {
      synchronized (output) {
        output.log(level, message);
      }
    } catch (Exception e) {
    }
    try {
      synchronized (file) {
        file.log(level, message);
      }
    } catch (Exception e) {
    }
  }

  public static void error(String message, Throwable throwable) {
    log(LogLevel.error, message, throwable);
  }

  public static void log(LogLevel level, String message, Throwable throwable) {
    try {
      synchronized (output) {
        output.log(level, message, throwable);
      }
    } catch (Exception e) {
    }
    try {
      synchronized (file) {
        file.log(level, message, throwable);
      }
    } catch (Exception e) {
    }
  }

  public static void error(Throwable throwable) {
    log(LogLevel.error, throwable);
  }

  public static void log(LogLevel level, Throwable throwable) {
    try {
      synchronized (output) {
        output.log(level, throwable);
      }
    } catch (Exception e) {
    }
    try {
      synchronized (file) {
        file.log(level, throwable);
      }
    } catch (Exception e) {
    }
  }

  // WARNING
  public static void warning(String message) {
    log(LogLevel.warning, message);
  }

  public static void warning(String message, Throwable throwable) {
    log(LogLevel.warning, message, throwable);
  }

  public static void warning(Throwable throwable) {
    log(LogLevel.warning, throwable);
  }

  // INFO
  public static void info(String message) {
    log(LogLevel.info, message);
  }

  public static void info(String message, Throwable throwable) {
    log(LogLevel.info, message, throwable);
  }

  public static void info(Throwable throwable) {
    log(LogLevel.info, "", throwable);
  }

  // DEBUG
  public static void debug(String message) {
    log(LogLevel.debug, message);
  }

  public static void debug(String message, Throwable throwable) {
    log(LogLevel.debug, message, throwable);
  }

  public static void debug(Throwable throwable) {
    log(LogLevel.debug, throwable);
  }

  @LuaExclude
  public static void dispose() {
    try {
      output.dispose();
    } catch (Exception e) {
    }
    try {
      file.dispose();
    } catch (Exception e) {
    }
  }
}