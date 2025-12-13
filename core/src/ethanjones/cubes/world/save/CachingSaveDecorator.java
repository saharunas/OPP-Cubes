package ethanjones.cubes.world.save;

import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.networking.server.ClientIdentifier;
import ethanjones.cubes.world.generator.smooth.Cave;
import ethanjones.cubes.world.reference.AreaReference;
import ethanjones.cubes.world.storage.Area;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Pattern: Decorator (ConcreteDecorator - Level 2)
 * 
 * Adds caching functionality to save operations.
 * Caches recently accessed areas and caves in memory to avoid disk I/O.
 * 
 * This decorator provides:
 * - LRU (Least Recently Used) cache for areas
 * - LRU cache for caves
 * - Significant performance improvement for frequently accessed data
 * - Reduced disk I/O operations
 */
public class CachingSaveDecorator extends SaveDecorator {
    
    private final Map<String, Area> areaCache;
    private final Map<String, Cave> caveCache;
    private final Map<UUID, Player> playerCache;
    private final int maxCacheSize;
    
    /**
     * Create a caching decorator with specified cache size
     * @param wrappedSave The save to wrap
     * @param maxCacheSize Maximum number of entries per cache (areas, caves, players)
     */
    public CachingSaveDecorator(SaveInterface wrappedSave, int maxCacheSize) {
        super(wrappedSave);
        this.maxCacheSize = maxCacheSize;
        
        // Create LRU caches using LinkedHashMap
        final int maxSize = maxCacheSize; // Java 7 requires final for inner class access
        
        this.areaCache = new LinkedHashMap<String, Area>(maxCacheSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Area> eldest) {
                return size() > maxSize;
            }
        };
        
        this.caveCache = new LinkedHashMap<String, Cave>(maxCacheSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Cave> eldest) {
                return size() > maxSize;
            }
        };
        
        this.playerCache = new LinkedHashMap<UUID, Player>(maxCacheSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<UUID, Player> eldest) {
                return size() > maxSize;
            }
        };
    }
    
    /**
     * Create a caching decorator with default cache size of 50
     * @param wrappedSave The save to wrap
     */
    public CachingSaveDecorator(SaveInterface wrappedSave) {
        this(wrappedSave, 50);
    }
    
    private String getAreaKey(int x, int z) {
        return x + "_" + z;
    }
    
    private String getCaveKey(AreaReference areaReference) {
        return areaReference.areaX + "_" + areaReference.areaZ;
    }
    
    @Override
    public synchronized boolean writeArea(Area area) {
        boolean result = super.writeArea(area);
        if (result) {
            // Update cache with the written area
            String key = getAreaKey(area.areaX, area.areaZ);
            areaCache.put(key, area);
        }
        return result;
    }
    
    @Override
    public synchronized Area readArea(int x, int z) {
        String key = getAreaKey(x, z);
        
        // Check cache first
        Area cached = areaCache.get(key);
        if (cached != null) {
            return cached;
        }
        
        // Cache miss - read from wrapped save
        Area area = super.readArea(x, z);
        if (area != null) {
            areaCache.put(key, area);
        }
        return area;
    }
    
    @Override
    public synchronized void writeCave(AreaReference areaReference, Cave cave) {
        super.writeCave(areaReference, cave);
        // Update cache
        String key = getCaveKey(areaReference);
        caveCache.put(key, cave);
    }
    
    @Override
    public synchronized Cave readCave(AreaReference areaReference) {
        String key = getCaveKey(areaReference);
        
        // Check cache first
        Cave cached = caveCache.get(key);
        if (cached != null) {
            return cached;
        }
        
        // Cache miss - read from wrapped save
        Cave cave = super.readCave(areaReference);
        if (cave != null) {
            caveCache.put(key, cave);
        }
        return cave;
    }
    
    @Override
    public synchronized void writePlayer(Player player) {
        super.writePlayer(player);
        // Update cache
        playerCache.put(player.uuid, player);
    }
    
    @Override
    public synchronized Player readPlayer(UUID uuid, ClientIdentifier clientIdentifier) {
        // Check cache first
        Player cached = playerCache.get(uuid);
        if (cached != null) {
            return cached;
        }
        
        // Cache miss - read from wrapped save
        Player player = super.readPlayer(uuid, clientIdentifier);
        if (player != null) {
            playerCache.put(uuid, player);
        }
        return player;
    }
    
    /**
     * Clear all caches
     */
    public synchronized void clearCache() {
        areaCache.clear();
        caveCache.clear();
        playerCache.clear();
    }
    
    /**
     * Get cache statistics
     * @return String containing cache sizes
     */
    public String getCacheStats() {
        return String.format("Cache stats - Areas: %d/%d, Caves: %d/%d, Players: %d/%d",
                areaCache.size(), maxCacheSize,
                caveCache.size(), maxCacheSize,
                playerCache.size(), maxCacheSize);
    }
}
