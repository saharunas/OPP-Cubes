package ethanjones.cubes.world.generator.strategies;

import ethanjones.cubes.world.generator.TerrainGenerationStrategy;
import ethanjones.cubes.world.generator.TestTerrainGenerator;
import ethanjones.cubes.world.storage.Area;

public class TestTerrainStrategy implements TerrainGenerationStrategy {
    private TestTerrainGenerator generator;
    
    public TestTerrainStrategy() {
        this.generator = new TestTerrainGenerator();
    }
    
    @Override
    public void generate(Area area) {
        generator.generate(area);
    }
    
    @Override
    public String getName() {
        return "core:test";
    }
}