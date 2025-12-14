package ethanjones.cubes.graphics.world.block;

import ethanjones.cubes.core.system.CubesException;
import ethanjones.cubes.core.util.BlockFace;
import ethanjones.cubes.graphics.assets.Assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;

/**
 * Flyweight-friendly texture handler.
 *
 * IMPORTANT: This class is now immutable.
 * Use {@link BlockTextureHandlers} (the Flyweight factory) to obtain shared instances.
 */
public final class BlockTextureHandler {

  final TextureRegion[] textureRegions;

  /** Package-private: use BlockTextureHandlers. */
  BlockTextureHandler(TextureRegion[] regions) {
    if (regions == null || regions.length != 6) throw new CubesException("Texture region array must be length 6");
    for (TextureRegion r : regions) {
      if (r == null) throw new CubesException("Texture region cannot be null");
    }
    this.textureRegions = regions;
  }

  /** Convenience: all faces use the same texture id. Prefer BlockTextureHandlers.uniform(id). */
  public static BlockTextureHandler uniform(String id) {
    return BlockTextureHandlers.uniform(id);
  }

  /** Convenience: create from a single region (all sides). Prefer BlockTextureHandlers.uniform(region). */
  public static BlockTextureHandler uniform(TextureRegion textureRegion) {
    return BlockTextureHandlers.uniform(textureRegion);
  }

  public TextureRegion getSide(BlockFace blockFace) {
    return getSide(blockFace == null ? 0 : blockFace.index);
  }

  public TextureRegion getSide(int direction) {
    return textureRegions[direction];
  }

  /**
   * Returns a NEW handler with one face changed (does not mutate this instance).
   * Prefer using {@link BlockTextureHandlers#withSide(BlockTextureHandler, BlockFace, String)}.
   */
  public BlockTextureHandler withSide(BlockFace blockFace, String id) {
    return BlockTextureHandlers.withSide(this, blockFace, id);
  }

  /** Legacy API kept for compatibility; now returns a NEW handler (does not mutate). */
  @Deprecated
  public BlockTextureHandler setSide(BlockFace blockFace, String id) {
    return withSide(blockFace, id);
  }

  /** Legacy API kept for compatibility; now returns a NEW handler (does not mutate). */
  @Deprecated
  public BlockTextureHandler setSide(int blockFace, String id) {
    return withSide(BlockFace.values()[blockFace], id);
  }

  /** Helper for factory: create a uniform region array. */
  static TextureRegion[] uniformRegions(TextureRegion textureRegion) {
    if (textureRegion == null) throw new CubesException("Texture region cannot be null");
    TextureRegion[] regions = new TextureRegion[6];
    Arrays.fill(regions, textureRegion);
    return regions;
  }

  /** Helper for factory: resolve id -> region. */
  static TextureRegion resolve(String id) {
    return Assets.getBlockItemTextureRegion(id, "block");
  }
}
