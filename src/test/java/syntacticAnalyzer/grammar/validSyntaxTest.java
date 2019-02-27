package syntacticAnalyzer.grammar;

import lexer.LexerDriver;
import lexer.Token;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import syntacticAnalyzer.Parser;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class validSyntaxTest {

    Parser parser = new Parser();

    LexerDriver lexerDriver;

    @Before
    public void setup() {
        lexerDriver = new LexerDriver();
    }

    @Test
    public void validEmptyProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validVarDeclIdProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { Blob id; };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validVarDeclTypeProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { integer id; };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validStatementProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { read(id); };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validWriteProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { write(id); };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validIfProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { \n" +
                "if(id) then \n" +
                "{return(id);} \n" +
                "else \n" +
                "{return(id);}; \n" +
                "};");
        parser .setTokenList(tokens);

        System.out.println(parser.parse());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validForProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { \n" +
                "for(id id = id; id < id; id = id);\n" +
                "};");
        parser .setTokenList(tokens);

        System.out.println(parser.parse());
        assertTrue(parser.isParseGood());
    }
}
