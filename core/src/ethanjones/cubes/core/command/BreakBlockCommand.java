package ethanjones.cubes.core.command;

import ethanjones.cubes.world.World;
import ethanjones.cubes.block.Block;

public class BreakBlockCommand implements Command {
    private final World world;
    private final int x, y, z;
    private Block previousBlock;

    public BreakBlockCommand(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void execute() {
        previousBlock = world.getBlock(x, y, z);
        world.setBlock(null, x, y, z); // null = air / empty space
    }

    @Override
    public void undo() {
        world.setBlock(previousBlock, x, y, z);
    }
}