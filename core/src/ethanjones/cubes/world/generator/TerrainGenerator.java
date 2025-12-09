package ethanjones.cubes.world.generator;

import ethanjones.cubes.block.Block;
import ethanjones.cubes.core.util.locks.Locked;
import ethanjones.cubes.world.reference.BlockReference;
import ethanjones.cubes.world.server.WorldServer;
import ethanjones.cubes.world.storage.Area;

public abstract class TerrainGenerator {

  // TEMPLATE METHOD - defines the skeleton of the terrain generation algorithm
  // This is the preferred way to generate a complete area
  public final void generateArea(Area area, WorldServer world) {
    // Step 1: Generate base terrain structure
    generateTerrain(area);
    
    // Step 2: Apply geological features (caves, ravines, etc.)
    generateCaves(area, world);
    
    // Step 3: Add vegetation (trees, grass, flowers)
    generateVegetation(area, world);
    
    // Step 4: Add structures (buildings, ruins, etc.)
    generateStructures(area, world);
    
    // Step 5: Add decorations (ores, special blocks)
    generateDecorations(area, world);
    
    // Step 6: Apply final post-processing
    postProcess(area, world);
  }

  // HOOK METHOD - must be implemented by subclasses
  // Generates the base terrain structure (blocks, height, ground layers)
  protected abstract void generateTerrain(Area area);

  // HOOK METHOD - optional, generates caves and underground features
  protected void generateCaves(Area area, WorldServer world) {
    // Default: no caves
  }

  // HOOK METHOD - optional, generates trees, grass, and other plants
  protected void generateVegetation(Area area, WorldServer world) {
    // Default: no vegetation
  }

  // HOOK METHOD - optional, generates buildings, ruins, villages
  protected void generateStructures(Area area, WorldServer world) {
    // Default: no structures
  }

  // HOOK METHOD - optional, generates ores, special blocks, details
  protected void generateDecorations(Area area, WorldServer world) {
    // Default: no decorations
  }

  // HOOK METHOD - optional, applies final post-processing steps
  protected void postProcess(Area area, WorldServer world) {
    // Default: do nothing
  }

  // Legacy methods for backward compatibility with WorldTasks
  // These are called separately by the world generation pipeline
  public final void generate(Area area) {
    generateTerrain(area);
  }

  public final void features(Area area, WorldServer world) {
    // Legacy method calls all feature generation hooks
    generateCaves(area, world);
    generateVegetation(area, world);
    generateStructures(area, world);
    generateDecorations(area, world);
  }

  public abstract BlockReference spawnPoint(WorldServer world);

  public RainStatus getRainStatus(float x, float z, float rainTime) {
    return RainStatus.NOT_RAINING;
  }

  public static void set(Area area, Block block, int x, int y, int z, int meta) {
    int ref = Area.getRef(x, y, z);

    try (Locked<Area> locked = area.acquireWriteLock()) {
      area.setupArrays(y);
      area.blocks[ref] = (block == null ? 0 : block.intID + ((meta & 0xFF) << 20));
    }
  }
  
  public static void setNeighbour(Area area, Block block, int x, int y, int z, int meta) {
    Area a = area.neighbourBlockCoordinates(x, z);
    set(a, block, x - a.minBlockX, y, z - a.minBlockZ, meta);
  }

  public static void setVisible(Area area, Block block, int x, int y, int z, int meta) {
    int ref = Area.getRef(x, y, z);

    try (Locked<Area> locked = area.acquireWriteLock()) {
      area.setupArrays(y);
      area.blocks[ref] = (block == null ? 0 : (block.intID + ((meta & 0xFF) << 20)) | Area.BLOCK_VISIBLE);
    }
  }

  public static void setVisibleNeighbour(Area area, Block block, int x, int y, int z, int meta) {
    Area a = area.neighbourBlockCoordinates(x, z);
    setVisible(a, block, x - a.minBlockX, y, z - a.minBlockZ, meta);
  }
}
