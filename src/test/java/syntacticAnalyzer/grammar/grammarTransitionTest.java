package syntacticAnalyzer.grammar;

import lexer.LexerDriver;
import lexer.Token;
import lexer.Tokenizer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import syntacticAnalyzer.Parser;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class grammarTransitionTest {

    @Spy
    Parser parser = new Parser();
    @Test
    public void testClassDeclRepTest() {
        LexerDriver lexerDriver = new LexerDriver();
        List<Token> tokens = lexerDriver.getTokensFromInput("class temp");
        parser .setTokenList(tokens);
        parser.parse();
        Mockito.verify(parser, times(1)).prog();
        Mockito.verify(parser, times(1)).classDeclRep();
        Mockito.verify(parser, times(1)).classDecl();
    }
}
