package ethanjones.cubes.side.server.command.parsing;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Unit tests for the Interpreter Pattern implementation
 * 
 * Tests Terminal and NonTerminal expressions with various edge cases
 */
public class CommandParsingTest {
  
  private final CommandExpression parser = new CommandExpression();
  
  @Test
  public void testSimpleCommand() {
    List<String> tokens = parser.parseTokens("say hello");
    assertEquals(2, tokens.size());
    assertEquals("say", tokens.get(0));
    assertEquals("hello", tokens.get(1));
  }
  
  @Test
  public void testQuotedString() {
    List<String> tokens = parser.parseTokens("say \"hello world\"");
    assertEquals(2, tokens.size());
    assertEquals("say", tokens.get(0));
    assertEquals("hello world", tokens.get(1));
  }
  
  @Test
  public void testSingleQuotes() {
    List<String> tokens = parser.parseTokens("say 'hello world'");
    assertEquals(2, tokens.size());
    assertEquals("say", tokens.get(0));
    assertEquals("hello world", tokens.get(1));
  }
  
  @Test
  public void testEscapeCharacters() {
    List<String> tokens = parser.parseTokens("say \"hello\\nworld\"");
    assertEquals(2, tokens.size());
    assertEquals("say", tokens.get(0));
    assertEquals("hello\nworld", tokens.get(1));
  }
  
  @Test
  public void testEscapedQuotes() {
    List<String> tokens = parser.parseTokens("say \"say \\\"hi\\\"\"");
    assertEquals(2, tokens.size());
    assertEquals("say", tokens.get(0));
    assertEquals("say \"hi\"", tokens.get(1));
  }
  
  @Test
  public void testNumbers() {
    List<String> tokens = parser.parseTokens("tp 10 20 30");
    assertEquals(4, tokens.size());
    assertEquals("tp", tokens.get(0));
    assertEquals("10", tokens.get(1));
    assertEquals("20", tokens.get(2));
    assertEquals("30", tokens.get(3));
  }
  
  @Test
  public void testNegativeNumbers() {
    List<String> tokens = parser.parseTokens("tp -5 10 -15");
    assertEquals(4, tokens.size());
    assertEquals("tp", tokens.get(0));
    assertEquals("-5", tokens.get(1));
    assertEquals("10", tokens.get(2));
    assertEquals("-15", tokens.get(3));
  }
  
  @Test
  public void testFloats() {
    List<String> tokens = parser.parseTokens("tp 10.5 20.75 30.0");
    assertEquals(4, tokens.size());
    assertEquals("tp", tokens.get(0));
    assertEquals("10.5", tokens.get(1));
    assertEquals("20.75", tokens.get(2));
    assertEquals("30.0", tokens.get(3));
  }
  
  @Test
  public void testMixedTokens() {
    List<String> tokens = parser.parseTokens("give @player stone 64");
    assertEquals(4, tokens.size());
    assertEquals("give", tokens.get(0));
    assertEquals("@player", tokens.get(1));
    assertEquals("stone", tokens.get(2));
    assertEquals("64", tokens.get(3));
  }
  
  @Test
  public void testMultipleSpaces() {
    List<String> tokens = parser.parseTokens("say    hello    world");
    assertEquals(3, tokens.size());
    assertEquals("say", tokens.get(0));
    assertEquals("hello", tokens.get(1));
    assertEquals("world", tokens.get(2));
  }
  
  @Test
  public void testLeadingTrailingSpaces() {
    List<String> tokens = parser.parseTokens("  say hello  ");
    assertEquals(2, tokens.size());
    assertEquals("say", tokens.get(0));
    assertEquals("hello", tokens.get(1));
  }
  
  @Test
  public void testEmptyString() {
    List<String> tokens = parser.parseTokens("");
    assertEquals(0, tokens.size());
  }
  
  @Test
  public void testOnlySpaces() {
    List<String> tokens = parser.parseTokens("     ");
    assertEquals(0, tokens.size());
  }
  
  @Test
  public void testComplexCommand() {
    List<String> tokens = parser.parseTokens("say \"Testing 123\" @player -10.5 'quoted'");
    assertEquals(5, tokens.size());
    assertEquals("say", tokens.get(0));
    assertEquals("Testing 123", tokens.get(1));
    assertEquals("@player", tokens.get(2));
    assertEquals("-10.5", tokens.get(3));
    assertEquals("quoted", tokens.get(4));
  }
  
  @Test
  public void testUnclosedQuote() {
    // Should still parse what it can
    List<String> tokens = parser.parseTokens("say \"hello");
    assertEquals(2, tokens.size());
    assertEquals("say", tokens.get(0));
    assertEquals("hello", tokens.get(1));
  }
  
  @Test
  public void testSpecialCharacters() {
    List<String> tokens = parser.parseTokens("give @player diamond_sword 1");
    assertEquals(4, tokens.size());
    assertEquals("give", tokens.get(0));
    assertEquals("@player", tokens.get(1));
    assertEquals("diamond_sword", tokens.get(2));
    assertEquals("1", tokens.get(3));
  }
  
  @Test
  public void testTabsAndNewlines() {
    List<String> tokens = parser.parseTokens("say \"line1\\tline2\\nline3\"");
    assertEquals(2, tokens.size());
    assertEquals("say", tokens.get(0));
    assertEquals("line1\tline2\nline3", tokens.get(1));
  }
}
