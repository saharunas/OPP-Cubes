package ethanjones.cubes.world.save;

import com.badlogic.gdx.files.FileHandle;

/**
 * Combinations:
 * 
 * 1. All three decorators (development):
 *    Performance -> Caching -> Logging -> BasicSave
 *    Provides full observability with optimal performance
 * 
 * 2. Performance + Caching (production):
 *    Performance -> Caching -> BasicSave
 *    Optimal performance without debug logging overhead
 * 
 * 3. Just Logging (debugging):
 *    Logging -> BasicSave
 *    Maximum visibility for troubleshooting
 * 
 * 4. Just Caching (simple optimization):
 *    Caching -> BasicSave
 *    Performance improvement without monitoring overhead
 */
public class SaveDecoratorExample {
    
    /**
     * Create a fully decorated save with all three levels
     * @param name Save name
     * @param fileHandle File handle for save location
     * @return Fully decorated SaveInterface with performance monitoring, caching, and logging
     */
    public static SaveInterface createFullyDecoratedSave(String name, FileHandle fileHandle) {
        // Start with the basic component
        SaveInterface save = new BasicSave(name, fileHandle);
        
        // Add Level 1: Logging (innermost decorator)
        save = new LoggingSaveDecorator(save);
        
        // Add Level 2: Caching (middle decorator)
        save = new CachingSaveDecorator(save, 50); // Cache up to 50 items
        
        // Add Level 3: Performance monitoring (outermost decorator)
        save = new PerformanceSaveDecorator(save, true, 100); // Log operations > 100ms
        
        return save;
    }
    
    /**
     * Create a production-optimized save (caching + performance, no logging)
     * @param name Save name
     * @param fileHandle File handle for save location
     * @return SaveInterface with performance monitoring and caching
     */
    public static SaveInterface createProductionSave(String name, FileHandle fileHandle) {
        SaveInterface save = new BasicSave(name, fileHandle);
        save = new CachingSaveDecorator(save, 100); // Larger cache for production
        save = new PerformanceSaveDecorator(save, true, 200); // Only log very slow operations
        return save;
    }
    
    /**
     * Create a debug-oriented save (logging only)
     * @param name Save name
     * @param fileHandle File handle for save location
     * @return SaveInterface with detailed logging
     */
    public static SaveInterface createDebugSave(String name, FileHandle fileHandle) {
        SaveInterface save = new BasicSave(name, fileHandle);
        save = new LoggingSaveDecorator(save);
        return save;
    }
    
    /**
     * Create a simple cached save (caching only)
     * @param name Save name
     * @param fileHandle File handle for save location
     * @return SaveInterface with caching
     */
    public static SaveInterface createCachedSave(String name, FileHandle fileHandle) {
        SaveInterface save = new BasicSave(name, fileHandle);
        save = new CachingSaveDecorator(save);
        return save;
    }
    
    /**
     * Create a basic save without any decorators
     * @param name Save name
     * @param fileHandle File handle for save location
     * @return Basic SaveInterface without any decorations
     */
    public static SaveInterface createBasicSave(String name, FileHandle fileHandle) {
        return new BasicSave(name, fileHandle);
    }
    
    /**
     * Demonstrate dynamic decorator composition at runtime
     * This shows how decorators can be added/removed based on configuration
     */
    public static SaveInterface createConfigurableSave(String name, FileHandle fileHandle,
                                                       boolean enableLogging,
                                                       boolean enableCaching,
                                                       boolean enablePerformanceMonitoring) {
        SaveInterface save = new BasicSave(name, fileHandle);
        
        // Dynamically add decorators based on configuration
        if (enableLogging) {
            save = new LoggingSaveDecorator(save);
        }
        
        if (enableCaching) {
            save = new CachingSaveDecorator(save);
        }
        
        if (enablePerformanceMonitoring) {
            save = new PerformanceSaveDecorator(save);
        }
        
        return save;
    }
    
    /**
     * Example usage demonstrating the pattern
     */
    public static void demonstrateUsage(FileHandle fileHandle) {
        // Example 1: Full decoration (all 3 levels)
        SaveInterface fullSave = createFullyDecoratedSave("test_world", fileHandle);
        
        // All operations go through:
        // 1. PerformanceSaveDecorator (measures time)
        // 2. CachingSaveDecorator (checks cache)
        // 3. LoggingSaveDecorator (logs operation)
        // 4. BasicSave (actual save operation)
        
        // Example 2: Different decoration for different scenarios
        SaveInterface devSave = createFullyDecoratedSave("dev_world", fileHandle);
        SaveInterface prodSave = createProductionSave("prod_world", fileHandle);
        SaveInterface debugSave = createDebugSave("debug_world", fileHandle);
        
        // Each save has different behavior without changing the client code!
        // This is the power of the Decorator pattern.
    }
}
