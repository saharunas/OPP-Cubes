package ethanjones.cubes.block.factories;

import ethanjones.cubes.block.blocks.BlockDirt;
import ethanjones.cubes.block.BlockFactory;
import ethanjones.cubes.block.Block;

public class DirtBlockFactory implements BlockFactory {
    @Override
    public Block createBlock() {
        return new BlockDirt();
    }
}
