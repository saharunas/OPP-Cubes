package ethanjones.cubes.world.generator.strategies;

import ethanjones.cubes.world.generator.TerrainGenerationStrategy;
import ethanjones.cubes.world.generator.VoidTerrainGenerator;
import ethanjones.cubes.world.storage.Area;

public class VoidTerrainStrategy implements TerrainGenerationStrategy {
    private VoidTerrainGenerator generator;
    
    public VoidTerrainStrategy() {
        this.generator = new VoidTerrainGenerator();
    }
    
    @Override
    public void generate(Area area) {
        generator.generate(area);
    }
    
    @Override
    public String getName() {
        return "core:void";
    }
}