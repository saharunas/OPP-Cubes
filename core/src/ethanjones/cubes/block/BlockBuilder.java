package ethanjones.cubes.block;

import ethanjones.cubes.item.ItemTool;

/**
 * Generic abstract builder for all Block subclasses.
 * @param <T> the concrete Block subtype being built
 * @param <B> the concrete Builder subtype (for fluent chaining)
 */
public abstract class BlockBuilder<T extends Block, B extends BlockBuilder<T, B>> {

    // ---- common configurable block fields ----
    protected String id;
    protected float miningTime = 0.5f;
    protected ItemTool.ToolType miningTool = ItemTool.ToolType.pickaxe;
    protected int miningToolLevel = 1;
    protected boolean miningOther = true;

    // ---- fluent setters shared by all builders ----
    public B id(String id) {
        this.id = id;
        return self();
    }

    public B miningTime(float time) {
        this.miningTime = time;
        return self();
    }

    public B miningTool(ItemTool.ToolType toolType) {
        this.miningTool = toolType;
        return self();
    }

    public B miningToolLevel(int level) {
        this.miningToolLevel = level;
        return self();
    }

    public B miningOther(boolean allowed) {
        this.miningOther = allowed;
        return self();
    }

    // implemented in each concrete builder to return the right subtype for chaining
    protected abstract B self();

    // each concrete builder must know how to create its block
    public abstract T build();
}