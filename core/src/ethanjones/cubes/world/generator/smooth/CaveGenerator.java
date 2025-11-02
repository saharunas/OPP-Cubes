package ethanjones.cubes.world.generator.smooth;

import ethanjones.cubes.world.reference.AreaReference;
import ethanjones.cubes.world.reference.BlockReference;
import ethanjones.cubes.world.storage.Area;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.math.RandomXS128;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CaveGenerator implements Cloneable {

    public static final int roomNodesMin = 25;
    public static final int roomNodesMax = 100;

    private final int caveStartX;
    private final int caveStartY;
    private final int caveStartZ;

    private final SmoothWorld smoothWorld;
    private final RandomXS128 numbers;
    private final IntSet intSet;

    // === Prototypes ===
    private final RoomNode roomPrototype;
    private final TunnelNode tunnelPrototype;

    // === Data ===
    private final HashMap<AreaReference, IntArray> blocks = new HashMap<>();
    private final ArrayList<RoomNode> rooms = new ArrayList<>();
    private final ArrayList<TunnelNode> tunnels = new ArrayList<>();

    // === Constructor ===
    public CaveGenerator(int x, int z, SmoothWorld smoothWorld) {
        this.caveStartX = x;
        this.caveStartY = smoothWorld.getSurfaceHeight(x, z);
        this.caveStartZ = z;
        this.smoothWorld = smoothWorld;

        long seedOffset = x + z + (x * (x - 1)) + (z * (z + 1)) + (long) Math.pow(x, z > 0 ? z : (z < 0 ? -z : 1));
        this.numbers = new RandomXS128(smoothWorld.baseSeed,
                SmoothWorld.murmurHash3(smoothWorld.baseSeed + SmoothWorld.murmurHash3(seedOffset)));
        this.intSet = new IntSet(roomNodesMax);

        // === Prototype instances ===
        this.roomPrototype = new RoomNode(new BlockReference().setFromBlockCoordinates(x, caveStartY, z));
        this.tunnelPrototype = new TunnelNode(
                new BlockReference().setFromBlockCoordinates(x, caveStartY, z),
                new BlockReference().setFromBlockCoordinates(x + 10, caveStartY, z)
        );
    }

    // === Main entry point ===
    public Cave generate() {
        generateNodes();
        calculateBlocks();

        // Build cave block map
        HashMap<AreaReference, int[]> caveBlocks = new HashMap<>();
        for (Map.Entry<AreaReference, IntArray> entry : blocks.entrySet()) {
            caveBlocks.put(entry.getKey(), entry.getValue().toArray());
        }

        return new Cave(caveStartX, caveStartY, caveStartZ, caveBlocks);
    }

    // === Node generation (Prototype logic) ===
    private void generateNodes() {
        // Randomized spherical rooms using deep-copied prototypes
        for (int i = 0; i < 5; i++) {
            RoomNode room = roomPrototype.deepCopy();
            int offsetX = (numbers.nextInt(40) - 20);
            int offsetZ = (numbers.nextInt(40) - 20);
            int offsetY = (numbers.nextInt(8) - 4);
            room.location.offset(offsetX, offsetY, offsetZ);
            room.size = 3 + numbers.nextInt(3);
            rooms.add(room);
        }

        // Connect rooms with tunnels using deep copies
        for (int i = 0; i < rooms.size() - 1; i++) {
            RoomNode a = rooms.get(i);
            RoomNode b = rooms.get(i + 1);

            // Main tunnel (deep copy ensures independent geometry)
            TunnelNode mainTunnel = tunnelPrototype.deepCopy()
                    .withEnds(a.location.copy(), b.location.copy());
            mainTunnel.startRadius = 1.5f + numbers.nextFloat();
            mainTunnel.endRadius = 1.5f + numbers.nextFloat();
            tunnels.add(mainTunnel);

            // Occasionally create a smaller parallel tunnel (shallow copy)
            if (numbers.nextFloat() < 0.3f) {
                TunnelNode sideTunnel = mainTunnel.shallowCopy();
                sideTunnel.startRadius *= 0.6f;
                sideTunnel.endRadius *= 0.6f;
                tunnels.add(sideTunnel);
            }
        }
    }

    // === Calculate cleared blocks in caves (restored realism) ===
    private void calculateBlocks() {
        // --- Spherical rooms ---
        for (RoomNode room : rooms) {
            int roomX = room.location.blockX;
            int roomY = room.location.blockY;
            int roomZ = room.location.blockZ;
            int r = room.size;
            int r2 = r * r;

            for (int x = roomX - r; x <= roomX + r; x++) {
                for (int y = roomY - r; y <= roomY + r; y++) {
                    for (int z = roomZ - r; z <= roomZ + r; z++) {
                        int dx = roomX - x;
                        int dy = roomY - y;
                        int dz = roomZ - z;
                        if (dx * dx + dy * dy + dz * dz <= r2) {
                            clear(x, y, z);
                        }
                    }
                }
            }
        }

        // --- Curved tunnels ---
        for (TunnelNode tunnel : tunnels) {
            BlockReference start = tunnel.start;
            BlockReference end = tunnel.end;

            int x1 = Math.min(start.blockX, end.blockX);
            int x2 = Math.max(start.blockX, end.blockX);
            int y1 = Math.min(start.blockY, end.blockY);
            int y2 = Math.max(start.blockY, end.blockY);
            int z1 = Math.min(start.blockZ, end.blockZ);
            int z2 = Math.max(start.blockZ, end.blockZ);

            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    for (int z = z1; z <= z2; z++) {
                        float a = distance(tunnel.start, x, y, z);
                        float b = distance(tunnel.end, x, y, z);
                        float c = distance(tunnel.start, tunnel.end);

                        float s = (a + b + c) / 2f;
                        float area = (float) Math.sqrt(Math.max(s * (s - a) * (s - b) * (s - c), 0));
                        float distFromLine = (area * 2f) / Math.max(c, 0.001f);

                        float radius = (a < b) ? tunnel.startRadius : tunnel.endRadius;
                        if (distFromLine <= radius) {
                            clear(x, y, z);
                        }
                    }
                }
            }
        }
    }

    // === Helper to clear a block ===
    private void clear(int blockX, int blockY, int blockZ) {
        if (blockY <= 0) return;
        AreaReference ref = new AreaReference().setFromBlockCoordinates(blockX, blockZ);
        IntArray array = blocks.get(ref);
        if (array == null) {
            array = new IntArray();
            blocks.put(ref, array);
        }
        array.add(Area.getRef(blockX - ref.minBlockX(), blockY, blockZ - ref.minBlockZ()));
    }

    // === Utility distance helpers ===
    private static float distance(BlockReference a, BlockReference b) {
        int dX = a.blockX - b.blockX;
        int dY = a.blockY - b.blockY;
        int dZ = a.blockZ - b.blockZ;
        return (float) Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    private static float distance(BlockReference a, int x2, int y2, int z2) {
        int dX = a.blockX - x2;
        int dY = a.blockY - y2;
        int dZ = a.blockZ - z2;
        return (float) Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    // === Prototype-based nested classes ===
    private static class RoomNode implements Cloneable {
        BlockReference location;
        int size = 3;

        public RoomNode(BlockReference location) {
            this.location = location;
        }

        public RoomNode deepCopy() {
            RoomNode copy = new RoomNode(location.copy());
            copy.size = this.size;
            return copy;
        }

        public RoomNode withLocation(BlockReference newLoc) {
            this.location = newLoc;
            return this;
        }
    }

    private static class TunnelNode implements Cloneable {
        BlockReference start, end;
        float startRadius = 2f;
        float endRadius = 2f;

        public TunnelNode(BlockReference start, BlockReference end) {
            this.start = start;
            this.end = end;
        }

        /** 🔹 Deep copy — duplicates coordinates */
        public TunnelNode deepCopy() {
            TunnelNode copy = new TunnelNode(start.copy(), end.copy());
            copy.startRadius = this.startRadius;
            copy.endRadius = this.endRadius;
            return copy;
        }

        /** 🔹 Shallow copy — shares same start/end references but copies radii */
        public TunnelNode shallowCopy() {
            TunnelNode copy = new TunnelNode(this.start, this.end);
            copy.startRadius = this.startRadius;
            copy.endRadius = this.endRadius;
            return copy;
        }

        public TunnelNode withEnds(BlockReference newStart, BlockReference newEnd) {
            this.start = newStart;
            this.end = newEnd;
            return this;
        }
    }
}