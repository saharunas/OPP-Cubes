package ethanjones.cubes.block.factories;

import ethanjones.cubes.block.blocks.BlockChest;
import ethanjones.cubes.block.BlockFactory;
import ethanjones.cubes.block.Block;

public class ChestBlockFactory implements BlockFactory {
    @Override
    public Block createBlock() {
        return new BlockChest();
    }
}
