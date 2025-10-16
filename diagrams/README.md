# Class Diagram Suite - OPP-Cubes Project

This document provides an overview of the class diagrams created for the OPP-Cubes project. Each diagram focuses on a specific system to make the architecture more understandable.

## 📊 Available Diagrams

### 1. **Core Architecture** (`01-core-architecture.puml`)
**Focus:** Foundation system - Client/Server architecture and platform abstraction
- **Side System** - Client/Server enumeration and management
- **Cubes Classes** - Abstract base class, CubesClient, CubesServer
- **Platform Abstraction** - Adapter pattern, ClientAdapter, ServerAdapter
- **Core Components** - Executor, EventBus, Event system
- **Key Classes:** ~15 core classes

### 2. **World System** (`02-world-system.puml`)  
**Focus:** World generation, storage, and management
- **Core World** - World, WorldClient, WorldServer
- **World Storage** - Area, AreaMap, Entities
- **World Generation** - TerrainGenerator implementations, GeneratorManager
- **World Threading** - WorldTasks, generation and save tasks
- **World Lighting** - Light calculation and propagation
- **World References** - Area and Block reference pools
- **Key Classes:** ~60 classes

### 3. **Graphics System** (`03-graphics-system.puml`)
**Focus:** Rendering, UI, and visual assets
- **Core Rendering** - Renderer, WorldRenderer, GuiRenderer
- **World Graphics** - AreaRenderer, AreaMesh, block rendering
- **Block Graphics** - Texture handling, face vertices, ambient occlusion
- **Asset Management** - Assets, AssetManager, texture packing
- **UI System** - Menu base classes, specific menus
- **HUD Components** - Hotbar, Chat, Inventory UI
- **Key Classes:** ~60 classes

### 4. **Networking System** (`04-networking-system.puml`)
**Focus:** Client/server communication and data exchange
- **Core Networking** - Networking abstract class, NetworkingManager
- **Client Networking** - ClientNetworking, connection handling
- **Server Networking** - ServerNetworking, client management
- **Singleplayer Networking** - Internal communication for singleplayer
- **Packet System** - Packet base class, priorities, directions
- **Packet Implementations** - All specific packet types (30+ packets)
- **Socket System** - SocketIO, input/output handling
- **Stream System** - Data stream utilities
- **Key Classes:** ~50 classes

## 🎯 **How to Use These Diagrams**

### For System Architecture Analysis:
1. **Start with Core Architecture** - Understand the client-server foundation
2. **Move to specific systems** based on your focus area
3. **Cross-reference between diagrams** for complete understanding

### For Development:
- **Core Architecture** - When modifying client/server logic
- **World System** - When working with world generation/storage
- **Graphics System** - When working with rendering/UI
- **Networking System** - When adding new packet types or networking features

### For Documentation:
- Use these as **reference documentation** for the codebase
- **Onboard new developers** with visual system overviews
- **Plan refactoring** by understanding current relationships

## 🔧 **Viewing the Diagrams**

### Option 1: PlantUML Extension (Recommended)
1. Open any `.puml` file in VS Code
2. Press `Alt+D` to preview the diagram
3. Use `Ctrl+Shift+P` → "PlantUML: Export Current Diagram" for images

### Option 2: Online PlantUML Server
1. Copy the diagram content from `.puml` files
2. Go to http://www.plantuml.com/plantuml/uml/
3. Paste and generate the diagram

### Option 3: Local PlantUML
```bash
# Install PlantUML locally
java -jar plantuml.jar diagrams/*.puml
```

## 📋 **Additional Systems Not Yet Diagrammed**

If you need diagrams for other systems, here are the remaining major components:

### 5. **Entity & Item System** (~20 classes)
- Entity hierarchy, Player, ItemEntity
- Item system, ItemStack, Inventory
- Crafting system, recipes

### 6. **Block System** (~15 classes)  
- Block types, Block data
- Block behaviors and properties

### 7. **Input System** (~10 classes)
- Input handling, controllers
- Keyboard/touch input

### 8. **Core Systems** (~30 classes)
- Mod system, Lua integration
- Settings system, Localization
- Logging, Performance monitoring

### 9. **Platform Modules** (~20 classes)
- Android, Desktop, Client, Server launchers
- Platform-specific compatibility

## 💡 **Tips for System Architecture Diagrams**

1. **Use these diagrams as starting points** for creating your "magic systems" architecture diagram
2. **Focus on the relationships** between major components rather than every detail
3. **Combine multiple systems** in your architecture diagram to show cross-system dependencies
4. **Highlight the main data flow** paths in your architecture representation

## 🎨 **Creating Your Magic Systems Diagram**

Based on these detailed diagrams, you can now create a high-level system architecture diagram that shows:

1. **Major System Boundaries** (from the 4 main diagrams)
2. **Key Data Flows** between systems
3. **Main Interfaces** and integration points
4. **Dependencies** and system relationships
5. **Client vs Server** components clearly separated

This approach gives you both the **detailed technical view** (individual system diagrams) and the ability to create a **high-level architectural overview** for documentation or presentation purposes.

---

**Next Steps:**
- Review each diagram to understand system boundaries
- Use these as reference when creating your consolidated architecture diagram
- Add additional system diagrams as needed for complete coverage