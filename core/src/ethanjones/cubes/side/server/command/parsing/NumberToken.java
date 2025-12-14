package ethanjones.cubes.side.server.command.parsing;

/**
 * Terminal Expression - parses numeric values (integers and floats)
 * 
 * Grammar: number ::= '-'? digit+ ('.' digit+)?
 * 
 * Examples:
 * - "123" → "123"
 * - "-45" → "-45"
 * - "3.14" → "3.14"
 * - "-0.5" → "-0.5"
 * 
 * Pattern Role: TerminalExpression
 */
public class NumberToken extends TokenExpression {
  
  @Override
  public boolean matches(ParseContext context) {
    if (!context.hasMore()) return false;
    char ch = context.peek();
    if (ch == '-' && context.getPosition() + 1 < context.getInput().length()) {
      return Character.isDigit(context.peek(1));
    }
    return Character.isDigit(ch);
  }
  
  @Override
  public String interpret(ParseContext context) {
    if (!matches(context)) return null;
    
    StringBuilder number = new StringBuilder();
    
    // Handle negative sign
    if (context.peek() == '-') {
      number.append(context.consume());
    }
    
    // Parse integer part
    while (context.hasMore() && Character.isDigit(context.peek())) {
      number.append(context.consume());
    }
    
    // Parse decimal part if present
    if (context.hasMore() && context.peek() == '.') {
      // Look ahead - make sure there's a digit after the dot
      if (context.getPosition() + 1 < context.getInput().length() &&
          Character.isDigit(context.peek(1))) {
        number.append(context.consume()); // consume '.'
        while (context.hasMore() && Character.isDigit(context.peek())) {
          number.append(context.consume());
        }
      }
    }
    
    return number.length() > 0 ? number.toString() : null;
  }
}
