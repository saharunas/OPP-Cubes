package ethanjones.cubes.graphics.entity;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.graphics.CubesRenderable;
import ethanjones.cubes.graphics.CubesVertexAttributes;
import ethanjones.cubes.graphics.world.block.FaceVertices;
import ethanjones.cubes.item.Item;
import ethanjones.cubes.item.ItemStack;
import ethanjones.cubes.item.ItemTool;

import static ethanjones.cubes.world.light.BlockLight.FULL_LIGHT;

/**
 * Decorator Level 3: Renders the selected tool/item from the player's hotbar
 * as a 3D model attached to the player's hand position.
 * 
 * Requirements:
 * - Shows currently selected item from hotbar as 3D model
 * - Positioned at player's right hand (side)
 * - Rotates with player (follows player.angle)
 * - Uses actual 3D block/item model, not billboard
 */
public class ToolRendererDecorator extends PlayerRendererDecorator {
  
  // Tool positioning relative to player center
  private static final float TOOL_OFFSET_RIGHT = 0.35f;  // Right from player center
  private static final float TOOL_OFFSET_FORWARD = 0.65f; // Forward from player center
  private static final float TOOL_OFFSET_UP = -1.35f;      // Up from player feet (hand height)
  private static final float TOOL_SIZE = 0.3f;           // Size of the 3D tool model
  
  private final Player player;
  
  // Mesh and renderable for tool (similar to ItemEntityRenderer)
  private Mesh toolMesh;
  private float[] toolVertices;
  private final CubesRenderable toolRenderable = new CubesRenderable();
  private Item cachedItem; // Track item changes
  private int cachedMeta;
  
  // Transform matrix and helper vectors
  private final Matrix4 toolTransform = new Matrix4();
  private final Vector3 rightVector = new Vector3();
  private final Vector3 forwardVector = new Vector3();
  
  // Indices for 2 flat quads (like ItemEntityRenderer for tools/items)
  private static short[] toolIndices;
  
  static {
    // Initialize indices for 2 quads (front and back, like ItemEntityRenderer)
    toolIndices = new short[6 * 2];
    short j = 0;
    for (int i = 0; i < toolIndices.length; i += 6, j += 4) {
      toolIndices[i + 0] = (short) (j + 0);
      toolIndices[i + 1] = (short) (j + 1);
      toolIndices[i + 2] = (short) (j + 2);
      toolIndices[i + 3] = (short) (j + 2);
      toolIndices[i + 4] = (short) (j + 3);
      toolIndices[i + 5] = (short) (j + 0);
    }
  }
  
  /**
   * Constructor for ToolRendererDecorator
   * 
   * @param wrappedRenderer The renderer to decorate
   * @param player The player whose tool to display
   */
  public ToolRendererDecorator(PlayerRendererInterface wrappedRenderer, Player player) {
    super(wrappedRenderer);
    this.player = player;
  }
  
