package ethanjones.cubes.world.save;

import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.networking.server.ClientIdentifier;
import ethanjones.cubes.world.generator.smooth.Cave;
import ethanjones.cubes.world.reference.AreaReference;
import ethanjones.cubes.world.storage.Area;
import ethanjones.cubes.world.storage.AreaMap;

import java.util.UUID;

/**
 * Pattern: Decorator (ConcreteDecorator - Level 1)
 * 
 * Adds logging functionality to save operations.
 * Logs all read and write operations with relevant parameters.
 * 
 * This decorator is useful for:
 * - Debugging save/load issues
 * - Tracking which areas are being accessed
 * - Monitoring save operation frequency
 * - Diagnosing corruption or missing data
 */
public class LoggingSaveDecorator extends SaveDecorator {
    
    public LoggingSaveDecorator(SaveInterface wrappedSave) {
        super(wrappedSave);
    }
    
    @Override
    public boolean writeArea(Area area) {
        Log.debug("Writing area at (" + area.areaX + ", " + area.areaZ + ")");
        boolean result = super.writeArea(area);
        if (result) {
            Log.debug("Successfully wrote area at (" + area.areaX + ", " + area.areaZ + ")");
        } else {
            Log.warning("Failed to write area at (" + area.areaX + ", " + area.areaZ + ")");
        }
        return result;
    }
    
    @Override
    public void writeAreas(AreaMap areas) {
        Log.debug("Writing " + areas.getSize() + " areas");
        super.writeAreas(areas);
        Log.debug("Finished writing areas");
    }
    
    @Override
    public Area readArea(int x, int z) {
        Log.debug("Reading area at (" + x + ", " + z + ")");
        Area result = super.readArea(x, z);
        if (result != null) {
            Log.debug("Successfully read area at (" + x + ", " + z + ")");
        } else {
            Log.debug("Area not found at (" + x + ", " + z + ")");
        }
        return result;
    }
    
    @Override
    public void writePlayer(Player player) {
        Log.debug("Writing player: " + player.username + " (UUID: " + player.uuid + ")");
        super.writePlayer(player);
        Log.debug("Finished writing player: " + player.username);
    }
    
    @Override
    public void writePlayers() {
        Log.debug("Writing all players");
        super.writePlayers();
        Log.debug("Finished writing all players");
    }
    
    @Override
    public Player readPlayer(UUID uuid, ClientIdentifier clientIdentifier) {
        Log.debug("Reading player with UUID: " + uuid);
        Player result = super.readPlayer(uuid, clientIdentifier);
        if (result != null) {
            Log.debug("Successfully read player: " + result.username + " (UUID: " + uuid + ")");
        } else {
            Log.debug("Player not found with UUID: " + uuid);
        }
        return result;
    }
    
    @Override
    public void writeCave(AreaReference areaReference, Cave cave) {
        Log.debug("Writing cave at area (" + areaReference.areaX + ", " + areaReference.areaZ + ")");
        super.writeCave(areaReference, cave);
        Log.debug("Finished writing cave");
    }
    
    @Override
    public Cave readCave(AreaReference areaReference) {
        Log.debug("Reading cave at area (" + areaReference.areaX + ", " + areaReference.areaZ + ")");
        Cave result = super.readCave(areaReference);
        if (result != null) {
            Log.debug("Successfully read cave at area (" + areaReference.areaX + ", " + areaReference.areaZ + ")");
        } else {
            Log.debug("Cave not found at area (" + areaReference.areaX + ", " + areaReference.areaZ + ")");
        }
        return result;
    }
    
    @Override
    public SaveOptions writeSaveOptions() {
        Log.debug("Writing save options for: " + getName());
        SaveOptions result = super.writeSaveOptions();
        Log.debug("Finished writing save options");
        return result;
    }
}
