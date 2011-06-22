package error;

import java.util.ArrayList;

public class Error {
	
	public class SingleErro{
		//variable to control the message
		public String msg;
		//variable to control the begin's line
		public int beginLine;
		//variable to control the end's line	
		public int endLine;
		
        //constructor
		public SingleErro(String erro, int beginLine, int endLine){
			msg = erro;
			this.beginLine = beginLine;
			this.endLine = endLine;
		}
		
		public SingleErro(String erro, int beginLine){
			msg = erro;
			this.beginLine = beginLine;
			this.endLine = -1;
		}
		
		public SingleErro(String erro){
			msg = erro;
			this.beginLine = -1;
			this.endLine = -1;
		}
		
		//says the range where the errors
		@Override
		public String toString(){
			String msg = new String();
			if(beginLine > -1)
				msg = "Na linha:" + (beginLine + 1);
			if(endLine > -1)
				msg += "Ate a linha:"+ (endLine + 1);
			msg += ".Erro: "+this.msg;
			return msg;
		}
	}
	
	//vector's errors
	private ArrayList<SingleErro> erros;
	
	
	private static Error instance = null;
	
	
	private Error(){
		erros = new ArrayList<SingleErro>();
	}
	
	
	public static Error getInstance(){
		if (instance == null){
			instance = new Error();
		}
		return instance;
	}
	
	//methods to add the error
	public void addErro(String erro, int beginLine, int endLine){
		SingleErro e = new SingleErro(erro, beginLine, endLine);
		erros.add(e);
	}
	
	public void addErro(String erro, int beginLine){
		SingleErro e = new SingleErro(erro, beginLine);
		erros.add(e);
	}
	
	public void addErro(String erro){
		SingleErro e = new SingleErro(erro);
		erros.add(e);
	}
	
	//verifies is has error
	public boolean hasErro(){
		return !erros.isEmpty();
	}
	     
	//string's size (percorre o erro)
	@Override
	public String toString(){
		String e = new String();
		for(int i = 0; i < erros.size(); i++){
			e+= erros.get(i).toString()+"\n";
		}
		return e;
	}

	//to print the string with error
	public void print() {
		System.out.print(instance.toString());
	}
}