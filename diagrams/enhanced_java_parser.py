#!/usr/bin/env python3
"""
Enhanced Java Source to PlantUML Generator
Handles complex Java syntax more accurately
"""

import os
import re
from pathlib import Path
from collections import defaultdict
import argparse

class JavaClass:
    def __init__(self, name, package="", is_abstract=False, is_interface=False, is_enum=False):
        self.name = name
        self.package = package
        self.is_abstract = is_abstract
        self.is_interface = is_interface
        self.is_enum = is_enum
        self.extends = None
        self.implements = []
        self.attributes = []
        self.methods = []
        self.enum_values = []
        self.constants = []
        self.constructors = []

class JavaMember:
    def __init__(self, name, type_name="", visibility="package", is_static=False, is_final=False, is_volatile=False):
        self.name = name
        self.type = type_name
        self.visibility = visibility
        self.is_static = is_static
        self.is_final = is_final
        self.is_volatile = is_volatile

class JavaMethod(JavaMember):
    def __init__(self, name, return_type="void", visibility="package", is_static=False, is_abstract=False, is_final=False):
        super().__init__(name, return_type, visibility, is_static, is_final)
        self.is_abstract = is_abstract
        self.parameters = []

class JavaSourceParser:
    def __init__(self):
        self.classes = {}
        
    def parse_file(self, file_path):
        """Parse a single Java file"""
        try:
            with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
                content = f.read()
        except Exception as e:
            print(f"Error reading {file_path}: {e}")
            return []
            
        return self.parse_content(content, str(file_path))
    
    def parse_content(self, content, file_path=""):
        """Parse Java source content"""
        # Remove comments first
        content = self.remove_comments(content)
        
        # Extract package
        package = self.extract_package(content)
        
        # Find class/interface/enum declarations
        classes = self.extract_classes(content, package, file_path)
        
        return classes
    
    def remove_comments(self, content):
        """Remove Java comments from content"""
        # Remove single-line comments, but preserve string literals
        lines = content.split('\n')
        result_lines = []
        
        for line in lines:
            in_string = False
            in_char = False
            escape = False
            i = 0
            
            while i < len(line):
                char = line[i]
                
                if escape:
                    escape = False
                elif char == '\\':
                    escape = True
                elif not in_char and char == '"':
                    in_string = not in_string
                elif not in_string and char == "'":
                    in_char = not in_char
                elif not in_string and not in_char and char == '/' and i + 1 < len(line) and line[i + 1] == '/':
                    line = line[:i]
                    break
                
                i += 1
            
            result_lines.append(line)
        
        content = '\n'.join(result_lines)
        
        # Remove multi-line comments
        content = re.sub(r'/\*.*?\*/', '', content, flags=re.DOTALL)
        
        return content
    
    def extract_package(self, content):
        """Extract package declaration"""
        match = re.search(r'package\s+([\w\.]+)\s*;', content)
        return match.group(1) if match else ""
    
    def extract_classes(self, content, package, file_path):
        """Extract class/interface/enum declarations"""
        classes = []
        
        # More robust class pattern
        class_pattern = r'(?:^|\s)((?:(?:public|private|protected|static|final|abstract)\s+)*)\s*(class|interface|enum)\s+(\w+)(?:\s*<[^>]*>)?(?:\s+extends\s+([\w\.<>]+))?(?:\s+implements\s+([\w\s,.<>]+))?\s*\{'
        
        matches = list(re.finditer(class_pattern, content, re.MULTILINE))
        
        for match in matches:
            modifiers = match.group(1).strip()
            class_type = match.group(2)
            class_name = match.group(3)
            extends_clause = match.group(4)
            implements_clause = match.group(5)
            
            # Create class object
            java_class = JavaClass(
                name=class_name,
                package=package,
                is_abstract="abstract" in modifiers,
                is_interface=class_type == "interface",
                is_enum=class_type == "enum"
            )
            
            if extends_clause:
                java_class.extends = self.clean_type_name(extends_clause)
            
            if implements_clause:
                interfaces = [self.clean_type_name(i.strip()) for i in implements_clause.split(',')]
                java_class.implements = interfaces
            
            # Find class body
            class_start = match.end() - 1  # Include the opening brace
            class_body = self.extract_class_body(content, class_start)
            
            if class_body:
                if java_class.is_enum:
                    java_class.enum_values = self.extract_enum_values(class_body)
                
                java_class.attributes = self.extract_fields(class_body)
                java_class.methods = self.extract_methods(class_body, class_name)
            
            classes.append(java_class)
            
        return classes
    
    def extract_class_body(self, content, start_pos):
        """Extract the body of a class between braces"""
        i = start_pos
        while i < len(content) and content[i] != '{':
            i += 1
        
        if i >= len(content):
            return ""
        
        # Find matching closing brace
        brace_count = 1
        i += 1
        start = i
        
        while i < len(content) and brace_count > 0:
            char = content[i]
            if char == '{':
                brace_count += 1
            elif char == '}':
                brace_count -= 1
            i += 1
        
        return content[start:i-1] if brace_count == 0 else content[start:]
    
    def extract_enum_values(self, class_body):
        """Extract enum values"""
        enum_values = []
        
        # Look for enum constants at the beginning
        lines = class_body.split('\n')
        for line in lines:
            line = line.strip()
            
            # Stop at first method or field declaration
            if line.startswith('public ') or line.startswith('private ') or line.startswith('protected ') or ';' in line:
                break
            
            # Extract enum constants
            if line and not line.startswith('//') and re.match(r'^[A-Z][A-Z0-9_]*', line):
                enum_name = line.split('(')[0].split(',')[0].strip()
                if enum_name:
                    enum_values.append(enum_name)
        
        return enum_values
    
    def extract_fields(self, class_body):
        """Extract field declarations with better handling"""
        fields = []
        
        # Split into lines and process each
        lines = class_body.split('\n')
        i = 0
        
        while i < len(lines):
            line = lines[i].strip()
            
            # Skip empty lines and comments
            if not line or line.startswith('//') or line.startswith('/*'):
                i += 1
                continue
            
            # Skip method declarations (have parentheses)
            if '(' in line and ')' in line and not line.endswith(';'):
                i += 1
                continue
            
            # Check for field declaration
            field_match = re.match(
                r'((?:public|private|protected|static|final|volatile|transient)\s+)*'  # modifiers
                r'([\w\[\]<>\.,\s]+?)\s+'  # type
                r'(\w+)'  # name
                r'(?:\s*=\s*[^;]+)?'  # optional initialization
                r'\s*;',  # semicolon
                line
            )
            
            if field_match:
                modifiers_str = field_match.group(1) or ""
                type_str = field_match.group(2).strip()
                field_name = field_match.group(3)
                
                # Parse modifiers
                modifiers = modifiers_str.lower().split()
                visibility = self.get_visibility_from_modifiers(modifiers)
                is_static = "static" in modifiers
                is_final = "final" in modifiers
                is_volatile = "volatile" in modifiers
                
                # Clean type name
                type_str = self.clean_type_name(type_str)
                
                field = JavaMember(
                    name=field_name,
                    type_name=type_str,
                    visibility=visibility,
                    is_static=is_static,
                    is_final=is_final,
                    is_volatile=is_volatile
                )
                
                fields.append(field)
            
            i += 1
        
        return fields
    
    def extract_methods(self, class_body, class_name):
        """Extract method declarations"""
        methods = []
        
        # Pattern for methods - more comprehensive
        method_pattern = r'((?:public|private|protected|static|final|abstract|synchronized|native)\s+)*' + \
                        r'(?:<[^>]*>\s+)?' + \
                        r'([\w\[\]<>\.,\s]+?)\s+' + \
                        r'(\w+)\s*' + \
                        r'\(([^)]*)\)' + \
                        r'(?:\s*throws\s+[\w\s,\.]+)?' + \
                        r'\s*[{;]'
        
        for match in re.finditer(method_pattern, class_body, re.MULTILINE):
            modifiers_str = match.group(1) or ""
            return_type = match.group(2).strip()
            method_name = match.group(3)
            params_str = match.group(4).strip()
            
            # Skip if this looks like a field declaration
            if method_name == class_name:
                continue  # Constructor - handle separately if needed
            
            # Parse modifiers
            modifiers = modifiers_str.lower().split()
            visibility = self.get_visibility_from_modifiers(modifiers)
            is_static = "static" in modifiers
            is_abstract = "abstract" in modifiers
            is_final = "final" in modifiers
            
            # Clean return type
            return_type = self.clean_type_name(return_type)
            
            method = JavaMethod(
                name=method_name,
                return_type=return_type,
                visibility=visibility,
                is_static=is_static,
                is_abstract=is_abstract,
                is_final=is_final
            )
            
            # Parse parameters
            if params_str:
                method.parameters = self.parse_parameters(params_str)
            
            methods.append(method)
        
        return methods
    
    def parse_parameters(self, params_str):
        """Parse method parameters"""
        parameters = []
        
        if not params_str.strip():
            return parameters
        
        # Simple parameter parsing - split by comma
        param_parts = []
        current_param = ""
        paren_depth = 0
        
        for char in params_str + ',':
            if char == ',' and paren_depth == 0:
                if current_param.strip():
                    param_parts.append(current_param.strip())
                current_param = ""
            else:
                if char in '(<':
                    paren_depth += 1
                elif char in ')>':
                    paren_depth -= 1
                current_param += char
        
        for param in param_parts:
            # Remove annotations and modifiers
            param = re.sub(r'@\w+\s*', '', param)
            param = re.sub(r'\bfinal\s+', '', param)
            
            parts = param.split()
            if len(parts) >= 2:
                type_name = ' '.join(parts[:-1])
                param_name = parts[-1]
                parameters.append({
                    'name': param_name,
                    'type': self.clean_type_name(type_name)
                })
        
        return parameters
    
    def get_visibility_from_modifiers(self, modifiers):
        """Get visibility from list of modifiers"""
        if "public" in modifiers:
            return "public"
        elif "private" in modifiers:
            return "private"
        elif "protected" in modifiers:
            return "protected"
        else:
            return "package"
    
    def clean_type_name(self, type_name):
        """Clean up type name for display"""
        if not type_name:
            return "void"
        
        # Remove extra whitespace
        type_name = ' '.join(type_name.split())
        
        # Handle arrays
        type_name = type_name.replace(' []', '[]')
        
        return type_name
    
    def parse_directory(self, directory, extensions=['.java']):
        """Parse all Java files in a directory"""
        directory = Path(directory)
        all_classes = []
        
        for ext in extensions:
            for java_file in directory.rglob(f'*{ext}'):
                classes = self.parse_file(java_file)
                if classes:
                    all_classes.extend(classes)
        
        return all_classes

