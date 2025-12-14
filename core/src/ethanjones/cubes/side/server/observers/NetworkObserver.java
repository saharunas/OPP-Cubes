package ethanjones.cubes.side.server.observers;

import com.badlogic.gdx.math.Vector3;
import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.networking.NetworkingManager;
import ethanjones.cubes.networking.packets.PacketOtherPlayerMovement;
import ethanjones.cubes.networking.server.ClientIdentifier;
import ethanjones.cubes.side.common.Cubes;
import ethanjones.cubes.world.reference.AreaReference;

/**
 * Pattern: Observer (Concrete Observer)
 * Responsibility: Network communication to other clients
 */
public class NetworkObserver implements PlayerStateObserver {
    
    private final ClientIdentifier client;
    
    public NetworkObserver(ClientIdentifier client) {
        this.client = client;
    }
    
    @Override
    public void onPositionChanged(String playerId, Vector3 oldPosition, Vector3 newPosition) {
        // Broadcast position change to other clients
        Log.debug("[NetworkObserver] Player " + client.getPlayer().username + 
                  " moved from " + oldPosition + " to " + newPosition);
        
        PacketOtherPlayerMovement packet = new PacketOtherPlayerMovement(client.getPlayer());
        NetworkingManager.sendPacketToOtherClients(packet, client);
    }
    
    @Override
    public void onAreaChanged(String playerId, AreaReference oldArea, AreaReference newArea) {
        // Area changes are already handled by position changes
        Log.debug("[NetworkObserver] Player " + client.getPlayer().username + 
                  " changed area from " + oldArea + " to " + newArea);
    }
    
    @Override
    public void onPlayerDisconnected(String playerId) {
        Log.debug("[NetworkObserver] Broadcasting disconnect for player " + 
                  client.getPlayer().username);
        // Disconnect notification is handled in PlayerManager.disconnected()
    }
}
