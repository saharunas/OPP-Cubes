package ethanjones.cubes.core.logging;

import ethanjones.cubes.core.logging.loggers.FileLogWriter;
import ethanjones.cubes.core.logging.loggers.SysOutLogWriter;
import ethanjones.cubes.core.lua.convert.LuaExclude;
import ethanjones.cubes.core.platform.Adapter;
import ethanjones.cubes.core.platform.Compatibility;

import java.io.File;

public class Log {

  private static LogWriter output;
  private static LogWriter file;

  // Safe defaults so logging still works before the factory is set
  static {
    try {
      output = new SysOutLogWriter();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      // Try base folder (if Compatibility is ready), else user.dir
      File target;
      try {
        if (Compatibility.get() != null && Compatibility.get().getBaseFolder() != null) {
          target = new File(Compatibility.get().getBaseFolder().file(), "log.txt");
        } else {
          target = new File(System.getProperty("user.dir"), "log.txt");
        }
      } catch (Exception ignored) {
        target = new File(System.getProperty("user.dir"), "log.txt");
      }
      file = new FileLogWriter(target);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** Call this right after Adapter.setFactory(...) in your launcher. */
  public static void initFromFactory() {
    try {
      LogWriter factoryWriter = Adapter.getFactory().createLogWriter();
      if (factoryWriter != null) output = factoryWriter;
    } catch (Exception e) {
      // Fall back to SysOut if anything goes wrong
      e.printStackTrace();
    }

    // Optional: recreate the file writer if base folder becomes available later
    try {
      if (Compatibility.get() != null && Compatibility.get().getBaseFolder() != null) {
        file = new FileLogWriter(new File(Compatibility.get().getBaseFolder().file(), "log.txt"));
      }
    } catch (Exception ignored) { }
  }

  //ERROR
  public static void error(String message) { log(LogLevel.error, message); }

  public static void log(LogLevel level, String message) {
    try { synchronized (output) { output.log(level, message); } } catch (Exception ignored) { }
    try { synchronized (file)   { file.log(level, message);   } } catch (Exception ignored) { }
  }

  public static void error(String message, Throwable throwable) { log(LogLevel.error, message, throwable); }

  public static void log(LogLevel level, String message, Throwable throwable) {
    try { synchronized (output) { output.log(level, message, throwable); } } catch (Exception ignored) { }
    try { synchronized (file)   { file.log(level, message, throwable);   } } catch (Exception ignored) { }
  }

  public static void error(Throwable throwable) { log(LogLevel.error, throwable); }

  public static void log(LogLevel level, Throwable throwable) {
    try { synchronized (output) { output.log(level, throwable); } } catch (Exception ignored) { }
    try { synchronized (file)   { file.log(level, throwable);   } } catch (Exception ignored) { }
  }

  //WARNING
  public static void warning(String message) { log(LogLevel.warning, message); }
  public static void warning(String message, Throwable throwable) { log(LogLevel.warning, message, throwable); }
  public static void warning(Throwable throwable) { log(LogLevel.warning, throwable); }

  //INFO
  public static void info(String message) { log(LogLevel.info, message); }
  public static void info(String message, Throwable throwable) { log(LogLevel.info, message, throwable); }
  public static void info(Throwable throwable) { log(LogLevel.info, "", throwable); }

  //DEBUG
  public static void debug(String message) { log(LogLevel.debug, message); }
  public static void debug(String message, Throwable throwable) { log(LogLevel.debug, message, throwable); }
  public static void debug(Throwable throwable) { log(LogLevel.debug, throwable); }

  @LuaExclude
  public static void dispose() {
    try { if (output != null) output.dispose(); } catch (Exception ignored) { }
    try { if (file != null) file.dispose(); } catch (Exception ignored) { }
  }
}
