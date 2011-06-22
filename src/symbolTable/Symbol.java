package symbolTable;

import java.util.HashMap;//necessary to the reverse mapping
import java.util.Iterator;


public class Symbol{
	

	private static int id = 0;
	
	private int value;
	//this next line implements a dictionary
	private static HashMap<String , Symbol> dict = new HashMap<String, Symbol>();
	
	
	private Symbol(int value){
		this.value = value;
	}
	
	public static boolean symbolCheck(String name){
		if(dict.containsKey(name)) return true;
		return false;
	}
	
	//verifies if the symbol is in dictionary - pag 99
	public static Symbol symbol(String name){
		if(dict.containsKey(name)) return dict.get(name);
		else{
			Symbol s = new Symbol(getID());
			dict.put(name, s);
			return s;
		}
	}
	
	private static int getID(){
		id++;
		return id - 1;
	}
	
	//method to compare the symbols for equality - pag 98
	public int compareTo(Object s){
		if(this.value == ((Symbol)s).value) return 0;
		return 1;
	}
	
	
	@Override
	public boolean equals(Object s){
		return this.value == ((Symbol)s).value;
	}
	
	@Override
	public String toString(){
		String name;
		Iterator<String> it = dict.keySet().iterator();
		while(it.hasNext()){
			name = it.next();
			if(dict.get(name).value == value) return name;
		}
		name = "Identificador nao eh valido";
		return name;
	}
	
}
