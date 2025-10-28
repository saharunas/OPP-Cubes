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
 * Pattern: Decorator (ConcreteComponent)
 * 
 * BasicSave wraps the original Save class and implements SaveInterface.
 * This is the concrete component that decorators will wrap to add
 * additional functionality like logging, caching, or performance monitoring.
 * 
 * All operations are delegated to the underlying Save instance.
 */
public class BasicSave implements SaveInterface {
    
    private final Save save;
    
    public BasicSave(Save save) {
        this.save = save;
    }
    
    public BasicSave(String name, FileHandle fileHandle) {
        this(new Save(name, fileHandle));
    }
    
    public BasicSave(String name, FileHandle fileHandle, boolean readOnly) {
        this(new Save(name, fileHandle, readOnly));
    }
    
    @Override
    public boolean writeArea(Area area) {
        return save.writeArea(area);
    }
    
    @Override
    public void writeAreas(AreaMap areas) {
        save.writeAreas(areas);
    }
    
    @Override
    public Area readArea(int x, int z) {
        return save.readArea(x, z);
    }
    
    @Override
    public void writePlayer(Player player) {
        save.writePlayer(player);
    }
    
    @Override
    public void writePlayers() {
        save.writePlayers();
    }
    
    @Override
    public Player readPlayer(UUID uuid, ClientIdentifier clientIdentifier) {
        return save.readPlayer(uuid, clientIdentifier);
    }
    
    @Override
    public void writeCave(AreaReference areaReference, Cave cave) {
        save.writeCave(areaReference, cave);
    }
    
    @Override
    public Cave readCave(AreaReference areaReference) {
        return save.readCave(areaReference);
    }
    
    @Override
    public SaveOptions getSaveOptions() {
        return save.getSaveOptions();
    }
    
    @Override
    public SaveOptions writeSaveOptions() {
        return save.writeSaveOptions();
    }
    
    @Override
    public String getName() {
        return save.name;
    }
    
    @Override
    public FileHandle getFileHandle() {
        return save.fileHandle;
    }
    
    @Override
    public boolean isReadOnly() {
        return save.readOnly;
    }
    
    @Override
    public FileHandle folderArea() {
        return save.folderArea();
    }
    
    @Override
    public FileHandle folderCave() {
        return save.folderCave();
    }
    
    @Override
    public FileHandle folderPlayer() {
        return save.folderPlayer();
    }
    
    /**
     * Get the underlying Save instance
     * @return The wrapped Save object
     */
    public Save getWrappedSave() {
        return save;
    }
}
