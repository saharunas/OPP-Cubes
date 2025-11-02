package ethanjones.cubes.block.blocks;

import ethanjones.cubes.block.BlockBuilder;

public class BlockLeavesBuilder extends BlockBuilder<BlockLeaves, BlockLeavesBuilder> {

    @Override
    protected BlockLeavesBuilder self() {
        return this;
    }

    @Override
    public BlockLeaves build() {
        BlockLeaves leaves = new BlockLeaves(id);
        leaves.miningTime = this.miningTime;
        leaves.miningTool = this.miningTool;
        leaves.miningToolLevel = this.miningToolLevel;
        leaves.miningOther = this.miningOther;
        return leaves;
    }
}