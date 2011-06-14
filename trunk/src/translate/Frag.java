package translate;

import treeIR.Stm;
import treeIR.StmList;
import activationRegister.Frame;

/**
 * 
 * */
public class Frag {

	/***/
	private Frame frame;
	/***/
	private StmList treeStm;
	
	/**
	 * 
	 * */
	public Frag(Frame frame, Stm treeStm){
		this.frame = frame;
		this.treeStm = new StmList(treeStm, null);
	}//fim construtor

	/**
	 * 
	 * */
	public StmList getBody() {
		return treeStm;
	}

	/**
	 * 
	 * */
	public void setBody(StmList linearize) {
		treeStm = linearize;
	}

	/**
	 * 
	 * */
	public String print() {
		String fg = new String();
		StmList aux = treeStm;
		for(; aux != null; aux = (StmList) aux.tail){
			fg += aux.head.print()+ "\n ";
		}
		fg += "\n *******************************************\n";
		return fg;
	}
	
}//fim classe