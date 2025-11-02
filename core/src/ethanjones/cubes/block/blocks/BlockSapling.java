package ethanjones.cubes.block.blocks;

import ethanjones.cubes.block.Block;

public class BlockSapling extends Block {

    protected BlockSapling(String id) {
        super(id);
    }

    @Override
    public boolean canBeTransparent() {
        return true;
    }
}