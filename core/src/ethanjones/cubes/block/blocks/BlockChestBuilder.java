package ethanjones.cubes.block.blocks;

import ethanjones.cubes.block.BlockBuilder;

public class BlockChestBuilder extends BlockBuilder<BlockChest, BlockChestBuilder> {

    @Override
    protected BlockChestBuilder self() {
        return this;
    }

    @Override
    public BlockChest build() {
        BlockChest chest = new BlockChest(id);
        chest.miningTime = this.miningTime;
        chest.miningTool = this.miningTool;
        chest.miningToolLevel = this.miningToolLevel;
        chest.miningOther = this.miningOther;
        return chest;
    }
}