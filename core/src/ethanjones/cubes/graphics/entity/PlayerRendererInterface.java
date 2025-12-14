package ethanjones.cubes.graphics.entity;

import ethanjones.cubes.entity.living.player.Player;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Pattern: Decorator (Component Interface)
 * 
 * Defines the interface for player rendering operations that can be decorated
 * with additional visual elements like name tags, held tools, and skin customization.
 * 
 * This interface allows decorators to be stacked to add different visual features
 * without modifying the base PlayerRenderer class.
 */
public interface PlayerRendererInterface {
    
    /**
     * Get renderables for the player
     * @param renderables Array to add renderables to
     * @param pool Pool for renderable objects
     * @param player The player to render
     */
    void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool, Player player);
}
