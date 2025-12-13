package ethanjones.cubes.entity.living.player.strategies;

import ethanjones.cubes.entity.living.player.strategies.GamemodeStrategy;

public class SurvivalStrategy implements GamemodeStrategy {
    @Override
    public boolean canFly() { return false; };

    @Override
    public boolean hasNoClip() { return false; };

    @Override
    public boolean canBreakBlocks() { return true; };
    
    @Override
    public boolean instantBreak() { return false; };
}
