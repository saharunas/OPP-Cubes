package ethanjones.cubes.graphics.entity;

import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.graphics.CubesRenderable;
import ethanjones.cubes.graphics.assets.Assets;
import ethanjones.cubes.core.logging.Log;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Pattern: Decorator (ConcreteDecorator - Level 3)
 * 
 * Adds color tint to player model based on selected skin color.
 * 
 * Skin colors:
 * - "red" - Red tint
 * - "green" - Green tint
 * - "blue" - Blue tint
 * - "default" - No tint (original black)
 * 
 * This decorator provides:
 * - Color customization for player models
 * - Multiplayer visual differentiation
 * - Persistent color selection
 * 
 * Example usage:
 *   PlayerRendererInterface renderer = new SkinRendererDecorator(
 *       new NameTagRendererDecorator(new BasicPlayerRenderer()),
 *       player
 *   );
 */
public class SkinRendererDecorator extends PlayerRendererDecorator {
    
    private Player player;
    
    // Cache for tinted textures per color
    private static java.util.Map<String, Texture> coloredTextureCache = new java.util.HashMap<String, Texture>();
    
    public SkinRendererDecorator(PlayerRendererInterface wrappedRenderer, Player player) {
        super(wrappedRenderer);
        this.player = player;
    }
    
    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool, Player player) {
        // Get the current size before wrapped renderer adds renderables
        int beforeSize = renderables.size;
        
        // First, render the base player model
        wrappedRenderer.getRenderables(renderables, pool, player);
        
        Log.debug("SkinDecorator for " + player.username + ", color: " + player.getSkinColor() + ", renderables added: " + (renderables.size - beforeSize));
        
        // Apply colored texture to newly added renderables
        String skinColor = player.getSkinColor();
        if (skinColor != null && !skinColor.equals("default")) {
            // Apply tinted texture to all renderables added by wrapped renderer
            for (int i = beforeSize; i < renderables.size; i++) {
                Renderable renderable = renderables.get(i);
                
                Log.debug("Checking renderable " + i + ": " + renderable.getClass().getSimpleName());
                
                // Only apply to player model, not name tags
                if (renderable instanceof CubesRenderable) {
                    CubesRenderable cubesRenderable = (CubesRenderable) renderable;
                    Log.debug("CubesRenderable name: " + cubesRenderable.name);
                    
                    if (cubesRenderable.name != null && cubesRenderable.name.equals("Player")) {
                        Log.info("Applying skin color '" + skinColor + "' to " + player.username);
                        
                        // Clone the material to avoid affecting other players
                        renderable.material = renderable.material.copy();
                        
                        // Get or create tinted texture
                        Texture tintedTexture = getOrCreateTintedTexture(skinColor);
                        if (tintedTexture != null) {
                            // Replace the texture in material
                            TextureAttribute texAttr = TextureAttribute.createDiffuse(tintedTexture);
                            renderable.material.set(texAttr);
                            Log.info("Successfully applied tinted texture for color: " + skinColor);
                        } else {
                            Log.error("Failed to get tinted texture for color: " + skinColor);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Get or create a tinted version of the player texture
     */
    private Texture getOrCreateTintedTexture(String colorName) {
        // Check cache first
        if (coloredTextureCache.containsKey(colorName)) {
            return coloredTextureCache.get(colorName);
        }
        
        try {
            // Get the original player texture
            TextureRegion originalRegion = Assets.getTextureRegion("core:world/player.png");
            Texture originalTexture = originalRegion.getTexture();
            
            // Download texture data to pixmap
            if (!originalTexture.getTextureData().isPrepared()) {
                originalTexture.getTextureData().prepare();
            }
            Pixmap originalPixmap = originalTexture.getTextureData().consumePixmap();
            
            // Create new pixmap for tinted version
            int width = originalPixmap.getWidth();
            int height = originalPixmap.getHeight();
            Pixmap tintedPixmap = new Pixmap(width, height, originalPixmap.getFormat());
            
            // Get tint color
            Color tintColor = getSkinTintColor(colorName);
            
            // Apply tint to each pixel
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = originalPixmap.getPixel(x, y);
                    
                    // Extract RGBA components
                    float r = ((pixel >> 24) & 0xff) / 255f;
                    float g = ((pixel >> 16) & 0xff) / 255f;
                    float b = ((pixel >> 8) & 0xff) / 255f;
                    float a = (pixel & 0xff) / 255f;
                    
                    // Apply tint (multiply)
                    r *= tintColor.r;
                    g *= tintColor.g;
                    b *= tintColor.b;
                    
                    // Set tinted pixel
                    Color tinted = new Color(r, g, b, a);
                    tintedPixmap.setColor(tinted);
                    tintedPixmap.drawPixel(x, y);
                }
            }
            
            // Create texture from tinted pixmap
            Texture tintedTexture = new Texture(tintedPixmap);
            tintedTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            
            // Cleanup
            tintedPixmap.dispose();
            originalPixmap.dispose();
            
            // Cache it
            coloredTextureCache.put(colorName, tintedTexture);
            
            Log.info("Created tinted player texture for color: " + colorName);
            return tintedTexture;
            
        } catch (Exception e) {
            Log.error("Failed to create tinted texture for color: " + colorName, e);
            return null;
        }
    }
    
    /**
     * Get the color tint based on color name
     */
    private Color getSkinTintColor(String colorName) {
        if (colorName.equals("red")) {
            return new Color(1.5f, 0.5f, 0.5f, 1.0f); // Red tint (boost red, dim others)
        }
        
        if (colorName.equals("green")) {
            return new Color(0.5f, 1.5f, 0.5f, 1.0f); // Green tint
        }
        
        if (colorName.equals("blue")) {
            return new Color(0.5f, 0.5f, 1.5f, 1.0f); // Blue tint
        }
        
        return new Color(1.0f, 1.0f, 1.0f, 1.0f); // No tint
    }
    
    /**
     * Dispose cached textures
     */
    public static void disposeCachedTextures() {
        for (Texture texture : coloredTextureCache.values()) {
            texture.dispose();
        }
        coloredTextureCache.clear();
    }
}
