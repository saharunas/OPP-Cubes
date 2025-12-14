package ethanjones.cubes.block.blocks;

import ethanjones.cubes.block.BlockBuilder;

public class BlockGrassBuilder extends BlockBuilder<BlockGrass, BlockGrassBuilder> {

    @Override
    protected BlockGrassBuilder self() {
        return this;
    }

    @Override
    public BlockGrass build() {
        BlockGrass grass = new BlockGrass(id);
        grass.miningTime = this.miningTime;
        grass.miningTool = this.miningTool;
        grass.miningToolLevel = this.miningToolLevel;
        grass.miningOther = this.miningOther;
        return grass;
    }
}