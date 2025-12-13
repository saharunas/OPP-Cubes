package ethanjones.cubes.graphics.world.block;

import ethanjones.cubes.core.util.BlockFace;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Flyweight factory for {@link BlockTextureHandler}.
 *
 * Intrinsic state: the 6 {@link TextureRegion} references (one per face).
 */
public final class BlockTextureHandlers {

  private BlockTextureHandlers() {}

  private static final ConcurrentHashMap<Key, BlockTextureHandler> CACHE = new ConcurrentHashMap<>();

  public static BlockTextureHandler uniform(String id) {
    return uniform(BlockTextureHandler.resolve(id));
  }

  public static BlockTextureHandler uniform(TextureRegion region) {
    TextureRegion[] regions = BlockTextureHandler.uniformRegions(region);
    return getOrCreate(regions);
  }

  public static BlockTextureHandler withSide(BlockTextureHandler base, BlockFace face, String id) {
    return withSide(base, face, BlockTextureHandler.resolve(id));
  }

  public static BlockTextureHandler withSide(BlockTextureHandler base, BlockFace face, TextureRegion region) {
    TextureRegion[] next = Arrays.copyOf(base.textureRegions, 6);
    next[face.index] = region;
    return getOrCreate(next);
  }

  private static BlockTextureHandler getOrCreate(TextureRegion[] regions) {
    Key k = new Key(regions);
    BlockTextureHandler existing = CACHE.get(k);
    if (existing != null) return existing;

    // copy so callers can't keep a reference and mutate it later
    TextureRegion[] stored = Arrays.copyOf(regions, 6);
    BlockTextureHandler created = new BlockTextureHandler(stored);

    BlockTextureHandler raced = CACHE.putIfAbsent(new Key(stored), created);
    return raced != null ? raced : created;
  }

  /** Key uses reference equality of TextureRegion objects. */
  private static final class Key {
    private final TextureRegion[] r;
    private final int hash;

    Key(TextureRegion[] regions) {
      this.r = regions;
      int h = 1;
      for (int i = 0; i < 6; i++) {
        h = 31 * h + System.identityHashCode(regions[i]);
      }
      this.hash = h;
    }

    @Override public int hashCode() { return hash; }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof Key)) return false;
      Key other = (Key) obj;
      for (int i = 0; i < 6; i++) {
        if (this.r[i] != other.r[i]) return false;
      }
      return true;
    }
  }
}
