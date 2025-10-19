package ethanjones.cubes.world.generator;

import ethanjones.cubes.world.storage.Area;

public interface TerrainGenerationStrategy {
    void generate(Area area);
    String getName();
}