package ethanjones.cubes.entity.living.player.strategies;

import ethanjones.cubes.entity.living.player.strategies.GamemodeStrategy;

public class AdventureStrategy implements GamemodeStrategy {
    @Override
    public boolean canFly() { return false; };
    
    @Override
    public boolean hasNoClip() { return false; };

    @Override
    public boolean canBreakBlocks() { return false; };

    @Override
    public boolean instantBreak() { return false; };
}
