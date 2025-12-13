package ethanjones.cubes.graphics.entity;

import ethanjones.cubes.entity.living.player.Player;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Pattern: Decorator (Abstract Decorator)
 * 
 * Abstract base class for all player renderer decorators.
 * Maintains a reference to a PlayerRendererInterface component and delegates
 * rendering operations to it by default.
 * 
 * Concrete decorators extend this class and override methods to add their
 * visual functionality before/after delegating to the wrapped component.
 * 
 * This enables stacking multiple decorators:
 * NameTag(Tool(Skin(BasicPlayerRenderer)))
 */
public abstract class PlayerRendererDecorator implements PlayerRendererInterface {
    
    protected final PlayerRendererInterface wrappedRenderer;
    
    public PlayerRendererDecorator(PlayerRendererInterface wrappedRenderer) {
        this.wrappedRenderer = wrappedRenderer;
    }
    
    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool, Player player) {
        wrappedRenderer.getRenderables(renderables, pool, player);
    }
}
