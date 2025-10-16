package ethanjones.cubes.block;

import ethanjones.cubes.block.blocks.*;
import ethanjones.cubes.block.factories.BlockFactoryRegistry;
import ethanjones.cubes.block.factories.ChestBlockFactory;
import ethanjones.cubes.block.factories.DirtBlockFactory;
import ethanjones.cubes.block.factories.GlassBlockFactory;
import ethanjones.cubes.block.factories.GrassBlockFactory;
import ethanjones.cubes.block.factories.LeavesBlockFactory;
import ethanjones.cubes.block.factories.SaplingBlockFactory;
import ethanjones.cubes.core.id.GetInstances.GetBlock;
import ethanjones.cubes.core.id.IDManager;

public class Blocks {

  @GetBlock("core:bedrock")
  public static Block bedrock;
  @GetBlock("core:stone")
  public static Block stone;

  public static Block dirt;

  public static Block grass;
  @GetBlock("core:log")
  public static Block log;
  
  public static Block leaves;
  @GetBlock("core:glow")
  public static Block glow;
  
  public static Block glass;

  public static Block chest;
  
  public static Block sapling;

  public static void init() {
    BlockFactoryRegistry.register("dirt", new DirtBlockFactory());
    BlockFactoryRegistry.register("grass", new GrassBlockFactory());
    BlockFactoryRegistry.register("leaves", new LeavesBlockFactory());
    BlockFactoryRegistry.register("glass", new GlassBlockFactory());
    BlockFactoryRegistry.register("chest", new ChestBlockFactory());
    BlockFactoryRegistry.register("sapling", new SaplingBlockFactory());

    dirt = BlockFactoryRegistry.create("dirt");
    IDManager.register(dirt);
    grass = BlockFactoryRegistry.create("grass");
    IDManager.register(grass);
    leaves = BlockFactoryRegistry.create("leaves");
    IDManager.register(leaves);
    glass = BlockFactoryRegistry.create("glass");
    IDManager.register(glass);
    chest = BlockFactoryRegistry.create("chest");
    IDManager.register(chest);
    sapling = BlockFactoryRegistry.create("sapling");
    IDManager.register(sapling);
  }
}
