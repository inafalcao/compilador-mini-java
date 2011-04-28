package compiladorMiniJava;

import java.util.Scanner;

import parser.ParseException;





public class compiladorMiniJava {
	
	public static void main(String[] args) throws ParseException{
		Scanner input = new Scanner(System.in);
			int op = 0;
		do{
			
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
				Factorial.factorial(); 
				break;
			case 2:
				BinaryTree.binaryTree();
				break;
			case 3:
				BinarySearch.binarySearch();
				break;
			case 4:
				BubbleSort.bubbleSort();     
				break;
			case 5:
				LinearTree.linearTree();	
				break;
			case 6:
				TreeVisitor.treeVisitor();
				break;
			default:
				System.out.println("VALOR INVALIDO"); 
				break;
			}//fim switch   
		}while(op != 7);	
	}//ends main

}
