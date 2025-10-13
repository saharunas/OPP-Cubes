#!/usr/bin/env python3
"""
Generate corrected world system PlantUML using the fixed parser
"""

import os
from pathlib import Path
from fixed_java_parser import FixedJavaParser

def main():
    """Generate corrected world system PlantUML"""
    parser = FixedJavaParser()
    
    # World system related packages
    world_packages = [
        "core/src/ethanjones/cubes/world",
        "core/src/ethanjones/cubes/world/storage", 
        "core/src/ethanjones/cubes/world/save",
        "core/src/ethanjones/cubes/world/light",
        "core/src/ethanjones/cubes/world/thread",
        "core/src/ethanjones/cubes/world/collision",
        "core/src/ethanjones/cubes/world/generator",
        "core/src/ethanjones/cubes/world/reference"
    ]
    
    all_classes = []
    
    # Parse all world-related Java files
    base_path = Path("C:/Users/as/Desktop/OPP-Cubes")
    
    for package in world_packages:
        package_path = base_path / package
        if package_path.exists():
            print(f"Parsing package: {package}")
            for java_file in package_path.rglob("*.java"):
                print(f"  Processing: {java_file.name}")
                classes = parser.parse_java_file(str(java_file))
                if classes:
                    all_classes.extend(classes)
    
    print(f"\nParsed {len(all_classes)} classes total")
    
    # Generate corrected PlantUML
    if all_classes:
        puml = parser.generate_plantuml_for_classes(all_classes, "World System - Corrected")
        
        # Write to file
        output_file = "world_system_corrected.puml"
        with open(output_file, "w", encoding="utf-8") as f:
            f.write(puml)
        
        print(f"✓ Generated corrected PlantUML: {output_file}")
        
        # Show summary
        print(f"\nSummary:")
        print(f"  Classes: {len([c for c in all_classes if not c.is_interface and not c.is_enum])}")
        print(f"  Interfaces: {len([c for c in all_classes if c.is_interface])}")
        print(f"  Enums: {len([c for c in all_classes if c.is_enum])}")
        
        # Show sample attributes for verification
        print(f"\nSample class attributes (first 3 classes):")
        for i, java_class in enumerate(all_classes[:3]):
            print(f"  {java_class.name}: {len(java_class.attributes)} attributes")
            for attr in java_class.attributes[:3]:
                print(f"    - {attr.name} : {attr.type}")
    else:
        print("No classes found!")

if __name__ == "__main__":
    main()