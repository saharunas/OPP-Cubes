package ethanjones.cubes.side.server.command.parsing;

/**
 * Terminal Expression - parses a quoted string with escape support
 * 
 * Grammar: quoted ::= '"' (char | '\"')* '"' | "'" (char | "\'")* "'"
 * 
 * Examples:
 * - "hello world" → hello world
 * - 'test string' → test string
 * - "say \"hi\"" → say "hi"
 * - "line1\nline2" → line1[newline]line2
 * 
 * Pattern Role: TerminalExpression
 */
public class QuotedStringToken extends TokenExpression {
  
  @Override
  public boolean matches(ParseContext context) {
    if (!context.hasMore()) return false;
    char ch = context.peek();
    return ch == '"' || ch == '\'';
  }
  
  @Override
  public String interpret(ParseContext context) {
    if (!matches(context)) return null;
    
    char quoteChar = context.consume(); // Consume opening quote
    StringBuilder result = new StringBuilder();
    
    while (context.hasMore()) {
      char ch = context.consume();
      
      if (ch == '\\' && context.hasMore()) {
        // Handle escape sequences
        char escaped = context.consume();
        switch (escaped) {
          case 'n':
            result.append('\n');
            break;
          case 't':
            result.append('\t');
            break;
          case 'r':
            result.append('\r');
            break;
          case '\\':
            result.append('\\');
            break;
          case '"':
            result.append('"');
            break;
          case '\'':
            result.append('\'');
            break;
          default:
            // Unknown escape, keep both characters
            result.append('\\').append(escaped);
            break;
        }
      } else if (ch == quoteChar) {
        // Found closing quote
        return result.toString();
      } else {
        result.append(ch);
      }
    }
    
    // Unclosed quote - return what we have
    return result.toString();
  }
}
