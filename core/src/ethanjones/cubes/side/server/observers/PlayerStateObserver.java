package ethanjones.cubes.side.server.observers;

import com.badlogic.gdx.math.Vector3;
import ethanjones.cubes.world.reference.AreaReference;

/**
 * Pattern: Observer (Subject-Observer)
 * Purpose: Allows multiple components to react to player state changes without tight coupling
 */
public interface PlayerStateObserver {
    
    /**
     * Called when the player's position changes.
     * 
     * @param playerId The UUID of the player
     * @param oldPosition Previous position (may be null on initial setup)
     * @param newPosition New position
     */
    void onPositionChanged(String playerId, Vector3 oldPosition, Vector3 newPosition);
    
    /**
     * Called when the player moves to a different area.
     * 
     * @param playerId The UUID of the player
     * @param oldArea Previous area reference (may be null on initial setup)
     * @param newArea New area reference
     */
    void onAreaChanged(String playerId, AreaReference oldArea, AreaReference newArea);
    
    /**
     * Called when the player disconnects from the server.
     * 
     * @param playerId The UUID of the player
     */
    void onPlayerDisconnected(String playerId);
}
