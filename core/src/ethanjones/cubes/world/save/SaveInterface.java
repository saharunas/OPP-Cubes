package ethanjones.cubes.world.save;

import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.networking.server.ClientIdentifier;
import ethanjones.cubes.world.generator.smooth.Cave;
import ethanjones.cubes.world.reference.AreaReference;
import ethanjones.cubes.world.storage.Area;
import ethanjones.cubes.world.storage.AreaMap;

import com.badlogic.gdx.files.FileHandle;

import java.util.UUID;

/**
 * Pattern: Decorator (Component Interface)
 * 
 * Defines the interface for save operations that can be decorated
 * with additional responsibilities like logging, caching, or performance monitoring.
 * 
 * This interface allows decorators to be stacked in any order to add
 * different combinations of functionality without modifying the base Save class.
 */
public interface SaveInterface {
    
    /**
     * Write an area to storage
     * @param area The area to save
     * @return true if successful
     */
    boolean writeArea(Area area);
    
    /**
     * Write multiple areas to storage
     * @param areas The areas to save
     */
    void writeAreas(AreaMap areas);
    
    /**
     * Read an area from storage
     * @param x Area X coordinate
     * @param z Area Z coordinate
     * @return The loaded area, or null if not found
     */
    Area readArea(int x, int z);
    
    /**
     * Write a player to storage
     * @param player The player to save
     */
    void writePlayer(Player player);
    
    /**
     * Write all connected players to storage
     */
    void writePlayers();
    
    /**
     * Read a player from storage
     * @param uuid Player's UUID
     * @param clientIdentifier The client identifier
     * @return The loaded player, or null if not found
     */
    Player readPlayer(UUID uuid, ClientIdentifier clientIdentifier);
    
    /**
     * Write cave data to storage
     * @param areaReference The area reference
     * @param cave The cave data
     */
    void writeCave(AreaReference areaReference, Cave cave);
    
    /**
     * Read cave data from storage
     * @param areaReference The area reference
     * @return The loaded cave data, or null if not found
     */
    Cave readCave(AreaReference areaReference);
    
    /**
     * Get save options
     * @return The save options
     */
    SaveOptions getSaveOptions();
    
    /**
     * Write save options to storage
     * @return The save options
     */
    SaveOptions writeSaveOptions();
    
    /**
     * Get the save name
     * @return The name of this save
     */
    String getName();
    
    /**
     * Get the file handle
     * @return The file handle for this save
     */
    FileHandle getFileHandle();
    
    /**
     * Check if this save is read-only
     * @return true if read-only
     */
    boolean isReadOnly();
    
    /**
     * Get the area folder
     * @return FileHandle for area folder
     */
    FileHandle folderArea();
    
    /**
     * Get the cave folder
     * @return FileHandle for cave folder
     */
    FileHandle folderCave();
    
    /**
     * Get the player folder
     * @return FileHandle for player folder
     */
    FileHandle folderPlayer();
}
