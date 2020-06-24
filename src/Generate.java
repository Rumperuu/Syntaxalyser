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
 ** This class represents the generator for the output file(s) produced when
 ** running Syntaxalyser.
 **/
 
/**
 **   @author  Ben Goldsworthy (rumperuu) <me+syntaxalyser@bengoldsworthy.uk>
 **   @version 1.0
 **/
public class Generate extends AbstractGenerate {
   /**
    **   Takes a token for a terminal and prints it to the output.
    **
    **   @param token The terminal token.
    **/
   public void insertTerminal(Token token) {
      String tt = Token.getName(token.symbol);
     
      if((token.symbol == Token.identifier) || 
         (token.symbol == Token.numberConstant) || 
         (token.symbol == Token.stringConstant)) {
         tt += " '" + token.text + "'";
      }

      tt += " on line " + token.lineNumber;

      System.out.println("rggTOKEN " + tt);
   }

   /**
    **   Prints the commencement of parsing a non-terminal.
    **
    **   @param name The name of the non-terminal.
    **/
   public void commenceNonterminal( String name ) {
      System.out.println( "rggBEGIN " + name );
   }

   /**
    **   Prints the finishing of parsing a non-terminal.
    **
    **   @param name The name of the non-terminal.
    **/
   public void finishNonterminal( String name ) {
      System.out.println( "rggEND " + name );
   }

   /**
    **   Reports that the file was successfully parsed.
    **/
   public void reportSuccess() {
      System.out.println( "rggSUCCESS" );
   }

   /**
    **   Reports that an error was encountered whilst parsing the file. Includes
    **   what was expect and what was found, along with where in the file the
    **   error ocurred.
    **
    **   @param token The token for which the error ocurred.
    **   @param explanatoryMessage A message for the error report.
    **   @throws CompilationException up the call stack until it reaches the
    **      `SyntaxAnalyser`'s `parse()` method for a stack trace.
    **/
   public void reportError(Token token, String explanatoryMessage) throws CompilationException {
      System.out.println("rggCOMPILATION_EXCEPTION");
      System.out.println("rggEXPECTED "+explanatoryMessage+", found: '"+token.text+"'");
      throw new CompilationException("expected "+explanatoryMessage+", found: '"+token.text+"'");
   }
}
