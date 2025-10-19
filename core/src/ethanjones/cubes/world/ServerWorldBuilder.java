package ethanjones.cubes.world;

import ethanjones.cubes.side.common.Side;
import ethanjones.cubes.world.server.WorldServer;

public class ServerWorldBuilder extends WorldBuilder {

    public ServerWorldBuilder() {
        this.side = Side.Server;
    }

    @Override
    public World build() {
        return new WorldServer(this);
    }
}
