package instructionSelection.Assem;

import util.List;

public class InstrList extends List<Instr>{

	public InstrList(Instr head, InstrList tail) {
		super(head, tail);
	}

	public InstrList() {
		super();
	}

	public void add(InstrList list) {
		InstrList aux = list;
		for(; aux != null; aux = (InstrList) aux.tail){
			tail = new InstrList(head, (InstrList) tail);
			head = aux.head;
		}
	}
	
	public void print(InstrList aux){
		if(aux!=null){
			System.out.println(aux.head.assem);
			print((InstrList)aux.tail);
		}
	}
	
}
