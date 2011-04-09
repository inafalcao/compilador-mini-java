package compiladorMiniJava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;


import parser.MiniJavaParser;
import parser.ParseException;
import typeCheking.*;



import symbolTable.*;
import syntaxtree.Program;

public class compiladorMiniJava {
	
	static File in;
	static File out;
	
	
	static MiniJavaParser parser;
	static SymbolTable table;
	static CheckType checkType;
	
	
	
	public static void main(String[] args) throws ParseException{
		
		//reading file
		try {
			Reader in = new FileReader("program");
			parser = new MiniJavaParser(in);
			Program prog = parser.Program();
			
			table = new SymbolTableVisitor().buildSymbolTable(prog);
			
			System.out.println("Passou");
			checkType = new CheckType(table);
			
			SymbolTable.print();
			
			//checkType.visit(prog);
			
			in.close();//close file
		} catch (IOException e) {
			System.out.println("Problemas para ler o arquivo");
		}//ends catch
		
		
		
		
	}//ends main

}
