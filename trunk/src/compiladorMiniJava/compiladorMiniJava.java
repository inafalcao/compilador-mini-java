package compiladorMiniJava;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import error.Error;

import parser.MiniJavaParser;
import parser.ParseException;
import activationRegister.Frame;
import activationRegister.Translate;
import typeCheking.*;
import visitor.SymbolTableVisitor;


import symbolTable.*;
import syntaxtree.Program;

public class compiladorMiniJava {
	
	static File in;
	static File out;
	
	
	static MiniJavaParser parser;
	static SymbolTable table;
	static CheckType checkType;
	static Frame frame;
	static Translate translate;
	
	
	public static void main(String[] args) throws ParseException{
		
		//reading file
		try {
			Reader in = new FileReader("files/Factorial.java");
			
			//fase de parser
			parser = new MiniJavaParser(in);
			Program prog = parser.Program();
			
			//fase analise semantica
			table = new SymbolTableVisitor().buildSymbolTable(prog);
			SymbolTable.print();
			
			//fase verificacao de tipo
			checkType = new CheckType(table);
			checkType.visit(prog);
			if(Error.getInstance().hasErro())Error.getInstance().print();
			else{
				System.out.println();
				System.out.println("COMPILOU!");
			}
			
			//fase traducao
			
			in.close();//close file
		} catch (IOException e) {
			System.out.println("Problemas para ler o arquivo");
		}//ends catch
		
	}//ends main

}
