package ethanjones.cubes.side.server.command.parsing;

/**
 * Abstract Expression for the Interpreter Pattern
 * 
 * Represents a token in the command parsing grammar.
 * Each concrete expression knows how to interpret (parse) its portion
 * of the input string using the provided context.
 * 
 * Pattern Role: AbstractExpression
 */
public abstract class TokenExpression {
  
  /**
   * Interpret (parse) a token from the context
   * 
   * @param context The parsing context containing input string and position
   * @return The parsed token value, or null if parsing fails
   */
  public abstract String interpret(ParseContext context);
  
  /**
   * Check if this expression can parse at current context position
   * 
   * @param context The parsing context
   * @return true if this expression matches the current position
   */
  public abstract boolean matches(ParseContext context);
}
