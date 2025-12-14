package ethanjones.cubes.block.blocks;

import ethanjones.cubes.block.BlockBuilder;

public class BlockGlassBuilder extends BlockBuilder<BlockGlass, BlockGlassBuilder> {

    @Override
    protected BlockGlassBuilder self() {
        return this;
    }

    @Override
    public BlockGlass build() {
        BlockGlass glass = new BlockGlass();
        glass.miningTime = this.miningTime;
        glass.miningTool = this.miningTool;
        glass.miningToolLevel = this.miningToolLevel;
        glass.miningOther = this.miningOther;
        return glass;
    }
}