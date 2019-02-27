package syntacticAnalyzer.grammar;

import lexer.LexerDriver;
import lexer.Token;
import org.junit.Before;
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

        System.out.println(parser.parse());
        Mockito.verify(parser, times(1)).classDeclRep();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validVarDeclProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { Blob id; };");
        parser .setTokenList(tokens);

        System.out.println(parser.parse());
        assertTrue(parser.isParseGood());
    }
}
