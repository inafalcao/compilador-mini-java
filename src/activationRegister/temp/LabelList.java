package activationRegister.temp;

import util.List;


public class LabelList extends List<Label> {
   

	public LabelList(Label h, LabelList t) {
	   super(h, t);
   }

	public String print() {
		String rt = new String();
		for(LabelList l = this; l != null; l = (LabelList) l.tail ){
			rt += l.head.print()+ " ";
		}
		return rt;
	}
	
	@Override
	public String toString(){
		String rt = new String();
		for(LabelList l = this; l != null; l = (LabelList) l.tail ){
			rt += l.head.print()+ " ";
		}
		return rt;
	}

}

