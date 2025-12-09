package ethanjones.cubes.side.server.command.parsing;

import java.util.ArrayList;
import java.util.List;

/**
 * Non-Terminal Expression - parses a complete command line into tokens
 * 
 * Grammar: command ::= token (' '+ token)*
 *          token   ::= quoted | number | word
 * 
 * Examples:
 * - "say hello" → ["say", "hello"]
 * - "tp 10 20 30" → ["tp", "10", "20", "30"]
 * - "say "hello world"" → ["say", "hello world"]
 * - "give @player stone 64" → ["give", "@player", "stone", "64"]
 * 
 * Pattern Role: NonTerminalExpression (Composite)
 */
public class CommandExpression extends TokenExpression {
  
  private final List<TokenExpression> terminalExpressions;
  
  public CommandExpression() {
    this.terminalExpressions = new ArrayList<>();
    // Order matters: try quoted first, then number, then word
    terminalExpressions.add(new QuotedStringToken());
    terminalExpressions.add(new NumberToken());
    terminalExpressions.add(new WordToken());
  }
  
  @Override
  public boolean matches(ParseContext context) {
    // Command always matches - can be empty
    return true;
  }
  
  @Override
  public String interpret(ParseContext context) {
    // This method not used for composite - use parseTokens() instead
    throw new UnsupportedOperationException("Use parseTokens() for CommandExpression");
  }
  
  /**
   * Parse the entire command into a list of tokens
   * 
   * @param input The command string to parse
   * @return List of parsed tokens (never null, may be empty)
   */
  public List<String> parseTokens(String input) {
    List<String> tokens = new ArrayList<>();
    ParseContext context = new ParseContext(input);
    
    while (context.hasMore()) {
      context.skipWhitespace();
      if (!context.hasMore()) break;
      
      // Try each terminal expression in order
      String token = null;
      for (TokenExpression expression : terminalExpressions) {
        if (expression.matches(context)) {
          token = expression.interpret(context);
          break;
        }
      }
      
      if (token != null) {
        tokens.add(token);
      } else {
        // Shouldn't happen, but consume one char to avoid infinite loop
        context.consume();
      }
    }
    
    return tokens;
  }
  
  /**
   * Parse command string into tokens (convenience method)
   * 
   * @param input Command string
   * @return Array of tokens
   */
  public String[] parse(String input) {
    List<String> tokens = parseTokens(input);
    return tokens.toArray(new String[0]);
  }
}
