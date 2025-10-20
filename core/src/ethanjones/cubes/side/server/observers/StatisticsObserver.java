package ethanjones.cubes.side.server.observers;

import com.badlogic.gdx.math.Vector3;
import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.networking.server.ClientIdentifier;
import ethanjones.cubes.world.reference.AreaReference;

/**
 * Pattern: Observer (Concrete Observer)
 * Responsibility: Player statistics and analytics
 */
public class StatisticsObserver implements PlayerStateObserver {
    
    private final ClientIdentifier client;
    private long totalMovementCount = 0;
    private double totalDistanceTraveled = 0.0;
    private long areaChangeCount = 0;
    private Vector3 lastPosition;
    
    public StatisticsObserver(ClientIdentifier client) {
        this.client = client;
        this.lastPosition = client.getPlayer().position.cpy();
    }
    
    @Override
    public void onPositionChanged(String playerId, Vector3 oldPosition, Vector3 newPosition) {
        totalMovementCount++;
        
        if (oldPosition != null && lastPosition != null) {
            double distance = oldPosition.dst(newPosition);
            totalDistanceTraveled += distance;
            
            if (totalMovementCount % 100 == 0) { // Log every 100 movements
                Log.debug("[StatisticsObserver] Player " + client.getPlayer().username + 
                          " - Total movements: " + totalMovementCount + 
                          ", Distance traveled: " + String.format("%.2f", totalDistanceTraveled) + " blocks");
            }
        }
        
        lastPosition = newPosition.cpy();
    }
    
    @Override
    public void onAreaChanged(String playerId, AreaReference oldArea, AreaReference newArea) {
        areaChangeCount++;
        Log.debug("[StatisticsObserver] Player " + client.getPlayer().username + 
                  " area transitions: " + areaChangeCount);
    }
    
    @Override
    public void onPlayerDisconnected(String playerId) {
        // Print final statistics
        Log.info("[StatisticsObserver] Final statistics for player " + client.getPlayer().username);
        Log.info("  Total movements: " + totalMovementCount);
        Log.info("  Distance traveled: " + String.format("%.2f", totalDistanceTraveled) + " blocks");
        Log.info("  Area transitions: " + areaChangeCount);
    }
    
    // Getters for testing/debugging
    public long getTotalMovementCount() { return totalMovementCount; }
    public double getTotalDistanceTraveled() { return totalDistanceTraveled; }
    public long getAreaChangeCount() { return areaChangeCount; }
}
