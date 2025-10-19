package ethanjones.cubes.world.generator.strategies;

import ethanjones.cubes.world.generator.TerrainGenerationStrategy;
import ethanjones.cubes.world.generator.BasicTerrainGenerator;
import ethanjones.cubes.world.storage.Area;

public class BasicTerrainStrategy implements TerrainGenerationStrategy {
    private BasicTerrainGenerator generator;
    
    public BasicTerrainStrategy(String seedString) {
        this.generator = new BasicTerrainGenerator(seedString);
    }
    
    @Override
    public void generate(Area area) {
        generator.generate(area);
    }
    
    @Override
    public String getName() {
        return "core:basic";
    }
}