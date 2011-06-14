package compiladorMiniJava;

import translate.*;
import jouette.*;
import instructionSelection.Assem.*;
import canon.BasicBlocks;
import canon.StmListList;
import canon.TraceSchedule;
import java.io.FileInputStream;

import parser.MiniJavaParser;
import parser.ParseException;
import typeCheking.*;
import visitor.SymbolTableVisitor;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.List;
import java.util.Scanner;

import error.Error;
import flow_graph.AssemFlowGraph;
import translate.Translate;
import treeIR.CALL;
import treeIR.TEMP;

import reg_alloc.InterferenceGraph;
import reg_alloc.Liveness;
import reg_alloc.RegAlloc;
import symbolTable.*;
import syntaxtree.Program;

import java.util.Iterator;
import java.io.PrintWriter;

public class compiladorMiniJava {
	static MiniJavaParser parser;
	static SymbolTable table;
	static CheckType checkType;
	static Frame frame;
	static Translate translate;

	public static void main(String[] args) throws ParseException{
		boolean podeContinuar =false;
		try {
			Reader in = null;
			boolean saiFora = false;
			Scanner input = new Scanner(System.in);
			int op = 0;
		
			System.out.println("BEM VINDO AO MINIJAVA COMPILADOR");
			System.out.println("@AUTORES: CAMILA FERREIRA / DANUSA RIBEIRO/ LUCAS ASSUNÇÃO / PAULO ALVES");
			System.out.println("O QUE DESEJA COMPILAR?");
			System.out.println("1 -- FACTORIAL");
			System.out.println("2 -- BINARY TREE");
			System.out.println("3 -- BINARY SEARCH");
			System.out.println("4 -- BUBBLE SHORT");
			System.out.println("5 -- LINEAR SEARCH");
			System.out.println("6 -- TREE VISITOR");
			System.out.println("7 -- SAIR");
			System.out.print(">> ");
			op = input.nextInt();
			switch(op){
				case 1:
					in = new FileReader("files/Factorial.java"); 
					break;
				case 2:
					in = new FileReader("files/binarytree.java");
					break;
				case 3:
					in = new FileReader("files/Binarysearch.java");
					break;
				case 4:
					in = new FileReader("files/bubblesort.java");   
					break;
				case 5:
					in = new FileReader("files/linearsearch.java");	
					break;
				case 6:
					in = new FileReader("files/treevisitor.java");
					break;
				case 7 : saiFora = true; break;
				default:
					saiFora = true;
					System.out.println("VALOR INVALIDO"); 
					break;
			}
	
			if(saiFora) return;
			
			//fase de parser
			parser = new MiniJavaParser(in);
			Program prog = MiniJavaParser.Program();
		
			//fase analise semantica
			table = new SymbolTableVisitor().buildSymbolTable(prog);
			SymbolTable.print();
		
			//fase verificacao de tipo
			checkType = new CheckType(table);
			checkType.visit(prog);
			if(Error.getInstance().hasErro())Error.getInstance().print();
			else{
				System.out.println();
				System.out.println("\n* O CÓDIDO COMPILOU!");
				podeContinuar = true;
			}
			
			if(!podeContinuar) return;
		
			//fase traducao
			frame = new Frame();
			
			translate = new Translate(table, frame);
			translate.visit(prog);
			FragList frags = translate.getFragList();
			
			frags = TraceSchedule.canonlize(frags);
			StmListList stml = null;
			StmListList aux;
			for(int i = 0 ; i < frags.size(); i++){
				Frag f = frags.get(i);
				aux = new BasicBlocks(f.getBody()).blocks;
				int j = 0;
				System.out.println("~~~~~~~~~~~~~ INICIO FRAG "+i+" ~~~~~~~~~~~~~~~");
				
				for(; aux!= null; aux = (StmListList) aux.tail){
					System.out.println("* Bloco "+j+":");
					j++;
					aux.head.print();
					stml = new StmListList(aux.head, stml);
					if(aux.tail!=null) System.out.println("----------------------------");
				}
				
				
				InstrList inst = frame.codegen(TraceSchedule.generateCode(stml));
				
				System.out.println("\n* SELEÇÃO DE INSTRUÇÕES: ");
				inst.print(inst);
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				
				System.out.println("\n* GRAFO DE FLUXO: ");
				AssemFlowGraph graph = new AssemFlowGraph(inst);
				graph.print();
				
				System.out.println("~~~~~~~~~~~~~~~ FIM FRAG "+i+" ~~~~~~~~~~~~~~~~");
				
			}
		
			
			/*
		
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Análise de Longevidade: ");
			Liveness liveness = new Liveness(graph);
			InterferenceGraph inGraph = new InterferenceGraph(liveness);*/
			//RegisterAlocator alocator = new RegisterAlocator(inGraph);
			//map = alocator.mapTempToReg(6);//maquina com  6 registradores
			//out = new File(getFileName(filename)+ ".pm");
			//emitInstr(out, alocator.g.liveness.g.instrs);
			//System.out.println("Compilacao completa com sucesso");

			in.close();//close file
			} catch (IOException e) {
				System.out.println("Problemas para ler o arquivo");
			}//ends catch
	}
}
