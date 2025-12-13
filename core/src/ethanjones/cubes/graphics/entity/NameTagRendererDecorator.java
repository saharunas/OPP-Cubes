package ethanjones.cubes.graphics.entity;

import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.graphics.menu.Fonts;
import ethanjones.cubes.world.save.Gamemode;
import ethanjones.cubes.side.common.Cubes;
import ethanjones.cubes.side.common.Side;
import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.graphics.CubesRenderable;
import ethanjones.cubes.graphics.CubesVertexAttributes;
import ethanjones.cubes.side.client.CubesClient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Disposable;

/**
 * Pattern: Decorator (ConcreteDecorator - Level 1)
 * 
 * Adds name tag rendering above player's head.
 * Name color depends on game mode:
 * - Creative: GREEN
 * - Survival: WHITE
 * 
 * This decorator provides:
 * - Visible player name above head (billboard quad)
 * - Color-coded by game mode
 * - Proper 3D positioning
 * - Camera-facing text
 * 
 * Example usage:
 *   PlayerRendererInterface renderer = new NameTagRendererDecorator(
 *       new BasicPlayerRenderer()
 *   );
 */
public class NameTagRendererDecorator extends PlayerRendererDecorator {
    
    private static final float NAME_TAG_HEIGHT_OFFSET = 1.2f; // Above player head
    private static final float NAME_TAG_SCALE = 0.012f; // Scale for text size in 3D
    
    private BitmapFont font;
    private GlyphLayout layout;
    private SpriteBatch spriteBatch;
    
    // Cache for name tag textures per player
    private java.util.Map<String, NameTagData> nameTagCache;
    
    private static class NameTagData {
        Texture texture;
        Mesh mesh;
        Material material;
        CubesRenderable renderable;
        float width;
        float height;
        
        void dispose() {
            if (texture != null) texture.dispose();
            if (mesh != null) mesh.dispose();
        }
    }
    
