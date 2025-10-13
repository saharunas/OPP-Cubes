#!/usr/bin/env python3
"""
Enhanced PlantUML generator that creates associations based on class attributes
"""

import os
import re
from pathlib import Path
from fixed_java_parser import FixedJavaParser

class AssociationGenerator:
    def __init__(self):
        self.known_classes = set()
        self.associations = []
        
    def generate_associations_from_attributes(self, classes):
        """Generate associations based on class attributes"""
        # Build set of known class names
        for java_class in classes:
            self.known_classes.add(java_class.name)
        
        # Generate associations from attributes
        for java_class in classes:
            for attr in java_class.attributes:
                self.analyze_attribute_for_association(java_class, attr)
        
        return self.associations
    
    def analyze_attribute_for_association(self, source_class, attribute):
        """Analyze an attribute to determine if it should create an association"""
        attr_type = attribute.type
        
        # Clean up type (remove arrays, generics, etc.)
        clean_type = self.extract_base_type(attr_type)
        
        if clean_type in self.known_classes:
            # Determine association type based on attribute characteristics
            if attribute.is_final and not attribute.is_static:
                # Final instance attributes suggest composition
                assoc_type = "composition"
                symbol = "*--"
            elif self.is_collection_type(attr_type):
                # Collections suggest aggregation
                assoc_type = "aggregation"  
                symbol = "o--"
            else:
                # Regular reference suggests association
                assoc_type = "association"
                symbol = "-->"
            
            association = {
                'source': source_class.name,
                'target': clean_type,
                'type': assoc_type,
                'symbol': symbol,
                'attribute': attribute.name
            }
            
            self.associations.append(association)
    
    def extract_base_type(self, type_str):
        """Extract the base class name from complex types"""
        if not type_str:
            return ""
            
        # Remove array notation
        type_str = type_str.replace('[]', '')
        
        # Remove generic parameters
        if '<' in type_str:
            type_str = type_str.split('<')[0]
        
        # Handle nested generics like List<Map<String, Area>>
        type_str = re.sub(r'<[^>]*>', '', type_str)
        
        # Clean up whitespace
        type_str = type_str.strip()
        
        # Handle fully qualified names
        if '.' in type_str:
            type_str = type_str.split('.')[-1]
        
        return type_str
    
    def is_collection_type(self, type_str):
        """Check if type is a collection type"""
        collection_types = [
            'List', 'ArrayList', 'LinkedList', 'Vector',
            'Set', 'HashSet', 'TreeSet', 'LinkedHashSet',
            'Map', 'HashMap', 'TreeMap', 'LinkedHashMap', 'LongMap',
            'Collection', 'Queue', 'Deque', 'ArrayDeque',
            'ConcurrentLinkedQueue', 'LinkedBlockingQueue',
            'AtomicReference'
        ]
        
        for coll_type in collection_types:
            if coll_type in type_str:
                return True
        
        # Check for array notation
        if '[]' in type_str:
            return True
            
        return False

class EnhancedPlantUMLGenerator:
    def __init__(self):
        self.parser = FixedJavaParser()
        self.association_gen = AssociationGenerator()
    
    def generate_plantuml_with_associations(self, classes, title="Class Diagram"):
        """Generate PlantUML with automatically generated associations"""
        puml = f"@startuml {title.replace(' ', '_')}\n"
        puml += "!theme plain\n"
        puml += f"title {title}\n\n"
        
        # Generate class definitions
        for java_class in classes:
            if java_class.is_interface:
                puml += f"    interface {java_class.name} {{\n"
            elif java_class.is_enum:
                puml += f"    enum {java_class.name} {{\n"
                # Add enum values
                for value in java_class.enum_values:
                    puml += f"        {value}\n"
                puml += "    }\n"
                continue
            elif java_class.is_abstract:
                puml += f"    abstract class {java_class.name} {{\n"
            else:
                puml += f"    class {java_class.name} {{\n"
            
            # Add attributes
            for attr in java_class.attributes:
                visibility_symbol = self.get_visibility_symbol(attr.visibility)
                static_modifier = "{static} " if attr.is_static else ""
                final_modifier = "{readonly} " if attr.is_final else ""
                puml += f"        {visibility_symbol}{static_modifier}{final_modifier}{attr.name} : {attr.type}\n"
            
            if java_class.attributes:
                puml += "        --\n"
            
            # Add constructors
            for constructor in java_class.constructors:
                visibility_symbol = self.get_visibility_symbol(constructor.visibility)
                params_str = ", ".join([f"{p['name']} : {p['type']}" for p in constructor.parameters])
                puml += f"        {visibility_symbol}{constructor.name}({params_str}) : void\n"
            
            # Add methods
            for method in java_class.methods:
                visibility_symbol = self.get_visibility_symbol(method.visibility)
                static_modifier = "{static} " if method.is_static else ""
                abstract_modifier = "{abstract} " if method.is_abstract else ""
                params_str = ", ".join([f"{p['name']} : {p['type']}" for p in method.parameters])
                puml += f"        {visibility_symbol}{static_modifier}{abstract_modifier}{method.name}({params_str}) : {method.return_type}\n"
            
            puml += "    }\n"
        
        # Generate inheritance relationships
        for java_class in classes:
            if java_class.extends:
                puml += f"    {java_class.name} --|> {java_class.extends}\n"
            
            for interface in java_class.implements:
                puml += f"    {java_class.name} ..|> {interface}\n"
        
        # Generate associations based on attributes
        associations = self.association_gen.generate_associations_from_attributes(classes)
        
        puml += "\n    ' Associations based on class attributes\n"
        for assoc in associations:
            puml += f"    {assoc['source']} {assoc['symbol']} {assoc['target']} : {assoc['attribute']}\n"
        
        puml += "@enduml\n"
        return puml
    
    def get_visibility_symbol(self, visibility):
        """Convert visibility to PlantUML symbol"""
        symbols = {
            'public': '+',
            'private': '-',
            'protected': '#',
            'package': '~'
        }
        return symbols.get(visibility, '+')

def main():
    """Generate world system with proper associations"""
    generator = EnhancedPlantUMLGenerator()
    
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
                classes = generator.parser.parse_java_file(str(java_file))
                if classes:
                    all_classes.extend(classes)
    
    print(f"\nParsed {len(all_classes)} classes total")
    
    # Generate PlantUML with associations
    if all_classes:
        puml = generator.generate_plantuml_with_associations(all_classes, "World System with Associations")
        
        # Write to file
        output_file = "world_system_with_associations.puml"
        with open(output_file, "w", encoding="utf-8") as f:
            f.write(puml)
        
        print(f"✓ Generated PlantUML with associations: {output_file}")
        
        # Show summary
        associations = generator.association_gen.associations
        print(f"\nSummary:")
        print(f"  Classes: {len([c for c in all_classes if not c.is_interface and not c.is_enum])}")
        print(f"  Interfaces: {len([c for c in all_classes if c.is_interface])}")
        print(f"  Enums: {len([c for c in all_classes if c.is_enum])}")
        print(f"  Generated Associations: {len(associations)}")
        
        # Show sample associations
        print(f"\nSample associations (first 10):")
        for i, assoc in enumerate(associations[:10]):
            print(f"  {assoc['source']} {assoc['symbol']} {assoc['target']} : {assoc['attribute']}")
    else:
        print("No classes found!")

if __name__ == "__main__":
    main()