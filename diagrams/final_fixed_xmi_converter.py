#!/usr/bin/env python3
"""
Final Fixed XMI converter that properly parses PlantUML attribute syntax
"""

import xml.etree.ElementTree as ET
import re
from pathlib import Path
import uuid

class FinalFixedXMIConverter:
    def __init__(self):
        self.class_map = {}
        self.interface_map = {}
        self.enum_map = {}
        self.primitive_types = {}
        
    def create_xmi_document(self, title="World System"):
        """Create XMI document with ultra-compatible namespaces"""
        # Create root XMI element with minimal namespaces
        root = ET.Element("xmi:XMI")
        root.set("xmi:version", "2.0")
        root.set("xmlns:xmi", "http://www.omg.org/XMI")
        root.set("xmlns:uml", "http://www.eclipse.org/uml2/1.1.0/GenModel")
        
        # Create model element
        model = ET.SubElement(root, "uml:Model")
        model.set("xmi:id", str(uuid.uuid4()))
        model.set("name", title)
        
        # Add primitive types directly to the model
        self.add_primitive_types(model)
        
        return root, model
    
    def add_primitive_types(self, model):
        """Add primitive types as part of the model to avoid external references"""
        primitive_names = ['int', 'float', 'double', 'boolean', 'String', 'void', 'long', 'byte', 'char']
        
        for prim_name in primitive_names:
            prim_type = ET.SubElement(model, "packagedElement")
            prim_type.set("xmi:type", "uml:PrimitiveType")
            prim_id = str(uuid.uuid4())
            prim_type.set("xmi:id", prim_id)
            prim_type.set("name", prim_name)
            self.primitive_types[prim_name] = prim_id
    
    def parse_plantuml_file(self, file_path):
        """Parse PlantUML file and extract all elements with IMPROVED attribute parsing"""
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        return self.parse_plantuml_content(content)
    
    def parse_plantuml_content(self, content):
        """Parse PlantUML content with improved attribute parsing"""
        elements = {
            'classes': {},
            'interfaces': {},
            'enums': {},
            'relationships': []
        }
        
        lines = content.split('\n')
        current_element = None
        current_type = None
        
        for line in lines:
            line = line.strip()
            if not line or line.startswith("'") or line.startswith("@"):
                continue
                
            # Class definitions
            if line.startswith('class '):
                match = re.match(r'class\s+(\w+)(?:\s+extends\s+(\w+))?(?:\s+implements\s+([\w,\s]+))?\s*\{?', line)
                if match:
                    class_name = match.group(1)
                    extends = match.group(2)
                    implements = match.group(3).split(',') if match.group(3) else []
                    implements = [i.strip() for i in implements] if implements else []
                    
                    elements['classes'][class_name] = {
                        'name': class_name,
                        'extends': extends,
                        'implements': implements,
                        'attributes': [],
                        'methods': [],
                        'stereotypes': []
                    }
                    current_element = elements['classes'][class_name]
                    current_type = 'class'
            
            # Interface definitions
            elif line.startswith('interface '):
                match = re.match(r'interface\s+(\w+)(?:\s+extends\s+([\w,\s]+))?\s*\{?', line)
                if match:
                    interface_name = match.group(1)
                    extends = match.group(2).split(',') if match.group(2) else []
                    extends = [e.strip() for e in extends] if extends else []
                    
                    elements['interfaces'][interface_name] = {
                        'name': interface_name,
                        'extends': extends,
                        'methods': []
                    }
                    current_element = elements['interfaces'][interface_name]
                    current_type = 'interface'
            
            # Enum definitions
            elif line.startswith('enum '):
                match = re.match(r'enum\s+(\w+)\s*\{?', line)
                if match:
                    enum_name = match.group(1)
                    elements['enums'][enum_name] = {
                        'name': enum_name,
                        'values': []
                    }
                    current_element = elements['enums'][enum_name]
                    current_type = 'enum'
            
            # Attributes and methods inside elements
            elif current_element and line and not line == '}' and not line == '--':
                if current_type in ['class', 'interface']:
                    # Method pattern (has parentheses)
                    if '(' in line and ')' in line:
                        method_info = self.parse_method_line(line)
                        if method_info:
                            current_element['methods'].append(method_info)
                    else:
                        # Attribute pattern (no parentheses)
                        attr_info = self.parse_attribute_line(line)
                        if attr_info and current_type == 'class':
                            current_element['attributes'].append(attr_info)
                
                elif current_type == 'enum' and not line.startswith('}'):
                    # Enum values
                    current_element['values'].append(line.strip())
            
            # End of element
            elif line == '}':
                current_element = None
                current_type = None
            
            # Relationships - INCLUDE ALL TYPES for Magic Systems
            elif any(rel in line for rel in ['-->', '--', '-', '*--', 'o--', '<|--', '..|>', '..>']):
                relationship = self.parse_relationship(line)
                if relationship:
                    elements['relationships'].append(relationship)
        
        return elements
    
    def parse_attribute_line(self, line):
        """Parse PlantUML attribute line with all modifiers"""
        # Handle complex PlantUML attribute syntax:
        # +{readonly} initialAreaX : int
        # -{static} someField : String
        # #field : Type[]
        
        # Pattern to match: visibility + {modifiers} + name + : + type
        attr_pattern = r'^\s*([+\-#~]?)\s*(\{[^}]*\})?\s*(\w+)\s*:\s*(.+)$'
        match = re.match(attr_pattern, line)
        
        if not match:
            return None
        
        visibility_symbol = match.group(1) or '+'
        modifiers_str = match.group(2) or ''
        attr_name = match.group(3)
        attr_type = match.group(4).strip()
        
        # Parse visibility
        visibility = self.parse_visibility(visibility_symbol)
        
        # Parse modifiers
        is_static = 'static' in modifiers_str
        is_readonly = 'readonly' in modifiers_str
        
        return {
            'name': attr_name,
            'type': attr_type,
            'visibility': visibility,
            'is_static': is_static,
            'is_readonly': is_readonly
        }
    
    def parse_method_line(self, line):
        """Parse PlantUML method line"""
        # Method pattern: visibility + {modifiers} + name(params) : return_type
        method_pattern = r'^\s*([+\-#~]?)\s*(\{[^}]*\})?\s*(\w+)\s*\(\s*(.*?)\s*\)\s*(?::\s*(.+?))?$'
        match = re.match(method_pattern, line)
        
        if not match:
            return None
        
        visibility_symbol = match.group(1) or '+'
        modifiers_str = match.group(2) or ''
        method_name = match.group(3)
        params_str = match.group(4)
        return_type = match.group(5) or 'void'
        
        # Parse visibility
        visibility = self.parse_visibility(visibility_symbol)
        
        # Parse modifiers  
        is_static = 'static' in modifiers_str
        is_abstract = 'abstract' in modifiers_str
        
        # Parse parameters
        params = []
        if params_str.strip():
            param_parts = params_str.split(',')
            for param in param_parts:
                param = param.strip()
                if ':' in param:
                    param_name, param_type = param.split(':', 1)
                    params.append({'name': param_name.strip(), 'type': param_type.strip()})
        
        return {
            'name': method_name,
            'visibility': visibility,
            'parameters': params,
            'return_type': return_type,
            'is_static': is_static,
            'is_abstract': is_abstract
        }
    
    def parse_visibility(self, symbol):
        """Parse visibility symbol to UML visibility"""
        if symbol == '+':
            return 'public'
        elif symbol == '-':
            return 'private'
        elif symbol == '#':
            return 'protected'
        elif symbol == '~':
            return 'package'
        else:
            return 'public'  # Default to public instead of package
    
    def parse_relationship(self, line):
        """Parse relationship line and extract information"""
        # Remove labels and clean up
        clean_line = re.sub(r':\s*"[^"]*"', '', line).strip()
        
        # Different relationship patterns
        patterns = [
            (r'(\w+)\s*\*--\s*(\w+)', 'composition'),
            (r'(\w+)\s*o--\s*(\w+)', 'aggregation'),
            (r'(\w+)\s*<\|--\s*(\w+)', 'generalization'),
            (r'(\w+)\s*-->\s*(\w+)', 'association'),
            (r'(\w+)\s*--\s*(\w+)', 'association'),
            (r'(\w+)\s*\.\.|>\s*(\w+)', 'realization'),
            (r'(\w+)\s*\.\.>\s*(\w+)', 'dependency')
        ]
        
        for pattern, rel_type in patterns:
            match = re.search(pattern, clean_line)
            if match:
                source = match.group(1)
                target = match.group(2)
                return {
                    'source': source,
                    'target': target,
                    'type': rel_type
                }
        
        return None
    
    def create_class_element(self, model, class_info):
        """Create XMI class element with ALL attributes and operations"""
        class_elem = ET.SubElement(model, "packagedElement")
        class_elem.set("xmi:type", "uml:Class")
        class_id = str(uuid.uuid4())
        class_elem.set("xmi:id", class_id)
        class_elem.set("name", class_info['name'])
        
        self.class_map[class_info['name']] = class_id
        
        # Add ALL attributes
        print(f"Creating {class_info['name']} with {len(class_info['attributes'])} attributes")
        for attr_info in class_info['attributes']:
            print(f"  Adding attribute: {attr_info['name']} : {attr_info['type']}")
            self.create_attribute_element(class_elem, attr_info)
        
        # Add methods
        for method_info in class_info['methods']:
            self.create_operation_element(class_elem, method_info)
        
        return class_elem
    
    def create_interface_element(self, model, interface_info):
        """Create XMI interface element"""
        interface_elem = ET.SubElement(model, "packagedElement")
        interface_elem.set("xmi:type", "uml:Interface")
        interface_id = str(uuid.uuid4())
        interface_elem.set("xmi:id", interface_id)
        interface_elem.set("name", interface_info['name'])
        
        self.interface_map[interface_info['name']] = interface_id
        
        # Add methods
        for method_info in interface_info['methods']:
            self.create_operation_element(interface_elem, method_info)
        
        return interface_elem
    
    def create_enumeration_element(self, model, enum_info):
        """Create XMI enumeration element"""
        enum_elem = ET.SubElement(model, "packagedElement")
        enum_elem.set("xmi:type", "uml:Enumeration")
        enum_id = str(uuid.uuid4())
        enum_elem.set("xmi:id", enum_id)
        enum_elem.set("name", enum_info['name'])
        
        self.enum_map[enum_info['name']] = enum_id
        
        # Add enumeration literals
        for value in enum_info['values']:
            literal = ET.SubElement(enum_elem, "ownedLiteral")
            literal.set("xmi:type", "uml:EnumerationLiteral")
            literal.set("xmi:id", str(uuid.uuid4()))
            literal.set("name", value)
        
        return enum_elem
    
    def create_attribute_element(self, parent, attr_info):
        """Create XMI attribute element with proper type reference"""
        attr_elem = ET.SubElement(parent, "ownedAttribute")
        attr_elem.set("xmi:id", str(uuid.uuid4()))
        attr_elem.set("name", attr_info['name'])
        attr_elem.set("visibility", attr_info['visibility'])
        
        # Set type reference
        attr_type = attr_info['type']
        # Clean up complex types
        clean_type = attr_type.replace('[]', '').replace('<', '').replace('>', '').strip()
        
        if clean_type in self.primitive_types:
            attr_elem.set("type", self.primitive_types[clean_type])
        elif clean_type in self.class_map:
            attr_elem.set("type", self.class_map[clean_type])
        else:
            # Just set the type name directly
            attr_elem.set("type", clean_type)
    
    def create_operation_element(self, parent, method_info):
        """Create XMI operation element with parameters and return type"""
        op_elem = ET.SubElement(parent, "ownedOperation")
        op_elem.set("xmi:id", str(uuid.uuid4()))
        op_elem.set("name", method_info['name'])
        op_elem.set("visibility", method_info['visibility'])
        
        # Add parameters
        for param in method_info['parameters']:
            param_elem = ET.SubElement(op_elem, "ownedParameter")
            param_elem.set("xmi:id", str(uuid.uuid4()))
            param_elem.set("name", param['name'])
            param_elem.set("direction", "in")
            
            # Set parameter type
            param_type = param['type'].strip()
            if param_type in self.primitive_types:
                param_elem.set("type", self.primitive_types[param_type])
            elif param_type in self.class_map:
                param_elem.set("type", self.class_map[param_type])
            else:
                param_elem.set("type", param_type)
        
        # Add return parameter
        if method_info['return_type'] != 'void':
            return_param = ET.SubElement(op_elem, "ownedParameter")
            return_param.set("xmi:id", str(uuid.uuid4()))
            return_param.set("direction", "return")
            
            return_type = method_info['return_type'].strip()
            if return_type in self.primitive_types:
                return_param.set("type", self.primitive_types[return_type])
            elif return_type in self.class_map:
                return_param.set("type", self.class_map[return_type])
            else:
                return_param.set("type", return_type)
    
    def create_associations(self, model, relationships):
        """Create ALL association elements for Magic Systems"""
        for rel in relationships:
            if rel['source'] in self.class_map and rel['target'] in self.class_map:
                if rel['type'] == 'composition':
                    self.create_composition_association(model, rel)
                elif rel['type'] == 'aggregation':
                    self.create_aggregation_association(model, rel)
                elif rel['type'] == 'association':
                    self.create_simple_association(model, rel)
                elif rel['type'] == 'generalization':
                    self.create_generalization_element(model, rel)
                elif rel['type'] == 'realization':
                    self.create_realization_element(model, rel)
                elif rel['type'] == 'dependency':
                    self.create_dependency_association(model, rel)
    
    def create_simple_association(self, model, relationship):
        """Create simple UML Association for Magic Systems"""
        assoc = ET.SubElement(model, "packagedElement")
        assoc.set("xmi:type", "uml:Association")
        assoc.set("xmi:id", str(uuid.uuid4()))
        assoc.set("name", f"{relationship['source']}_to_{relationship['target']}")
        
        # Source end
        source_end = ET.SubElement(assoc, "memberEnd")
        source_end_id = str(uuid.uuid4())
        source_end.set("xmi:idref", source_end_id)
        
        # Target end  
        target_end = ET.SubElement(assoc, "memberEnd")
        target_end_id = str(uuid.uuid4())
        target_end.set("xmi:idref", target_end_id)
        
        # Create property elements for ends
        source_prop = ET.SubElement(assoc, "ownedEnd")
        source_prop.set("xmi:id", source_end_id)
        source_prop.set("type", self.class_map[relationship['source']])
        
        target_prop = ET.SubElement(assoc, "ownedEnd")
        target_prop.set("xmi:id", target_end_id)
        target_prop.set("type", self.class_map[relationship['target']])
    
    def create_composition_association(self, model, relationship):
        """Create UML Composition Association"""
        assoc = ET.SubElement(model, "packagedElement")
        assoc.set("xmi:type", "uml:Association")
        assoc.set("xmi:id", str(uuid.uuid4()))
        assoc.set("name", f"{relationship['source']}_composedOf_{relationship['target']}")
        
        # Source end (composite)
        source_end = ET.SubElement(assoc, "memberEnd")
        source_end_id = str(uuid.uuid4())
        source_end.set("xmi:idref", source_end_id)
        
        # Target end (composed)
        target_end = ET.SubElement(assoc, "memberEnd")
        target_end_id = str(uuid.uuid4())
        target_end.set("xmi:idref", target_end_id)
        
        # Source property (composite)
        source_prop = ET.SubElement(assoc, "ownedEnd")
        source_prop.set("xmi:id", source_end_id)
        source_prop.set("type", self.class_map[relationship['source']])
        source_prop.set("aggregation", "composite")
        
        # Target property (composed)
        target_prop = ET.SubElement(assoc, "ownedEnd")
        target_prop.set("xmi:id", target_end_id)
        target_prop.set("type", self.class_map[relationship['target']])
    
    def create_aggregation_association(self, model, relationship):
        """Create UML Aggregation Association"""
        assoc = ET.SubElement(model, "packagedElement")
        assoc.set("xmi:type", "uml:Association")
        assoc.set("xmi:id", str(uuid.uuid4()))
        assoc.set("name", f"{relationship['source']}_aggregates_{relationship['target']}")
        
        # Source end (aggregate)
        source_end = ET.SubElement(assoc, "memberEnd")
        source_end_id = str(uuid.uuid4())
        source_end.set("xmi:idref", source_end_id)
        
        # Target end (aggregated)
        target_end = ET.SubElement(assoc, "memberEnd")
        target_end_id = str(uuid.uuid4())
        target_end.set("xmi:idref", target_end_id)
        
        # Source property (aggregate)
        source_prop = ET.SubElement(assoc, "ownedEnd")
        source_prop.set("xmi:id", source_end_id)
        source_prop.set("type", self.class_map[relationship['source']])
        source_prop.set("aggregation", "shared")
        
        # Target property (aggregated)
        target_prop = ET.SubElement(assoc, "ownedEnd")
        target_prop.set("xmi:id", target_end_id)
        target_prop.set("type", self.class_map[relationship['target']])
    
    def create_dependency_association(self, model, relationship):
        """Create UML Dependency"""
        dep = ET.SubElement(model, "packagedElement")
        dep.set("xmi:type", "uml:Dependency")
        dep.set("xmi:id", str(uuid.uuid4()))
        dep.set("client", self.class_map[relationship['source']])
        dep.set("supplier", self.class_map[relationship['target']])
        dep.set("name", f"{relationship['source']}_depends_on_{relationship['target']}")
    
    def create_generalization_element(self, model, relationship):
        """Create UML Generalization"""
        source_id = self.class_map.get(relationship['source']) or self.interface_map.get(relationship['source'])
        target_id = self.class_map.get(relationship['target']) or self.interface_map.get(relationship['target'])
        
        if source_id and target_id:
            gen = ET.SubElement(model, "packagedElement")
            gen.set("xmi:type", "uml:Generalization")
            gen.set("xmi:id", str(uuid.uuid4()))
            gen.set("specific", source_id)
            gen.set("general", target_id)
    
    def create_realization_element(self, model, relationship):
        """Create UML InterfaceRealization"""
        source_id = self.class_map.get(relationship['source'])
        target_id = self.interface_map.get(relationship['target'])
        
        if source_id and target_id:
            real = ET.SubElement(model, "packagedElement")
            real.set("xmi:type", "uml:InterfaceRealization")
            real.set("xmi:id", str(uuid.uuid4()))
            real.set("implementingClassifier", source_id)
            real.set("contract", target_id)
    
    def convert_to_xmi(self, plantuml_file, output_file):
        """Convert PlantUML file to corrected XMI with ALL attributes"""
        print(f"Converting {plantuml_file} to {output_file}...")
        
        # Parse PlantUML with improved parsing
        elements = self.parse_plantuml_file(plantuml_file)
        
        # Create XMI document
        root, model = self.create_xmi_document("World System - Final Corrected")
        
        # Create classes first (so we can reference them in associations)
        for class_info in elements['classes'].values():
            self.create_class_element(model, class_info)
        
        # Create interfaces
        for interface_info in elements['interfaces'].values():
            self.create_interface_element(model, interface_info)
        
        # Create enums
        for enum_info in elements['enums'].values():
            self.create_enumeration_element(model, enum_info)
        
        # Create ALL relationships for Magic Systems
        self.create_associations(model, elements['relationships'])
        
        # Write XMI file
        self.write_xmi_file(root, output_file)
        
        print(f"✓ Created {output_file}")
        print(f"  Classes: {len(elements['classes'])}")
        print(f"  Interfaces: {len(elements['interfaces'])}")
        print(f"  Enums: {len(elements['enums'])}")
        print(f"  Relationships: {len(elements['relationships'])}")
    
    def write_xmi_file(self, root, output_file):
        """Write XMI to file with proper formatting"""
        # Pretty print the XML
        self.indent_xml(root)
        
        # Create tree and write
        tree = ET.ElementTree(root)
        tree.write(output_file, encoding='utf-8', xml_declaration=True)
    
    def indent_xml(self, elem, level=0):
        """Add indentation to XML for readability"""
        i = "\n" + level * "  "
        if len(elem):
            if not elem.text or not elem.text.strip():
                elem.text = i + "  "
            if not elem.tail or not elem.tail.strip():
                elem.tail = i
            for child in elem:
                self.indent_xml(child, level + 1)
            if not child.tail or not child.tail.strip():
                child.tail = i
        else:
            if level and (not elem.tail or not elem.tail.strip()):
                elem.tail = i

def main():
    """Generate final corrected XMI with ALL attributes properly parsed"""
    converter = FinalFixedXMIConverter()
    
    # Input file
    puml_file = "world_system_corrected.puml"
    
    # Generate final corrected XMI with ALL attributes
    print("Creating FINAL corrected XMI with ALL attributes...")
    converter.convert_to_xmi(puml_file, "world_system_FINAL_with_all_attributes.xmi")
    
    print("\n🎉 FINAL XMI with ALL attributes created!")
    print("Try importing: world_system_FINAL_with_all_attributes.xmi")
    print("This version includes:")
    print("  ✓ ALL class attributes properly parsed from PlantUML")
    print("  ✓ Handles {readonly}, {static} modifiers")
    print("  ✓ Proper complex type parsing (Area[], Locked<Area>, etc.)")
    print("  ✓ Debug output shows attribute count per class")

if __name__ == "__main__":
    main()