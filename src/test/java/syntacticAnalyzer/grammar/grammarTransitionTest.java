package syntacticAnalyzer.grammar;

import lexer.LexerDriver;
import lexer.Token;
import lexer.Tokenizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import syntacticAnalyzer.Parser;

import static org.junit.Assert.*;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class grammarTransitionTest {

    @Spy
    Parser parser = new Parser();

    LexerDriver lexerDriver;

    @Before
    public void setup() {
        lexerDriver = new LexerDriver();
    }

    @Test
    public void classDeclRepTest() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class temp { Blob blob");
        parser .setTokenList(tokens);
        parser.parse();
        Mockito.verify(parser, times(1)).prog();
        Mockito.verify(parser, times(1)).classDeclRep();
        Mockito.verify(parser, times(1)).classDecl();
        Mockito.verify(parser, times(1)).classExOpt();
        Mockito.verify(parser, times(0)).classExMoreRep();
        Mockito.verify(parser, times(1)).varOrFuncCheck();
        Mockito.verify(parser, times(1)).type();
        Mockito.verify(parser, times(0)).typeNotId();
        Mockito.verify(parser, times(1)).varCheckNext();
    }

    @Test
    public void classDeclRepTestInt() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class temp { integer blob");
        parser .setTokenList(tokens);
        parser.parse();
        Mockito.verify(parser, times(1)).prog();
        Mockito.verify(parser, times(1)).classDeclRep();
        Mockito.verify(parser, times(1)).classDecl();
        Mockito.verify(parser, times(1)).classExOpt();
        Mockito.verify(parser, times(0)).classExMoreRep();
        Mockito.verify(parser, times(1)).varOrFuncCheck();
        Mockito.verify(parser, times(1)).type();
        Mockito.verify(parser, times(1)).typeNotId();
        Mockito.verify(parser, times(1)).varCheckNext();
    }

    @Test
    public void classDeclExtRepTest() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class temp : blob {");
        parser .setTokenList(tokens);
        parser.parse();
        Mockito.verify(parser, times(1)).prog();
        Mockito.verify(parser, times(1)).classDeclRep();
        Mockito.verify(parser, times(1)).classDecl();
        Mockito.verify(parser, times(1)).classExOpt();
        Mockito.verify(parser, times(1)).classExMoreRep();
        Mockito.verify(parser, times(1)).varOrFuncCheck();
    }

    @Test
    public void invalidClassDeclTest() {
        List<Token> tokens = lexerDriver.getTokensFromInput("{ }");
        parser .setTokenList(tokens);
        parser.parse();
        Mockito.verify(parser, times(1)).prog();
        Mockito.verify(parser, times(0)).classDeclRep();
        assertFalse(parser.isParseGood());
    }


}
