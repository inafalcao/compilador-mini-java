package treeIR;

import util.List;

public class ExpList extends List<Exp>{
  
	public ExpList(Exp exp, ExpList t) {
		super(exp, t);
	}

	public ExpList(java.util.List<Exp> args) {
		if(args.size() > 1){
			head = args.get(0);
			args.remove(0);
			tail = new ExpList(args);
		}
  }

	public ExpList(ExpList args) {
		head = args.head;
		tail = args.tail;
	}

	public void addHead(Exp e) {
		if(e != null){
			tail = new ExpList(head, (ExpList) tail);
			head = e;
		}
	}
	
	public String print() {
		ExpList aux = this;
		String retorno = "";
		while(aux!=null){
			if(aux.head!=null) retorno = retorno+aux.head.print();
			aux=(ExpList)aux.tail;
		}
		return retorno;
	}
}



