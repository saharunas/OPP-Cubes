package ethanjones.cubes.side.server.observers;

import com.badlogic.gdx.math.Vector3;
import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.networking.NetworkingManager;
import ethanjones.cubes.networking.packets.PacketChat;
import ethanjones.cubes.networking.server.ClientIdentifier;
import ethanjones.cubes.world.reference.AreaReference;

/**
 * Pattern: Observer (Concrete Observer)
 * Responsibility: Chat notifications for player events
 */
public class ChatObserver implements PlayerStateObserver {
    
    private final ClientIdentifier client;
    private boolean notifyAreaChanges;
    
    public ChatObserver(ClientIdentifier client, boolean notifyAreaChanges) {
        this.client = client;
        this.notifyAreaChanges = notifyAreaChanges;
    }
    
    @Override
    public void onPositionChanged(String playerId, Vector3 oldPosition, Vector3 newPosition) {
        // Position changes are too frequent for chat notifications
    }
    
    @Override
    public void onAreaChanged(String playerId, AreaReference oldArea, AreaReference newArea) {
        if (notifyAreaChanges) {
            String message = client.getPlayer().username + " entered area [" + 
                           newArea.areaX + ", " + newArea.areaZ + "]";
            Log.debug("[ChatObserver] " + message);
            
            PacketChat packet = new PacketChat();
            packet.msg = message;
            NetworkingManager.sendPacketToAllClients(packet);
        }
    }
    
    @Override
    public void onPlayerDisconnected(String playerId) {
        String message = client.getPlayer().username + " left the game";
        Log.info("[ChatObserver] " + message);
        
        PacketChat packet = new PacketChat();
        packet.msg = message;
        NetworkingManager.sendPacketToAllClients(packet);
    }
    
    public void setNotifyAreaChanges(boolean notify) {
        this.notifyAreaChanges = notify;
    }
}
