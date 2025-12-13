package ethanjones.cubes.side.server.command.parsing;

/**
 * Context for the Interpreter Pattern
 * 
 * Maintains the state during command parsing:
 * - The original input string
 * - Current parsing position
 * - Provides utility methods for examining input
 * 
 * Pattern Role: Context
 */
public class ParseContext {
  
  private final String input;
  private int position;
  
  public ParseContext(String input) {
    this.input = input;
    this.position = 0;
  }
  
  /**
   * Get the original input string
   */
  public String getInput() {
    return input;
  }
  
  /**
   * Get current parsing position
   */
  public int getPosition() {
    return position;
  }
  
  /**
   * Set parsing position
   */
  public void setPosition(int position) {
    if (position < 0 || position > input.length()) {
      throw new IllegalArgumentException("Position out of bounds: " + position);
    }
    this.position = position;
  }
  
  /**
   * Check if there are more characters to parse
   */
  public boolean hasMore() {
    return position < input.length();
  }
  
  /**
   * Peek at current character without consuming it
   */
  public char peek() {
    if (!hasMore()) {
      throw new IllegalStateException("No more characters to peek");
    }
    return input.charAt(position);
  }
  
  /**
   * Peek at character at offset from current position
   */
  public char peek(int offset) {
    int pos = position + offset;
    if (pos < 0 || pos >= input.length()) {
      throw new IllegalArgumentException("Peek offset out of bounds: " + offset);
    }
    return input.charAt(pos);
  }
  
  /**
   * Consume and return current character
   */
  public char consume() {
    if (!hasMore()) {
      throw new IllegalStateException("No more characters to consume");
    }
    return input.charAt(position++);
  }
  
  /**
   * Skip whitespace characters
   */
  public void skipWhitespace() {
    while (hasMore() && Character.isWhitespace(peek())) {
      position++;
    }
  }
  
  /**
   * Get remaining unparsed string
   */
  public String getRemaining() {
    return input.substring(position);
  }
  
  /**
   * Check if input starts with given string at current position
   */
  public boolean startsWith(String prefix) {
    return getRemaining().startsWith(prefix);
  }
}