  @Override
  public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool, Player player) {
    // First, delegate to wrapped renderer
    wrappedRenderer.getRenderables(renderables, pool, player);
    
    // Then add tool renderable if item is selected
    ItemStack selectedItem = player.getInventory().selectedItemStack();
    
    // Only render tools, not blocks or other items
    if (selectedItem != null && !(selectedItem.item instanceof ItemTool)) {
      selectedItem = null; // Treat non-tools as if nothing is selected
    }
    
    // Check if item changed (including to null)
    boolean itemChanged = false;
    if (selectedItem == null && cachedItem != null) {
      // Item was removed (switched to empty slot or non-tool)
      itemChanged = true;
    } else if (selectedItem != null && (selectedItem.item != cachedItem || selectedItem.meta != cachedMeta)) {
      // Item changed to different item
      itemChanged = true;
    }
    
    // Clean up mesh if item changed
    if (itemChanged) {
      if (toolMesh != null) {
        toolMesh.dispose();
        toolMesh = null;
      }
      cachedItem = selectedItem != null ? selectedItem.item : null;
      cachedMeta = selectedItem != null ? selectedItem.meta : 0;
    }
    
    // Only render if player has a tool selected
    if (selectedItem != null) {
      // Build mesh if needed (same approach as ItemEntityRenderer)
      if (toolMesh == null) {
        buildToolMesh(selectedItem);
      }
      
      // Update transform to position tool relative to player and rotate with player
      updateToolTransform(player, selectedItem);
      toolRenderable.worldTransform.set(toolTransform);
      
      // Set lighting based on player position
      toolRenderable.setLightOverride(player.position);
      
      renderables.add(toolRenderable);
    }
  }
  
  /**
   * Builds the tool mesh as 2 flat quads (like ItemEntityRenderer for items)
   * This is how Cubes renders tools/items - not as full cubes
   */
  private void buildToolMesh(ItemStack itemStack) {
    // Create mesh with 2 quads (front and back faces)
    toolMesh = new Mesh(false, 4 * 2, 6 * 2, CubesVertexAttributes.VERTEX_ATTRIBUTES);
    toolMesh.setIndices(toolIndices);
    
    toolVertices = new float[CubesVertexAttributes.COMPONENTS * 4 * 2];
    
    // Get item texture
    TextureRegion textureRegion = itemStack.getTextureRegion();
    
    // Check if this is an axe - if so, flip both faces horizontally
    boolean isAxe = false;
    if (itemStack.item instanceof ItemTool) {
      ItemTool tool = (ItemTool) itemStack.item;
      isAxe = tool.getToolType() == ItemTool.ToolType.axe;
    }
    
    TextureRegion frontTexture;
    TextureRegion backTexture;
    
    if (isAxe) {
      // For axe: flip both sides horizontally (so blade direction is consistent from both angles)
      frontTexture = new TextureRegion(textureRegion);
      frontTexture.flip(true, false);
      
      // Back texture should NOT be flipped for axe (since front is already flipped)
      backTexture = new TextureRegion(textureRegion);
    } else {
      // For other tools: front normal, back flipped (default ItemEntityRenderer behavior)
      frontTexture = textureRegion;
      
      backTexture = new TextureRegion(textureRegion);
      backTexture.flip(true, false);
    }
    
    int vertexOffset = 0;
    
    // Front face (MinZ)
    vertexOffset = FaceVertices.createMinZ(
      new Vector3(-TOOL_SIZE / 2f, 0f, 0f), 
      frontTexture, 
      null, 0, 0, 0, 
      FULL_LIGHT, 
      toolVertices, 
      vertexOffset
    );
    
    // Back face (MaxZ)
    vertexOffset = FaceVertices.createMaxZ(
      new Vector3(-TOOL_SIZE / 2f, 0f, -1f), 
      backTexture, 
      null, 0, 0, 0, 
      FULL_LIGHT, 
      toolVertices, 
      vertexOffset
    );
    
    toolMesh.setVertices(toolVertices);
    
    // Setup renderable
    toolRenderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
    toolRenderable.meshPart.offset = 0;
    toolRenderable.meshPart.size = 6 * 2;
    toolRenderable.meshPart.mesh = toolMesh;
    toolRenderable.material = ethanjones.cubes.graphics.assets.Assets.blockItemSheet.getMaterial();
    toolRenderable.name = "Tool - " + itemStack.item.id;
  }
  
  /**
   * Updates the tool transform matrix to position it at the player's hand
   * and rotate it to follow the player's facing direction
   */
  private void updateToolTransform(Player player, ItemStack itemStack) {
    // Start with identity
    toolTransform.idt();
    
    // Calculate player's facing direction (forward and right vectors)
    // Player angle is the direction they're looking
    float yaw = (float) Math.atan2(player.angle.x, player.angle.z);
    
    // Right vector (perpendicular to forward, for hand position)
    rightVector.set(
      (float) Math.cos(yaw),  // X component
      0f,                      // Y component (no vertical tilt for right vector)
      (float) -Math.sin(yaw)   // Z component
    );
    
    // Forward vector (where player is looking, for tool position in front)
    forwardVector.set(
      (float) Math.sin(yaw),
      0f,
      (float) Math.cos(yaw)
    );
    
    // Check if this is an axe for special positioning
    boolean isAxe = false;
    if (itemStack.item instanceof ItemTool) {
      ItemTool tool = (ItemTool) itemStack.item;
      isAxe = tool.getToolType() == ItemTool.ToolType.axe;
    }
    
    // Calculate tool position: player center + right offset + forward offset + up offset
    // For axe, use 0 forward offset instead of TOOL_OFFSET_FORWARD
    float forwardOffset = isAxe ? 0f : TOOL_OFFSET_FORWARD;
    
    Vector3 toolPosition = new Vector3(player.position);
    toolPosition.add(rightVector.x * TOOL_OFFSET_RIGHT, TOOL_OFFSET_UP, rightVector.z * TOOL_OFFSET_RIGHT);
    toolPosition.add(forwardVector.x * forwardOffset, 0f, forwardVector.z * forwardOffset);
    
    // Apply transformations in order: scale -> rotate -> translate
    toolTransform.scl(1.0f); // Tool at normal size (TOOL_SIZE is already in mesh)
    
    // Base rotation to match player's facing direction (Y-axis rotation)
    float baseRotation = (float) Math.toDegrees(yaw) + 90f;
    
    toolTransform.rotate(Vector3.Y, baseRotation);
    
    // Special case for axe: rotate 90 degrees around Z axis
    if (itemStack.item instanceof ItemTool) {
      ItemTool tool = (ItemTool) itemStack.item;
      if (tool.getToolType() == ItemTool.ToolType.axe) {
        toolTransform.rotate(Vector3.Z, 90f);
      }
    }
    
    // Optionally add slight tilt based on player's vertical look angle
    // This makes the tool tilt up/down slightly when player looks up/down
    float pitch = (float) Math.atan(player.angle.y);
    toolTransform.rotate(Vector3.X, (float) -Math.toDegrees(pitch) * 0.3f); // 30% of player's pitch
    
    // Set final position
    toolTransform.setTranslation(toolPosition);
  }
}
