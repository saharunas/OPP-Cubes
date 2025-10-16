package ethanjones.cubes.block.factories;

import java.util.HashMap;
import java.util.Map;
import ethanjones.cubes.block.BlockFactory;
import ethanjones.cubes.block.Block;

public class BlockFactoryRegistry {
    private static final Map<String, BlockFactory> registry = new HashMap<>();

    public static void register(String id, BlockFactory factory) {
        registry.put(id, factory);
    }

    public static Block create(String id) {
        BlockFactory factory = registry.get(id);
        if (factory == null) throw new IllegalArgumentException("Unknown block ID: " + id);
        return factory.createBlock();
    }
}