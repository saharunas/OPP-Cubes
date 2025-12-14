package ethanjones.cubes.side.server.command.parsing;

/**
 * Terminal Expression - parses a simple word (no spaces, no quotes)
 * 
 * Grammar: word ::= [non-whitespace]+
 * 
 * Examples:
 * - "hello" → "hello"
 * - "test123" → "test123"
 * - "@player" → "@player"
 * 
 * Pattern Role: TerminalExpression
 */
public class WordToken extends TokenExpression {
  
  @Override
  public boolean matches(ParseContext context) {
    if (!context.hasMore()) return false;
    char ch = context.peek();
    // Word cannot start with quote
    return !Character.isWhitespace(ch) && ch != '"' && ch != '\'';
  }
  
  @Override
  public String interpret(ParseContext context) {
    if (!matches(context)) return null;
    
    StringBuilder word = new StringBuilder();
    while (context.hasMore()) {
      char ch = context.peek();
      // Stop at whitespace or quotes
      if (Character.isWhitespace(ch) || ch == '"' || ch == '\'') {
        break;
      }
      word.append(context.consume());
    }
    
    return word.length() > 0 ? word.toString() : null;
  }
}
