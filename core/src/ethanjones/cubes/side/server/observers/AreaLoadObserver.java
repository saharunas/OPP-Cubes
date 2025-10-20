package ethanjones.cubes.side.server.observers;

import com.badlogic.gdx.math.Vector3;
import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.networking.NetworkingManager;
import ethanjones.cubes.networking.packets.PacketEntityAdd;
import ethanjones.cubes.networking.server.ClientIdentifier;
import ethanjones.cubes.side.common.Cubes;
import ethanjones.cubes.world.reference.AreaReference;
import ethanjones.cubes.world.reference.multi.AreaReferenceSet;
import ethanjones.cubes.world.reference.multi.WorldRegion;
import ethanjones.cubes.world.storage.Area;
import ethanjones.cubes.entity.Entity;
import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.core.util.locks.Locked;
import ethanjones.cubes.world.World;
import ethanjones.cubes.world.thread.WorldLockable;

/**
 * Pattern: Observer (Concrete Observer)
 * Responsibility: World area management and loading
 */
public class AreaLoadObserver implements PlayerStateObserver {
    
    private final ClientIdentifier client;
    private final int loadDistance;
    private AreaReference currentArea;
    
    public AreaLoadObserver(ClientIdentifier client, int loadDistance) {
        this.client = client;
        this.loadDistance = loadDistance;
        this.currentArea = new AreaReference().setFromPositionVector3(client.getPlayer().position);
    }
    
    @Override
    public void onPositionChanged(String playerId, Vector3 oldPosition, Vector3 newPosition) {
        // Check if player moved to a different area
        AreaReference newRef = new AreaReference().setFromPositionVector3(newPosition);
        
        if (!newRef.equals(currentArea)) {
            onAreaChanged(playerId, currentArea, newRef);
            currentArea.setFromAreaReference(newRef);
        }
    }
    
    @Override
    public void onAreaChanged(String playerId, AreaReference oldArea, AreaReference newArea) {
        Log.debug("[AreaLoadObserver] Player " + client.getPlayer().username + 
                  " moved from area " + oldArea + " to " + newArea);
        
        // Calculate which new areas need to be loaded
        WorldRegion newRegion = new WorldRegion(newArea, loadDistance);
        WorldRegion oldRegion = new WorldRegion(oldArea, loadDistance);
        AreaReferenceSet difference = new AreaReferenceSet();
        difference.addAll(newRegion.getAreaReferences());
        difference.removeAll(oldRegion.getAreaReferences());
        
        // Request loading of new areas
        if (!difference.isEmpty()) {
            Log.debug("[AreaLoadObserver] Loading " + difference.size() + " new areas");
            Cubes.getServer().world.requestRegion(difference, null);
            
            // Send newly visible entities to client
            World world = Cubes.getServer().world;
            try (Locked<WorldLockable> locked = world.entities.acquireReadLock()) {
                for (Entity entity : world.entities.map.values()) {
                    if (!(entity instanceof Player) && 
                        newRegion.contains(entity.position) && 
                        !oldRegion.contains(entity.position)) {
                        PacketEntityAdd packet = new PacketEntityAdd();
                        packet.entity = entity;
                        NetworkingManager.sendPacketToClient(packet, client);
                    }
                }
            }
        }
    }
    
    @Override
    public void onPlayerDisconnected(String playerId) {
        Log.debug("[AreaLoadObserver] Cleaning up areas for disconnected player " + 
                  client.getPlayer().username);
        // Areas will be unloaded by the world manager
    }
}
