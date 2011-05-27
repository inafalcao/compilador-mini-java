package treeIR;


public class SEQ extends Stm {

	public Stm left, right;
  
	public SEQ(Stm l, Stm r) { 
		left=l; right=r; 
	}
	  

	@Override
	public ExpList kids() {
		throw new Error("kids() not applicable to SEQ");
	}
	 

	@Override
	public Stm build(ExpList kids) {throw new Error("build() not applicable to SEQ");}

	@Override
	public String print() {
		String rt = "SEQ( "+left.print() + ", "+ right.print()+ " );";
		return rt;
	}
}