class PlantUMLGenerator:
    def __init__(self):
        self.visibility_map = {
            'public': '+',
            'private': '-',
            'protected': '#',
            'package': '~'
        }
    
    def generate_plantuml(self, classes, title="Java Class Diagram", package_filter=None, max_classes=50):
        """Generate PlantUML from parsed classes"""
        
        if package_filter:
            classes = [c for c in classes if c.package.startswith(package_filter)]
        
        # Limit number of classes to avoid overwhelming diagrams
        if len(classes) > max_classes:
            print(f"Warning: Found {len(classes)} classes, limiting to first {max_classes}")
            classes = classes[:max_classes]
        
        plantuml = f"@startuml {title.replace(' ', '_')}\n"
        plantuml += "!theme plain\n"
        plantuml += f"title {title}\n\n"
        
        for cls in classes:
            plantuml += self.generate_class_plantuml(cls)
            plantuml += "\n"
        
        # Generate relationships
        plantuml += self.generate_relationships(classes)
        
        plantuml += "@enduml\n"
        
        return plantuml
    
    def generate_class_plantuml(self, cls):
        """Generate PlantUML for a single class"""
        lines = []
        
        # Class declaration
        if cls.is_enum:
            lines.append(f"    enum {cls.name} {{")
        elif cls.is_interface:
            lines.append(f"    interface {cls.name} {{")
        elif cls.is_abstract:
            lines.append(f"    abstract class {cls.name} {{")
        else:
            lines.append(f"    class {cls.name} {{")
        
        # Enum values first
        if cls.is_enum and cls.enum_values:
            for value in cls.enum_values:
                lines.append(f"        +{value}")
            if cls.attributes or cls.methods:
                lines.append("        --")
        
        # Attributes
        shown_attributes = 0
        for attr in cls.attributes:
            if shown_attributes >= 20:  # Limit to prevent huge diagrams
                lines.append("        +... (more fields)")
                break
            
            visibility = self.visibility_map.get(attr.visibility, '~')
            markers = []
            
            if attr.is_static:
                markers.append("{static}")
            if attr.is_final:
                markers.append("{readonly}")
            
            marker_str = " ".join(markers)
            marker_str = marker_str + " " if marker_str else ""
            
            lines.append(f"        {visibility}{marker_str}{attr.name} : {attr.type}")
            shown_attributes += 1
        
        # Separator
        if cls.attributes and cls.methods:
            lines.append("        --")
        
        # Methods
        shown_methods = 0
        for method in cls.methods:
            if shown_methods >= 15:  # Limit methods too
                lines.append("        +... (more methods)")
                break
            
            visibility = self.visibility_map.get(method.visibility, '~')
            markers = []
            
            if method.is_static:
                markers.append("{static}")
            if method.is_abstract:
                markers.append("{abstract}")
            
            marker_str = " ".join(markers)
            marker_str = marker_str + " " if marker_str else ""
            
            # Parameters
            param_strs = []
            for param in method.parameters:
                param_strs.append(f"{param['name']} : {param['type']}")
            param_str = ", ".join(param_strs)
            
            lines.append(f"        {visibility}{marker_str}{method.name}({param_str}) : {method.type}")
            shown_methods += 1
        
        lines.append("    }")
        
        return "\n".join(lines)
    
    def generate_relationships(self, classes):
        """Generate inheritance and implementation relationships"""
        relationships = []
        
        for cls in classes:
            if cls.extends:
                relationships.append(f"    {cls.extends} <|-- {cls.name}")
            
            for interface in cls.implements:
                relationships.append(f"    {interface} <|.. {cls.name}")
        
        return "\n".join(relationships) + "\n" if relationships else ""

