#!/usr/bin/env python3
"""
Generate multiple targeted PlantUML diagrams from OPP-Cubes codebase
Creates separate accurate diagrams for each major subsystem
"""

import subprocess
import os
from pathlib import Path

def run_parser(package, title, max_classes=15):
    """Run the enhanced parser for a specific package"""
    cmd = [
        'python', 'enhanced_java_parser.py', '../core/src',
        '--package', package,
        '--title', title,
        '--max-classes', str(max_classes)
    ]
    
    print(f"Generating diagram for {title}...")
    result = subprocess.run(cmd, capture_output=True, text=True)
    
    if result.returncode == 0:
        print(f"✓ Successfully generated {title}")
        return True
    else:
        print(f"✗ Error generating {title}: {result.stderr}")
        return False

def main():
    # Change to diagrams directory
    os.chdir(Path(__file__).parent)
    
    # Define subsystems to generate diagrams for
    subsystems = [
        # Core systems
        ("ethanjones.cubes.core.system", "Core System Architecture", 15),
        ("ethanjones.cubes.core.event", "Event System", 12), 
        ("ethanjones.cubes.core.id", "ID Management System", 10),
        ("ethanjones.cubes.core.logging", "Logging System", 8),
        ("ethanjones.cubes.core.settings", "Settings Management", 6),
        
        # World system
        ("ethanjones.cubes.world", "World System", 20),
        ("ethanjones.cubes.world.storage", "World Storage", 8),
        ("ethanjones.cubes.world.generator", "World Generation", 10),
        ("ethanjones.cubes.world.thread", "World Threading", 8),
        ("ethanjones.cubes.world.collision", "Collision System", 6),
        
        # Block and item systems
        ("ethanjones.cubes.block", "Block System", 12),
        ("ethanjones.cubes.item", "Item System", 10),
        
        # Graphics and rendering
        ("ethanjones.cubes.graphics", "Graphics System", 15),
        ("ethanjones.cubes.graphics.rendering", "Rendering Engine", 12),
        ("ethanjones.cubes.graphics.world", "World Rendering", 10),
        
        # Input and networking
        ("ethanjones.cubes.input", "Input System", 8),
        ("ethanjones.cubes.networking", "Networking System", 12),
    ]
    
    print("Generating accurate PlantUML diagrams from Java source code...")
    print("=" * 60)
    
    successful = 0
    total = len(subsystems)
    
    for package, title, max_classes in subsystems:
        if run_parser(package, title, max_classes):
            successful += 1
        print()
    
    print(f"Summary: {successful}/{total} diagrams generated successfully")
    
    # List generated files
    print("\nGenerated files:")
    for puml_file in Path('.').glob('ethanjones_*.puml'):
        print(f"  - {puml_file.name}")

if __name__ == "__main__":
    main()