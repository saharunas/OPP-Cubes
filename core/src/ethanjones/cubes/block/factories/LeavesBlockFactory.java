package ethanjones.cubes.block.factories;

import ethanjones.cubes.block.blocks.BlockLeaves;
import ethanjones.cubes.block.BlockFactory;
import ethanjones.cubes.block.Block;

public class LeavesBlockFactory implements BlockFactory {
    @Override
    public Block createBlock() {
        return new BlockLeaves();
    }
}
