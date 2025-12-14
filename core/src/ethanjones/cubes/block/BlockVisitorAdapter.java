package ethanjones.cubes.block;

import ethanjones.cubes.block.blocks.BlockChest;
import ethanjones.cubes.block.blocks.BlockDirt;
import ethanjones.cubes.block.blocks.BlockGlass;
import ethanjones.cubes.block.blocks.BlockGrass;

/**
 * Java 7 compatible adapter so subclasses only override what they need.
 */
public abstract class BlockVisitorAdapter<R> implements BlockVisitor<R> {

    @Override
    public R visitDirt(BlockDirt dirt) {
        return visitBlock(dirt);
    }

    @Override
    public R visitGrass(BlockGrass grass) {
        return visitBlock(grass);
    }

    @Override
    public R visitGlass(BlockGlass glass) {
        return visitBlock(glass);
    }

    @Override
    public R visitChest(BlockChest chest) {
        return visitBlock(chest);
    }
}
