/*
 *                             Syntaxalyser 1.0                        
 *             Copyright © 2017 Ben Goldsworthy (rumperuu)        
 *                                                                      
 * A program to syntactically analyse programs written in SCC# using recursive
 * descent. I have added four new non-terminals `<assignment statement 
 * remainder>`, `<if statement remainder>`, `<expression remainder>` and
 * `<condition remainder>`, which were the results of applying left-recursion
 * to a number of original productions. I have also removed the `<term>`
 * non-terminal and rolled it into the `<factor>` non-terminal for simplicity,
 * as `<term>` was redundant.
 *                                                                           
 * This file is part of Syntaxalyser.                                         
 *                                                                            
 * PcapCrack is free software: you can redistribute it and/or modify        
 * it under the terms of the GNU General Public License as published by       
 * the Free Software Foundation, either version 3 of the License, or          
 * (at your option) any later version.                                        
 *                                                                            
 * PcapCrack is distributed in the hope that it will be useful,             
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              
 * GNU General Public License for more details.                               
 *                                                                            
 * You should have received a copy of the GNU General Public License          
 * along with PcapCrack.  If not, see <http://www.gnu.org/licenses/>.       
 */

/**
 ** This class represents the syntax analyser itself.
 **/
 
import java.io.*;

/**
 **   @author  Ben Goldsworthy (rumperuu) <me+syntaxalyser@bengoldsworthy.uk>
 **   @version 1.0
 **/
public class SyntaxAnalyser extends AbstractSyntaxAnalyser {
	LexicalAnalyser lex ;
	Token nextToken ;
	Generate myGenerate = null;
   
   /**
    **   Constructor function. Creates a new Lexical Analyser using the given
    **   filename.
    **
    **   @param fileName The name of the file to syntactically analyse.
    **/
   public SyntaxAnalyser(String fileName) throws IOException {
      lex = new LexicalAnalyser(fileName);
   }
   
