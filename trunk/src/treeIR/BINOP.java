package treeIR;

public class BINOP extends Exp {
	

	public int binop;

	public Exp left, right;

	public BINOP(int b, Exp l, Exp r) {
		binop=b; left=l; right=r; 
	}

	public final static int PLUS=0, MINUS=1, MUL=2, DIV=3, AND=4,OR=5,LSHIFT=6,RSHIFT=7,ARSHIFT=8,XOR=9;

	@Override
	public ExpList kids() {return new ExpList(left, new ExpList(right,null));}

	@Override
	public Exp build(ExpList kids) {
		return new BINOP(binop,kids.head,kids.tail.head);
	}

	@Override
	public String print() {
		String str = "BINOP( "+ left.print();
		switch(binop){
		case PLUS: str += "+ ";
			break;
		case MINUS: str += "- ";
			break;
		case MUL: str += "* ";
			break;
		case DIV: str += "/ ";
			break;
		case AND: str += "&& ";
			break;
		case OR: str += "|| ";
			break;
		case LSHIFT: str += "< ";
			break;
		case RSHIFT: str += "> ";
			break;
		case ARSHIFT: str += "<> ";
			break;
		case XOR: str += "xor ";
			break;
		}
		str += right.print()+" );";
		return str;
	}
}