    public NameTagRendererDecorator(PlayerRendererInterface wrappedRenderer) {
        super(wrappedRenderer);
        this.font = Fonts.smallHUD;
        this.layout = new GlyphLayout();
        this.spriteBatch = new SpriteBatch();
        this.nameTagCache = new java.util.HashMap<String, NameTagData>();
    }
    
    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool, Player player) {
        // First, render the base player model
        wrappedRenderer.getRenderables(renderables, pool, player);
        
        // Add name tag rendering
        if (shouldRenderNameTag(player)) {
            Color nameColor = getNameColor(player);
            Vector3 namePos = getNameTagPosition(player);
            
            // Get or create name tag data for this player
            NameTagData tagData = getOrCreateNameTag(player.username, nameColor);
            
            if (tagData != null && tagData.renderable != null) {
                // Update billboard orientation to face camera
                updateBillboardTransform(tagData.renderable, namePos, tagData.width, tagData.height);
                
                // Add to render queue
                renderables.add(tagData.renderable);
            }
        }
    }
    
    /**
     * Get or create cached name tag texture and mesh for a player
     */
    private NameTagData getOrCreateNameTag(String username, Color color) {
        if (nameTagCache.containsKey(username)) {
            return nameTagCache.get(username);
        }
        
        try {
            NameTagData data = createNameTagTexture(username, color);
            nameTagCache.put(username, data);
            return data;
        } catch (Exception e) {
            Log.error("Failed to create name tag for " + username, e);
            return null;
        }
    }
    
    /**
     * Create texture with player name rendered on it
     */
    private NameTagData createNameTagTexture(String username, Color color) {
        NameTagData data = new NameTagData();
        
        // Measure text
        layout.setText(font, username);
        int textWidth = (int) Math.ceil(layout.width);
        int textHeight = (int) Math.ceil(layout.height);
        
        // Validation and logging
        Log.debug("Creating name tag for '" + username + "': text size " + textWidth + "x" + textHeight);
        
        // Ensure minimum size
        if (textWidth < 1) textWidth = 32;
        if (textHeight < 1) textHeight = 16;
        
        // Add padding
        int padding = 8;
        int texWidth = textWidth + padding * 2;
        int texHeight = textHeight + padding * 2;
        
        // Make sure dimensions are power of 2 for compatibility
        texWidth = nextPowerOfTwo(texWidth);
        texHeight = nextPowerOfTwo(texHeight);
        
        Log.debug("Final texture size: " + texWidth + "x" + texHeight);
        
        // Validation
        if (texWidth <= 0 || texHeight <= 0 || texWidth > 2048 || texHeight > 2048) {
            Log.error("Invalid texture dimensions: " + texWidth + "x" + texHeight);
            return null;
        }
        
        // Create pixmap and render text directly
        Pixmap pixmap = null;
        FrameBuffer fbo = null;
        
        try {
            // Try to render text using FrameBuffer + SpriteBatch
            fbo = new FrameBuffer(Pixmap.Format.RGBA8888, texWidth, texHeight, false);
            
            fbo.begin();
            
            // Clear with transparent background
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            
            // Enable blending for transparency
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            
            // Render text
            spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, texWidth, texHeight);
            spriteBatch.begin();
            font.setColor(color);
            font.draw(spriteBatch, username, padding, texHeight - padding - 4);
            spriteBatch.end();
            
            fbo.end();
            
            // Read pixels from framebuffer
            pixmap = new Pixmap(texWidth, texHeight, Pixmap.Format.RGBA8888);
            Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, fbo.getFramebufferHandle());
            Gdx.gl.glReadPixels(0, 0, texWidth, texHeight, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixmap.getPixels());
            Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0);
            
            // Create texture from pixmap
            data.texture = new Texture(pixmap);
            data.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            
            Log.debug("Successfully created name tag texture for '" + username + "'");
            
        } catch (Exception e) {
            Log.error("Error creating name tag texture for '" + username + "', size " + texWidth + "x" + texHeight, e);
            if (pixmap != null) {
                pixmap.dispose();
                pixmap = null;
            }
            if (fbo != null) {
                fbo.dispose();
                fbo = null;
            }
            return null;
        } finally {
            if (pixmap != null) {
                pixmap.dispose();
            }
            if (fbo != null) {
                fbo.dispose();
            }
        }
        
        // Store dimensions in world units
        data.width = texWidth * NAME_TAG_SCALE;
        data.height = texHeight * NAME_TAG_SCALE;
        
        // Create billboard mesh (simple quad)
        data.mesh = createBillboardMesh(data.width, data.height);
        
        // Create material with texture
        data.material = new Material();
        data.material.set(TextureAttribute.createDiffuse(data.texture));
        data.material.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
        
        // Create renderable
        data.renderable = new CubesRenderable();
        data.renderable.meshPart.mesh = data.mesh;
        data.renderable.meshPart.offset = 0;
        data.renderable.meshPart.size = 6; // 2 triangles = 6 indices
        data.renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
        data.renderable.material = data.material;
        data.renderable.name = "NameTag:" + username;
        data.renderable.setFogEnabled(false);
        
        return data;
    }
    
    /**
     * Get next power of 2 for texture dimensions
     */
    private int nextPowerOfTwo(int value) {
        int power = 1;
        while (power < value) {
            power *= 2;
        }
        return power;
    }
    
    /**
     * Create a simple quad mesh for billboard
     */
    private Mesh createBillboardMesh(float width, float height) {
        float halfW = width / 2f;
        float halfH = height / 2f;
        
        // CubesVertexAttributes.VERTEX_ATTRIBUTES format:
        // Position (3): x, y, z
        // TexCoord (2): u, v
        // VoxelLight (1): light value
        // Total: 6 components per vertex
        
        float[] vertices = new float[] {
            // Vertex 0: Bottom-left
            -halfW, -halfH, 0,   // Position
            0, 0,                // TexCoord
            15,                  // VoxelLight (full brightness)
            
            // Vertex 1: Bottom-right
             halfW, -halfH, 0,   // Position
            1, 0,                // TexCoord
            15,                  // VoxelLight
            
            // Vertex 2: Top-right
             halfW,  halfH, 0,   // Position
            1, 1,                // TexCoord
            15,                  // VoxelLight
            
            // Vertex 3: Top-left
            -halfW,  halfH, 0,   // Position
            0, 1,                // TexCoord
            15                   // VoxelLight
        };
        
        short[] indices = new short[] {
            0, 1, 2,  // First triangle
            2, 3, 0   // Second triangle
        };
        
        Mesh mesh = new Mesh(true, 4, 6, CubesVertexAttributes.VERTEX_ATTRIBUTES);
        mesh.setVertices(vertices);
        mesh.setIndices(indices);
        
        return mesh;
    }
    
    /**
     * Update billboard transform to face camera and position at namePos
     */
    private void updateBillboardTransform(CubesRenderable renderable, Vector3 position, float width, float height) {
        try {
            // Get camera from client
            CubesClient client = Cubes.getClient();
            if (client == null || client.renderer == null || client.renderer.worldRenderer == null) {
                return;
            }
            
            Vector3 camPos = client.renderer.worldRenderer.camera.position;
            Vector3 camDir = new Vector3(camPos).sub(position).nor();
            
            // Calculate billboard rotation to face camera
            Matrix4 transform = renderable.worldTransform;
            transform.idt();
            
            // Position
            transform.setTranslation(position);
            
            // Rotate to face camera (billboard effect)
            // Calculate rotation around Y axis
            float angleY = (float) Math.atan2(camDir.x, camDir.z);
            transform.rotate(Vector3.Y, (float) Math.toDegrees(angleY));
            
        } catch (Exception e) {
            // Fallback: just position without rotation
            renderable.worldTransform.idt();
            renderable.worldTransform.setTranslation(position);
        }
    }
    
    private boolean shouldRenderNameTag(Player player) {
        // Don't render name tag for local player
        if (!Side.isClient()) return false;
        try {
            if (player == Cubes.getClient().player) return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    /**
     * Get the color for player name based on game mode
     */
    private Color getNameColor(Player player) {
        try {
            if (Cubes.getServer() != null) {
                Gamemode gamemode = Cubes.getServer().world.save.getSaveOptions().worldGamemode;
                if (gamemode == Gamemode.creative) {
                    return Color.GREEN;
                }
            }
        } catch (Exception e) {
            // Fallback to white
        }
        return Color.WHITE;
    }
    
    /**
     * Get the position for name tag (above player's head)
     */
    private Vector3 getNameTagPosition(Player player) {
        Vector3 pos = new Vector3(player.position);
        pos.y += NAME_TAG_HEIGHT_OFFSET;
        return pos;
    }
    
    /**
     * Dispose resources when decorator is no longer needed
     */
    public void dispose() {
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
        for (NameTagData data : nameTagCache.values()) {
            data.dispose();
        }
        nameTagCache.clear();
    }
}
