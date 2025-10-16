package ethanjones.cubes.block.factories;

import ethanjones.cubes.block.blocks.BlockGrass;
import ethanjones.cubes.block.BlockFactory;
import ethanjones.cubes.block.Block;

public class GrassBlockFactory implements BlockFactory {
    @Override
    public Block createBlock() {
        return new BlockGrass();
    }
}
