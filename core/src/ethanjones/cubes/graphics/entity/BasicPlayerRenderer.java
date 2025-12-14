package ethanjones.cubes.graphics.entity;

import ethanjones.cubes.entity.living.player.Player;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Pattern: Decorator (ConcreteComponent)
 * 
 * BasicPlayerRenderer wraps the original PlayerRenderer static methods
 * and implements PlayerRendererInterface.
 * This is the concrete component that decorators will wrap to add
 * additional visual features like name tags, tools, and skins.
 */
public class BasicPlayerRenderer implements PlayerRendererInterface {
    
    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool, Player player) {
        // Delegate to original PlayerRenderer static method
        PlayerRenderer.getRenderables(renderables, pool, player);
    }
}
