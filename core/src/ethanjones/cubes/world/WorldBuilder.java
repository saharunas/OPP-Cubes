package ethanjones.cubes.world;

import ethanjones.cubes.side.common.Side;
import ethanjones.cubes.world.generator.TerrainGenerator;
import ethanjones.cubes.world.save.Save;
import org.luaj.vm2.LuaTable;

public abstract class WorldBuilder {

    public Save save;
    public Side side;
    protected TerrainGenerator terrainGenerator;
    protected int time;
    protected long playingTime;
    protected LuaTable lua;

    public WorldBuilder setSave(Save save) {
        this.save = save;
        return this;
    }

    public WorldBuilder setSide(Side side) {
        this.side = side;
        return this;
    }

    public WorldBuilder setTerrainGenerator(TerrainGenerator terrainGenerator) {
        this.terrainGenerator = terrainGenerator;
        return this;
    }

    public WorldBuilder setTime(int time) {
        this.time = time;
        return this;
    }

    public WorldBuilder setPlayingTime(long playingTime) {
        this.playingTime = playingTime;
        return this;
    }

    public WorldBuilder setLua(LuaTable lua) {
        this.lua = lua;
        return this;
    }

    public abstract World build();
}
