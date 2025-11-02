package ethanjones.cubes.block.blocks;

import ethanjones.cubes.block.Block;

public class BlockGlass extends Block {

    protected BlockGlass(String id) {
        super(id);
    }

    @Override
    public boolean alwaysTransparent() {
        return true;
    }

    @Override
    public boolean canBeTransparent() {
        return true;
    }
}