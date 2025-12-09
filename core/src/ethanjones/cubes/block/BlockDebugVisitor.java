package ethanjones.cubes.block;

import ethanjones.cubes.block.blocks.BlockChest;
import ethanjones.cubes.block.blocks.BlockDirt;
import ethanjones.cubes.block.blocks.BlockGlass;
import ethanjones.cubes.block.blocks.BlockGrass;

public class BlockDebugVisitor extends BlockVisitorAdapter<String> {

    @Override
    public String visitBlock(Block block) {
        // Generic info for any block
        return "Block: " + block.getClass().getSimpleName();
    }

    @Override
    public String visitDirt(BlockDirt dirt) {
        return "Dirt block (good for farming). Class=" + dirt.getClass().getSimpleName();
    }

    @Override
    public String visitGrass(BlockGrass grass) {
        return "Grass block (top surface). Class=" + grass.getClass().getSimpleName();
    }

    @Override
    public String visitGlass(BlockGlass glass) {
        return "Glass block (transparent). Class=" + glass.getClass().getSimpleName();
    }

    @Override
    public String visitChest(BlockChest chest) {
        return "Chest block (inventory container). Class=" + chest.getClass().getSimpleName();
    }
}
