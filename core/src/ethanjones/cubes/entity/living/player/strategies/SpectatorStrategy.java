package ethanjones.cubes.entity.living.player.strategies;

import ethanjones.cubes.entity.living.player.strategies.GamemodeStrategy;

public class SpectatorStrategy implements GamemodeStrategy {
    @Override
    public boolean canFly() { return true; };
    
    @Override
    public boolean hasNoClip() { return true; };

    @Override
    public boolean canBreakBlocks() { return false; };

    @Override
    public boolean instantBreak() { return false; };
}