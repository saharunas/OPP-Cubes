package ethanjones.cubes.world.save;

import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.networking.server.ClientIdentifier;
import ethanjones.cubes.world.generator.smooth.Cave;
import ethanjones.cubes.world.reference.AreaReference;
import ethanjones.cubes.world.storage.Area;
import ethanjones.cubes.world.storage.AreaMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Pattern: Decorator (ConcreteDecorator - Level 3)
 * 
 * Adds performance monitoring to save operations.
 * Measures execution time for all read/write operations and tracks statistics.
 * 
 * This decorator provides:
 * - Execution time measurement for each operation
 * - Total operation counts
 * - Average execution times
 * - Identification of performance bottlenecks
 */
public class PerformanceSaveDecorator extends SaveDecorator {
    
    private final Map<String, OperationStats> operationStats;
    private final boolean logSlowOperations;
    private final long slowOperationThresholdMs;
    
    /**
     * Create a performance monitoring decorator
     * @param wrappedSave The save to wrap
     * @param logSlowOperations Whether to log operations that exceed threshold
     * @param slowOperationThresholdMs Threshold in milliseconds for slow operations
     */
    public PerformanceSaveDecorator(SaveInterface wrappedSave, boolean logSlowOperations, long slowOperationThresholdMs) {
        super(wrappedSave);
        this.operationStats = new HashMap<>();
        this.logSlowOperations = logSlowOperations;
        this.slowOperationThresholdMs = slowOperationThresholdMs;
    }
    
    /**
     * Create a performance monitoring decorator with default settings
     * Logs operations that take longer than 100ms
     * @param wrappedSave The save to wrap
     */
    public PerformanceSaveDecorator(SaveInterface wrappedSave) {
        this(wrappedSave, true, 100);
    }
    
    private static class OperationStats {
        long totalTime = 0;
        long count = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = 0;
        
        void record(long time) {
            totalTime += time;
            count++;
            minTime = Math.min(minTime, time);
            maxTime = Math.max(maxTime, time);
        }
        
        double getAverageTime() {
            return count > 0 ? (double) totalTime / count : 0;
        }
    }
    
    private void recordOperation(String operationName, long durationMs) {
        synchronized (operationStats) {
            OperationStats stats = operationStats.get(operationName);
            if (stats == null) {
                stats = new OperationStats();
                operationStats.put(operationName, stats);
            }
            stats.record(durationMs);
        }
        
        if (logSlowOperations && durationMs > slowOperationThresholdMs) {
            Log.warning(String.format("Slow operation detected: %s took %d ms", operationName, durationMs));
        }
    }
    
    @Override
    public boolean writeArea(Area area) {
        long startTime = System.currentTimeMillis();
        boolean result = super.writeArea(area);
        long duration = System.currentTimeMillis() - startTime;
        recordOperation("writeArea(" + area.areaX + "," + area.areaZ + ")", duration);
        return result;
    }
    
    @Override
    public void writeAreas(AreaMap areas) {
        long startTime = System.currentTimeMillis();
        super.writeAreas(areas);
        long duration = System.currentTimeMillis() - startTime;
        recordOperation("writeAreas(" + areas.getSize() + " areas)", duration);
    }
    
    @Override
    public Area readArea(int x, int z) {
        long startTime = System.currentTimeMillis();
        Area result = super.readArea(x, z);
        long duration = System.currentTimeMillis() - startTime;
        recordOperation("readArea(" + x + "," + z + ")", duration);
        return result;
    }
    
    @Override
    public void writePlayer(Player player) {
        long startTime = System.currentTimeMillis();
        super.writePlayer(player);
        long duration = System.currentTimeMillis() - startTime;
        recordOperation("writePlayer(" + player.username + ")", duration);
    }
    
    @Override
    public void writePlayers() {
        long startTime = System.currentTimeMillis();
        super.writePlayers();
        long duration = System.currentTimeMillis() - startTime;
        recordOperation("writePlayers", duration);
    }
    
    @Override
    public Player readPlayer(UUID uuid, ClientIdentifier clientIdentifier) {
        long startTime = System.currentTimeMillis();
        Player result = super.readPlayer(uuid, clientIdentifier);
        long duration = System.currentTimeMillis() - startTime;
        recordOperation("readPlayer(" + uuid + ")", duration);
        return result;
    }
    
    @Override
    public void writeCave(AreaReference areaReference, Cave cave) {
        long startTime = System.currentTimeMillis();
        super.writeCave(areaReference, cave);
        long duration = System.currentTimeMillis() - startTime;
        recordOperation("writeCave(" + areaReference.areaX + "," + areaReference.areaZ + ")", duration);
    }
    
    @Override
    public Cave readCave(AreaReference areaReference) {
        long startTime = System.currentTimeMillis();
        Cave result = super.readCave(areaReference);
        long duration = System.currentTimeMillis() - startTime;
        recordOperation("readCave(" + areaReference.areaX + "," + areaReference.areaZ + ")", duration);
        return result;
    }
    
    @Override
    public SaveOptions writeSaveOptions() {
        long startTime = System.currentTimeMillis();
        SaveOptions result = super.writeSaveOptions();
        long duration = System.currentTimeMillis() - startTime;
        recordOperation("writeSaveOptions", duration);
        return result;
    }
    
    /**
     * Log performance statistics to console
     */
    public void logPerformanceStats() {
        synchronized (operationStats) {
            Log.info("=== Save Performance Statistics ===");
            for (Map.Entry<String, OperationStats> entry : operationStats.entrySet()) {
                OperationStats stats = entry.getValue();
                Log.info(String.format("%s: count=%d, avg=%.2fms, min=%dms, max=%dms, total=%dms",
                        entry.getKey(),
                        stats.count,
                        stats.getAverageTime(),
                        stats.minTime,
                        stats.maxTime,
                        stats.totalTime));
            }
        }
    }
    
    /**
     * Get performance statistics as a map
     * @return Map of operation names to their statistics
     */
    public Map<String, String> getPerformanceStats() {
        Map<String, String> stats = new HashMap<>();
        synchronized (operationStats) {
            for (Map.Entry<String, OperationStats> entry : operationStats.entrySet()) {
                OperationStats opStats = entry.getValue();
                stats.put(entry.getKey(), String.format(
                        "count=%d, avg=%.2fms, min=%dms, max=%dms",
                        opStats.count,
                        opStats.getAverageTime(),
                        opStats.minTime,
                        opStats.maxTime));
            }
        }
        return stats;
    }
    
    /**
     * Reset all performance statistics
     */
    public void resetStats() {
        synchronized (operationStats) {
            operationStats.clear();
        }
    }
}
