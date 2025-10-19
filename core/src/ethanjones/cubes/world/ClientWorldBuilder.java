package ethanjones.cubes.world;

import ethanjones.cubes.side.common.Side;
import ethanjones.cubes.world.client.WorldClient;

public class ClientWorldBuilder extends WorldBuilder {

    public ClientWorldBuilder() {
        this.side = Side.Client;
    }

    @Override
    public World build() {
        return new WorldClient(this);
    }
}
