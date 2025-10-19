package ethanjones.cubes.world.generator.strategies;

import ethanjones.cubes.world.generator.TerrainGenerationStrategy;
import ethanjones.cubes.world.generator.smooth.SmoothWorld;
import ethanjones.cubes.world.storage.Area;

public class SmoothWorldStrategy implements TerrainGenerationStrategy {
    private SmoothWorld generator;
    
    public SmoothWorldStrategy(long seed) {
        this.generator = new SmoothWorld(seed);
    }
    
    @Override
    public void generate(Area area) {
        generator.generate(area);
    }
    
    @Override
    public String getName() {
        return "core:smooth";
    }
}