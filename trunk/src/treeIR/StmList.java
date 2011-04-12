package treeIR;

import util.List;

public class StmList extends List<Stm> {

	public StmList(Stm s, StmList sl){
		super(s,sl);
	}

	public void addHead(Stm stm) {
		tail = new StmList(head, (StmList) tail);
		head = stm;
	}

}