def main():
    parser = argparse.ArgumentParser(description='Generate accurate PlantUML diagrams from Java source code')
    parser.add_argument('directory', help='Root directory containing Java source files')
    parser.add_argument('--package', help='Filter classes by package prefix')
    parser.add_argument('--output', '-o', help='Output file name')
    parser.add_argument('--title', help='Diagram title')
    parser.add_argument('--max-classes', type=int, default=10, help='Maximum classes per diagram')
    
    args = parser.parse_args()
    
    # Parse Java sources
    print(f"Parsing Java files in {args.directory}...")
    java_parser = JavaSourceParser()
    classes = java_parser.parse_directory(args.directory)
    
    print(f"Found {len(classes)} total classes")
    
    # Filter by package
    if args.package:
        original_count = len(classes)
        classes = [c for c in classes if c.package.startswith(args.package)]
        print(f"Filtered to {len(classes)} classes in package '{args.package}'")
    
    if not classes:
        print("No classes found!")
        return
    
    # Generate PlantUML
    title = args.title or f"Java Classes - {args.package or Path(args.directory).name}"
    plantuml_generator = PlantUMLGenerator()
    plantuml_content = plantuml_generator.generate_plantuml(
        classes, 
        title=title,
        max_classes=args.max_classes
    )
    
    # Determine output filename
    if args.output:
        output_file = args.output
    else:
        safe_title = re.sub(r'[^\w\-_]', '_', (args.package or title).lower().replace('.', '_'))
        output_file = f"{safe_title}_accurate.puml"
    
    # Write output
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(plantuml_content)
    
    print(f"Generated PlantUML diagram: {output_file}")
    
    # Show class details
    for cls in classes[:5]:  # Show first 5 classes
        print(f"\nClass: {cls.package}.{cls.name}")
        print(f"  Attributes: {len(cls.attributes)}")
        print(f"  Methods: {len(cls.methods)}")

if __name__ == "__main__":
    main()