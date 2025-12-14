package ethanjones.cubes.block.blocks;

import ethanjones.cubes.block.BlockBuilder;

public class BlockSaplingBuilder extends BlockBuilder<BlockSapling, BlockSaplingBuilder> {

    @Override
    protected BlockSaplingBuilder self() {
        return this;
    }

    @Override
    public BlockSapling build() {
        BlockSapling sapling = new BlockSapling(id);
        sapling.miningTime = this.miningTime;
        sapling.miningTool = this.miningTool;
        sapling.miningToolLevel = this.miningToolLevel;
        sapling.miningOther = this.miningOther;
        return sapling;
    }
}