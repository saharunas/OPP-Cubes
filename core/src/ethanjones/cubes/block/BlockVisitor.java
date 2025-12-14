package ethanjones.cubes.block;

import ethanjones.cubes.block.blocks.BlockChest;
import ethanjones.cubes.block.blocks.BlockDirt;
import ethanjones.cubes.block.blocks.BlockGlass;
import ethanjones.cubes.block.blocks.BlockGrass;

public interface BlockVisitor<R> {

    R visitBlock(Block block);

    R visitDirt(BlockDirt dirt);

    R visitGrass(BlockGrass grass);

    R visitGlass(BlockGlass glass);

    R visitChest(BlockChest chest);
}
