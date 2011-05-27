package activationRegister.temp;

import java.util.LinkedList;
import util.List;

public class TempList extends List<Temp>{


	public TempList(Temp h, TempList t) {
		super(h, t);
	}


	public TempList() {
		super();
	}


	public TempList(LinkedList<Temp> l) {
		head = l.get(l.size()-1);
		for(int i = l.size()-2; i > 0; i-- ){
			tail = new TempList(head, (TempList)tail);
			head = l.get(i);
		}
	}


	@Override
	public String toString(){
		String rt = new String();
		for(TempList l = this; l != null; l = (TempList) l.tail ){
			rt += l.head.toString()+ " ";
		}
		return rt;
	}
}

