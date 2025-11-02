package ethanjones.cubes.block;

import ethanjones.cubes.item.ItemTool;
import ethanjones.cubes.block.blocks.*;
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
    public static Block sapling;
    public static Block glass;
    public static Block chest;
    @GetBlock("core:glow")
    public static Block glow;

    // Called once at startup (from Cubes.java)
    public static void init() {
        dirt = new BlockDirtBuilder().id("core:dirt").build();
        IDManager.register(dirt);

        grass = new BlockGrassBuilder().id("core:grass").build();
        IDManager.register(grass);

        leaves = new BlockLeavesBuilder().id("core:leaves").build();
        IDManager.register(leaves);

        glass = new BlockGlassBuilder().id("core:glass").build();
        IDManager.register(glass);

        chest = new BlockChestBuilder().id("core:chest").build();
        IDManager.register(chest);

        sapling = new BlockSaplingBuilder().id("core:sapling").build();
        IDManager.register(sapling);
    }

    private Blocks() {}
}