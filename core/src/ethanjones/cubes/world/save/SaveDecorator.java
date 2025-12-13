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
 * Pattern: Decorator (Abstract Decorator)
 * 
 * Abstract base class for all save decorators.
 * Maintains a reference to a SaveInterface component and delegates all
 * operations to it by default.
 * 
 * Concrete decorators extend this class and override specific methods
 * to add their functionality before/after delegating to the wrapped component.
 * 
 * This enables stacking multiple decorators:
 * Performance(Caching(Logging(BasicSave)))
 */
public abstract class SaveDecorator implements SaveInterface {
    
    protected final SaveInterface wrappedSave;
    
    public SaveDecorator(SaveInterface wrappedSave) {
        this.wrappedSave = wrappedSave;
    }
    
    @Override
    public boolean writeArea(Area area) {
        return wrappedSave.writeArea(area);
    }
    
    @Override
    public void writeAreas(AreaMap areas) {
        wrappedSave.writeAreas(areas);
    }
    
    @Override
    public Area readArea(int x, int z) {
        return wrappedSave.readArea(x, z);
    }
    
    @Override
    public void writePlayer(Player player) {
        wrappedSave.writePlayer(player);
    }
    
    @Override
    public void writePlayers() {
        wrappedSave.writePlayers();
    }
    
    @Override
    public Player readPlayer(UUID uuid, ClientIdentifier clientIdentifier) {
        return wrappedSave.readPlayer(uuid, clientIdentifier);
    }
    
    @Override
    public void writeCave(AreaReference areaReference, Cave cave) {
        wrappedSave.writeCave(areaReference, cave);
    }
    
    @Override
    public Cave readCave(AreaReference areaReference) {
        return wrappedSave.readCave(areaReference);
    }
    
    @Override
    public SaveOptions getSaveOptions() {
        return wrappedSave.getSaveOptions();
    }
    
    @Override
    public SaveOptions writeSaveOptions() {
        return wrappedSave.writeSaveOptions();
    }
    
    @Override
    public String getName() {
        return wrappedSave.getName();
    }
    
    @Override
    public FileHandle getFileHandle() {
        return wrappedSave.getFileHandle();
    }
    
    @Override
    public boolean isReadOnly() {
        return wrappedSave.isReadOnly();
    }
    
    @Override
    public FileHandle folderArea() {
        return wrappedSave.folderArea();
    }
    
    @Override
    public FileHandle folderCave() {
        return wrappedSave.folderCave();
    }
    
    @Override
    public FileHandle folderPlayer() {
        return wrappedSave.folderPlayer();
    }
}