	/**
	 **   Begin syntax analysis at the distinguished symbol token `<statement part>`.
	 **/
	public void _statementPart_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<statement part>");
         acceptTerminal(Token.beginSymbol);
         _statementList_();
         acceptTerminal(Token.endSymbol);
         myGenerate.finishNonterminal("<statement part>");
      } catch (CompilationException ex) {
         String errString = "'<statement part>' at line "+currLine+".";
         throw new CompilationException(errString , ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<statement list>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _statementList_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<statement list>");
         _statement_();
         while (nextToken.symbol == Token.semicolonSymbol) {
            acceptTerminal(Token.semicolonSymbol);
            _statement_();
         }
         myGenerate.finishNonterminal("<statement list>");
      } catch (CompilationException ex) {
         String errString = "'<statement list>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<statement>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _statement_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<statement>");
         switch (nextToken.symbol) {
         case Token.identifier:
            _assignmentStatement_();
            break;
         case Token.ifSymbol:
            _ifStatement_();
            break;
         case Token.whileSymbol:
            _whileStatement_();
            break;
         case Token.callSymbol:
            _procedureStatement_();
            break;
         case Token.doSymbol:
            _untilStatement_();
            break;
         default:
            String errString = "'identifier', '<if statement>', '<while statement>', '<procedure statement>' or '<until statement>' at line "+currLine;
            myGenerate.reportError(nextToken, errString);
            throw new CompilationException(errString);
         }
         myGenerate.finishNonterminal("<statement>");
      } catch (CompilationException ex) {
         String errString = "'<statement>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<assignment statement>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _assignmentStatement_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<assignment statement>");
         acceptTerminal(Token.identifier);
         acceptTerminal(Token.becomesSymbol);
         _assignmentStatementRemainder_();
         myGenerate.finishNonterminal("<assignment statement>");
      } catch (CompilationException ex) {
         String errString = "'<assignment statement>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<assignment statement remainder>`
	 **   non-terminal token, which I had added to the language grammar.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _assignmentStatementRemainder_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<NEW assignment statement remainder>");
         if (nextToken.symbol == Token.stringConstant)
            acceptTerminal(Token.stringConstant);
         else
            _expression_();
         myGenerate.finishNonterminal("<NEW assignment statement remainder>");
      } catch (CompilationException ex) {
         String errString = "'<assignment statement remainder>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<if statement>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _ifStatement_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<if statement>");
         acceptTerminal(Token.ifSymbol);
         _condition_();
         acceptTerminal(Token.thenSymbol);
         _statementList_();
         _ifStatementRemainder_();
         acceptTerminal(Token.endSymbol);
         acceptTerminal(Token.ifSymbol);
         myGenerate.finishNonterminal("<if statement>");
      } catch (CompilationException ex) {
         String errString = "'<if statement>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
        
	/**
	 **   Syntactically analyse the `<if statement remainder>` non-terminal token,
	 **   which I have added to the grammar.
	 **/
   public void _ifStatementRemainder_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<NEW if statement remainder>");
         if (nextToken.symbol == Token.elseSymbol) {
            acceptTerminal(Token.elseSymbol);
            _statementList_();
         }
         myGenerate.finishNonterminal("<NEW if statement remainder>");
      } catch (CompilationException ex) {
         String errString = "'<if statement remainder>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }

	/**
	 **   Syntactically analyse the `<while statement>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _whileStatement_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<while statement>");
         acceptTerminal(Token.whileSymbol);
         _condition_();
         acceptTerminal(Token.loopSymbol);
         _statementList_();
         acceptTerminal(Token.endSymbol);
         acceptTerminal(Token.loopSymbol);
         myGenerate.finishNonterminal("<while statement>");
      } catch (CompilationException ex) {
         String errString = "'<while statement>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<procedure statement>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _procedureStatement_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<procedure statement>");
         acceptTerminal(Token.callSymbol);
         acceptTerminal(Token.identifier);
         acceptTerminal(Token.leftParenthesis);
         _argumentList_();
         acceptTerminal(Token.rightParenthesis);
         myGenerate.finishNonterminal("<procedure statement>");
      } catch (CompilationException ex) {
         String errString = "'<procedure statement>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<until statement>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _untilStatement_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<until statement>");
         acceptTerminal(Token.doSymbol);
         _statementList_();
         acceptTerminal(Token.untilSymbol);
         _condition_();
         myGenerate.finishNonterminal("<until statement>");
      } catch (CompilationException ex) {
         String errString = "'<until statement>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<expression>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _expression_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<expression>");
         _factor_();
         _expressionRemainder_();
         myGenerate.finishNonterminal("<expression>");
      } catch (CompilationException ex) {
         String errString = "'<expression>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<expression remainder>` non-terminal token,
	 **   which I have added to the language grammar.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _expressionRemainder_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<NEW expression remainder>");
         switch (nextToken.symbol) {
         case Token.plusSymbol:
            acceptTerminal(Token.plusSymbol);
            _factor_();
            break;
         case Token.minusSymbol:
            acceptTerminal(Token.minusSymbol);
            _factor_();
            break;
         case Token.timesSymbol:
            acceptTerminal(Token.timesSymbol);
            _factor_();
            break;
         case Token.divideSymbol:
            acceptTerminal(Token.divideSymbol);
            _factor_();
            break;
         case Token.rightParenthesis:
         case Token.semicolonSymbol:
            break;
         default:
            String errString = "'+', '-', '*', '/', ')' or ';' at line "+currLine;
            myGenerate.reportError(nextToken, errString);
            throw new CompilationException(errString);
         }
         myGenerate.finishNonterminal("<NEW expression remainder>");
      } catch (CompilationException ex) {
         String errString = "'<NEW expression remainder>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<factor>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _factor_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<factor>");
         switch(nextToken.symbol) {
         case Token.identifier:
            acceptTerminal(Token.identifier);
            break;
         case Token.numberConstant:
            acceptTerminal(Token.numberConstant);
            break;
         case Token.leftParenthesis:
            acceptTerminal(Token.leftParenthesis);
            _expression_();
            acceptTerminal(Token.rightParenthesis);
            break;
         default:
            String errString = "'identifier', 'numberConstant' or '(' at line "+currLine;
            myGenerate.reportError(nextToken, errString);
            throw new CompilationException(errString);
         }
         myGenerate.finishNonterminal("<factor>");
      } catch (CompilationException ex) {
         String errString = "'<factor>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   } 
   
	/**
	 **   Syntactically analyse the `<argument list>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _argumentList_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<argument list>");
         acceptTerminal(Token.identifier);
         while (nextToken.symbol == Token.commaSymbol) {
            acceptTerminal(Token.commaSymbol);
            _argumentList_();
         }
         myGenerate.finishNonterminal("<argument list>");
      } catch (CompilationException ex) {
         String errString = "'<argument list>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   } 
   
	/**
	 **   Syntactically analyse the `<condition>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _condition_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<condition>");
         acceptTerminal(Token.identifier);
         _conditionalOperator_();
         _conditionRemainder_();
         myGenerate.finishNonterminal("<condition>");
      } catch (CompilationException ex) {
         String errString = "'<condition>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<condition remainder>` non-terminal token,
	 **   which I have added to the language grammar.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _conditionRemainder_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<NEW condition remainder>");
         switch (nextToken.symbol) {
         case Token.identifier:
            acceptTerminal(Token.identifier);
            break;
         case Token.numberConstant:
            acceptTerminal(Token.numberConstant);
            break;
         case Token.stringConstant:
            acceptTerminal(Token.numberConstant);
            break;
         default:
            String errString = "'identifier', 'numberConstant' or 'stringConstant' at line "+currLine;
            myGenerate.reportError(nextToken, errString);
            throw new CompilationException(errString);
         }
         myGenerate.finishNonterminal("<NEW condition remainder>");
      } catch (CompilationException ex) {
         String errString = "'<NEW condition remainder>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Syntactically analyse the `<conditional operator>` non-terminal token.
	 **
	 **	@throws IOException in the event that the `LexicalAnalyser` can no
	 **		longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails
	 **/
   public void _conditionalOperator_() throws IOException, CompilationException {
      int currLine = nextToken.lineNumber;
		try {
         myGenerate.commenceNonterminal("<conditional operator>");
         switch (nextToken.symbol) {
         case Token.greaterThanSymbol:
            acceptTerminal(Token.greaterThanSymbol);
            break;
         case Token.greaterEqualSymbol:
            acceptTerminal(Token.greaterEqualSymbol);
            break;
         case Token.equalSymbol:
            acceptTerminal(Token.equalSymbol);
            break;
         case Token.notEqualSymbol:
            acceptTerminal(Token.notEqualSymbol);
            break;
         case Token.lessThanSymbol:
            acceptTerminal(Token.lessThanSymbol);
            break;
         case Token.lessEqualSymbol:
            acceptTerminal(Token.lessEqualSymbol);
            break;
         default:
            String errString = "'>', '>=', '=', '/=', '<' or '<=' at line "+currLine;
            myGenerate.reportError(nextToken, errString);
            throw new CompilationException(errString);
         }
         myGenerate.finishNonterminal("<conditional operator>");
      } catch (CompilationException ex) {
         String errString = "'<conditional operator>' at line "+currLine+".";
         throw new CompilationException(errString, ex);
      }
   }
   
	/**
	 **   Accepts an expected terminal symbol and tests that the current token
	 **   matches what is expects. If not, throws a `CompilationError` with
	 **   details of the discrepancy between the expected and found token.
	 **  
	 **   @param symbol The symbol to expect at the next token.
	 **   @throws IOException in the event that the `LexicalAnalyser` can no
	 **      longer read.
	 **   @throws CompilationException in the event that the syntax analysis
	 **      fails.
	 **/
	public void acceptTerminal(int symbol) throws IOException, CompilationException {
      if (nextToken.symbol == symbol) {
         myGenerate.insertTerminal(new Token(symbol, nextToken.text, nextToken.lineNumber));
         nextToken = lex.getNextToken();
      } else {
         String errString = "'"+Token.getName(symbol)+"' at line "+nextToken.lineNumber;
         myGenerate.reportError(nextToken, errString);
      }
   }
	
	/**
	 **   Parses the given `PrintStream` with this instance's `LexicalAnalyser`.
	 **
	 **   @param ps The `PrintStream` object to read tokens from.
	 **   @throws IOException in the event that the `PrintStream` object can no
	 **      longer read.
	 **/
	public void parse(PrintStream ps) throws IOException {
		myGenerate = new Generate();
		try {
			nextToken = lex.getNextToken();
			_statementPart_();
			acceptTerminal(Token.eofSymbol);
			myGenerate.reportSuccess();
		} catch(CompilationException ex) {
			ps.println("Compilation Exception");
			ps.println(ex.toTraceString());
		}
	}
}
