package ethanjones.cubes.block.blocks;

import ethanjones.cubes.block.BlockBuilder;

public class BlockDirtBuilder extends BlockBuilder<BlockDirt, BlockDirtBuilder> {

    @Override
    protected BlockDirtBuilder self() {
        return this;
    }

    @Override
    public BlockDirt build() {
        BlockDirt dirt = new BlockDirt();
        dirt.miningTime = this.miningTime;
        dirt.miningTool = this.miningTool;
        dirt.miningToolLevel = this.miningToolLevel;
        dirt.miningOther = this.miningOther;
        return dirt;
    }
}