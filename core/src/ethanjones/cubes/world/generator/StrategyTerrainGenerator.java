package ethanjones.cubes.world.generator;

import ethanjones.cubes.world.generator.smooth.SmoothWorld;
import ethanjones.cubes.world.generator.strategies.*;
import ethanjones.cubes.world.reference.BlockReference;
import ethanjones.cubes.world.storage.Area;
import ethanjones.cubes.world.save.SaveOptions;
import ethanjones.cubes.world.server.WorldServer;
import ethanjones.cubes.world.CoordinateConverter;

public class StrategyTerrainGenerator extends TerrainGenerator {
    private TerrainGenerationStrategy currentStrategy;
    private TerrainGenerator actualGenerator;
    
    public StrategyTerrainGenerator(SaveOptions saveOptions) {
        // Create the actual generator and wrap it in strategy
        switch (saveOptions.worldType) {
            case "core:smooth":
                actualGenerator = new SmoothWorld(saveOptions.worldSeed);
                currentStrategy = new SmoothWorldStrategy(saveOptions.worldSeed);
                break;
            case "core:basic":
                actualGenerator = new BasicTerrainGenerator(saveOptions.worldSeedString);
                currentStrategy = new BasicTerrainStrategy(saveOptions.worldSeedString);
                break;
            case "core:test":
                actualGenerator = new TestTerrainGenerator();
                currentStrategy = new TestTerrainStrategy();
                break;
            case "core:void":
                actualGenerator = new VoidTerrainGenerator();
                currentStrategy = new VoidTerrainStrategy();
                break;
            default:
                actualGenerator = new BasicTerrainGenerator(saveOptions.worldSeedString);
                currentStrategy = new BasicTerrainStrategy(saveOptions.worldSeedString);
                break;
        }
    }
    
    public void setStrategy(TerrainGenerationStrategy strategy) {
        this.currentStrategy = strategy;
    }
    
    public TerrainGenerationStrategy getStrategy() {
        return currentStrategy;
    }

    @Override
    public void features(Area area, WorldServer world) {
        if (actualGenerator != null) {
            actualGenerator.features(area, world);
        }
    }
    
    @Override
    public void generate(Area area) {
        if (currentStrategy != null) {
            currentStrategy.generate(area);
        }
    }
    
    @Override
    public BlockReference spawnPoint(WorldServer world) {
        // Use the actual generator's spawn point logic
        if (actualGenerator != null) {
            return actualGenerator.spawnPoint(world);
        }
        // Default spawn point if no generator
        return new BlockReference().setFromBlockCoordinates(0, 5, 0);
    }
}