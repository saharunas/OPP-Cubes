package ethanjones.cubes.entity.living.player.strategies;

import ethanjones.cubes.block.Block;

public interface GamemodeStrategy {
    boolean canFly();
    boolean hasNoClip();

    boolean canBreakBlocks();
    boolean instantBreak();
}
