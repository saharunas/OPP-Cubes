package ethanjones.cubes.core.command;

import ethanjones.cubes.world.World;
import ethanjones.cubes.block.Block;

public class PlaceBlockCommand implements Command {
    private final World world;
    private final Block newBlock;
    private final int x, y, z;
    private final int meta;
    private Block previousBlock;
    private int previousMeta;

    public PlaceBlockCommand(World world, Block newBlock, int x, int y, int z, int meta) {
        this.world = world;
        this.newBlock = newBlock;
        this.x = x;
        this.y = y;
        this.z = z;
        this.meta = meta;
    }

    @Override
    public void execute() {
        previousBlock = world.getBlock(x, y, z);
        previousMeta = world.getMeta(x, y, z); // if this exists
        world.setBlock(newBlock, x, y, z, meta);
    }

    @Override
    public void undo() {
        world.setBlock(previousBlock, x, y, z, previousMeta);
    }
}