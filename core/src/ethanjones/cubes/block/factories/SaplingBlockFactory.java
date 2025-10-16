package ethanjones.cubes.block.factories;

import ethanjones.cubes.block.blocks.BlockSapling;
import ethanjones.cubes.block.BlockFactory;
import ethanjones.cubes.block.Block;

public class SaplingBlockFactory implements BlockFactory {
    @Override
    public Block createBlock() {
        return new BlockSapling();
    }
}
