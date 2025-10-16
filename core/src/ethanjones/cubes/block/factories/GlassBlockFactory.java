package ethanjones.cubes.block.factories;

import ethanjones.cubes.block.blocks.BlockGlass;
import ethanjones.cubes.block.BlockFactory;
import ethanjones.cubes.block.Block;

public class GlassBlockFactory implements BlockFactory {
    @Override
    public Block createBlock() {
        return new BlockGlass();
    }
}
