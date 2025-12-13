package ethanjones.cubes.side.server.command.parsing;

import java.util.List;

/**
 * Manual tests for the Interpreter Pattern implementation
 * 
 * Tests Terminal and NonTerminal expressions with various edge cases
 * Run main() to execute all tests
 */
public class CommandParsingTest {
  
  private static final CommandExpression parser = new CommandExpression();
  private static int testsPassed = 0;
  private static int testsFailed = 0;

  
  public static void main(String[] args) {
    System.out.println("=== Command Parsing Tests (Interpreter Pattern) ===\n");
    
    testSimpleCommand();
    testQuotedString();
    testSingleQuotes();
    testEscapeCharacters();
    testEscapedQuotes();
    testNumbers();
    testNegativeNumbers();
    testFloats();
    testMixedTokens();
    testMultipleSpaces();
    testLeadingTrailingSpaces();
    testEmptyString();
    testOnlySpaces();
    testComplexCommand();
    testUnclosedQuote();
    testSpecialCharacters();
    testTabsAndNewlines();
    
    System.out.println("\n=== Test Results ===");
    System.out.println("Passed: " + testsPassed);
    System.out.println("Failed: " + testsFailed);
    System.out.println("Total:  " + (testsPassed + testsFailed));
    
    if (testsFailed == 0) {
      System.out.println("\n✓ All tests passed!");
    } else {
      System.out.println("\n✗ Some tests failed!");
    }
  }
  
  private static void assertEquals(int expected, int actual) {
    if (expected != actual) {
      throw new AssertionError("Expected: " + expected + ", but got: " + actual);
    }
  }
  
  private static void assertEquals(String expected, String actual) {
    if (expected == null && actual == null) return;
    if (expected == null || actual == null || !expected.equals(actual)) {
      throw new AssertionError("Expected: \"" + expected + "\", but got: \"" + actual + "\"");
    }
  }
  
  private static void runTest(String testName, Runnable test) {
    try {
      test.run();
      System.out.println("✓ " + testName);
      testsPassed++;
    } catch (Exception e) {
      System.out.println("✗ " + testName + ": " + e.getMessage());
      testsFailed++;
    }
  }
  
  public static void testSimpleCommand() {
    runTest("testSimpleCommand", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("say hello");
        assertEquals(2, tokens.size());
        assertEquals("say", tokens.get(0));
        assertEquals("hello", tokens.get(1));
      }
    });
  }
  
  public static void testQuotedString() {
    runTest("testQuotedString", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("say \"hello world\"");
        assertEquals(2, tokens.size());
        assertEquals("say", tokens.get(0));
        assertEquals("hello world", tokens.get(1));
      }
    });
  }
  
  public static void testSingleQuotes() {
    runTest("testSingleQuotes", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("say 'hello world'");
        assertEquals(2, tokens.size());
        assertEquals("say", tokens.get(0));
        assertEquals("hello world", tokens.get(1));
      }
    });
  }
  
  public static void testEscapeCharacters() {
    runTest("testEscapeCharacters", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("say \"hello\\nworld\"");
        assertEquals(2, tokens.size());
        assertEquals("say", tokens.get(0));
        assertEquals("hello\nworld", tokens.get(1));
      }
    });
  }
  
  public static void testEscapedQuotes() {
    runTest("testEscapedQuotes", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("say \"say \\\"hi\\\"\"");
        assertEquals(2, tokens.size());
        assertEquals("say", tokens.get(0));
        assertEquals("say \"hi\"", tokens.get(1));
      }
    });
  }
  
  public static void testNumbers() {
    runTest("testNumbers", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("tp 10 20 30");
        assertEquals(4, tokens.size());
        assertEquals("tp", tokens.get(0));
        assertEquals("10", tokens.get(1));
        assertEquals("20", tokens.get(2));
        assertEquals("30", tokens.get(3));
      }
    });
  }
  
  public static void testNegativeNumbers() {
    runTest("testNegativeNumbers", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("tp -5 10 -15");
        assertEquals(4, tokens.size());
        assertEquals("tp", tokens.get(0));
        assertEquals("-5", tokens.get(1));
        assertEquals("10", tokens.get(2));
        assertEquals("-15", tokens.get(3));
      }
    });
  }
  
  public static void testFloats() {
    runTest("testFloats", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("tp 10.5 20.75 30.0");
        assertEquals(4, tokens.size());
        assertEquals("tp", tokens.get(0));
        assertEquals("10.5", tokens.get(1));
        assertEquals("20.75", tokens.get(2));
        assertEquals("30.0", tokens.get(3));
      }
    });
  }
  
  public static void testMixedTokens() {
    runTest("testMixedTokens", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("give @player stone 64");
        assertEquals(4, tokens.size());
        assertEquals("give", tokens.get(0));
        assertEquals("@player", tokens.get(1));
        assertEquals("stone", tokens.get(2));
        assertEquals("64", tokens.get(3));
      }
    });
  }
  
  public static void testMultipleSpaces() {
    runTest("testMultipleSpaces", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("say    hello    world");
        assertEquals(3, tokens.size());
        assertEquals("say", tokens.get(0));
        assertEquals("hello", tokens.get(1));
        assertEquals("world", tokens.get(2));
      }
    });
  }
  
  public static void testLeadingTrailingSpaces() {
    runTest("testLeadingTrailingSpaces", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("  say hello  ");
        assertEquals(2, tokens.size());
        assertEquals("say", tokens.get(0));
        assertEquals("hello", tokens.get(1));
      }
    });
  }
  
  public static void testEmptyString() {
    runTest("testEmptyString", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("");
        assertEquals(0, tokens.size());
      }
    });
  }
  
  public static void testOnlySpaces() {
    runTest("testOnlySpaces", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("     ");
        assertEquals(0, tokens.size());
      }
    });
  }
  
  public static void testComplexCommand() {
    runTest("testComplexCommand", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("say \"Testing 123\" @player -10.5 'quoted'");
        assertEquals(5, tokens.size());
        assertEquals("say", tokens.get(0));
        assertEquals("Testing 123", tokens.get(1));
        assertEquals("@player", tokens.get(2));
        assertEquals("-10.5", tokens.get(3));
        assertEquals("quoted", tokens.get(4));
      }
    });
  }
  
  public static void testUnclosedQuote() {
    runTest("testUnclosedQuote", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("say \"hello");
        assertEquals(2, tokens.size());
        assertEquals("say", tokens.get(0));
        assertEquals("hello", tokens.get(1));
      }
    });
  }
  
  public static void testSpecialCharacters() {
    runTest("testSpecialCharacters", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("give @player diamond_sword 1");
        assertEquals(4, tokens.size());
        assertEquals("give", tokens.get(0));
        assertEquals("@player", tokens.get(1));
        assertEquals("diamond_sword", tokens.get(2));
        assertEquals("1", tokens.get(3));
      }
    });
  }
  
  public static void testTabsAndNewlines() {
    runTest("testTabsAndNewlines", new Runnable() {
      public void run() {
        List<String> tokens = parser.parseTokens("say \"line1\\tline2\\nline3\"");
        assertEquals(2, tokens.size());
        assertEquals("say", tokens.get(0));
        assertEquals("line1\tline2\nline3", tokens.get(1));
      }
    });
  }
}
