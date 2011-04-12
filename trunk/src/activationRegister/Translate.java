package activationRegister;

import java.util.HashMap;
import symbolTable.Class;
import symbolTable.Symbol;
import symbolTable.SymbolTable;
import syntaxtree.*;
import activationRegister.temp.Label;
import activationRegister.temp.LabelList;
import activationRegister.temp.Temp;
import activationRegister.util.BoolList;
import activationRegister.util.Ex;
import activationRegister.util.Exp;
import activationRegister.util.Nx;
import treeIR.BINOP;
import treeIR.CALL;
import treeIR.CJUMP;
import treeIR.CONST;
import treeIR.ESEQ;
import treeIR.ExpList;
import treeIR.JUMP;
import treeIR.LABEL;
import treeIR.MEM;
import treeIR.MOVE;
import treeIR.NAME;
import treeIR.SEQ;
import treeIR.Stm;
import treeIR.TEMP;
import util.List;
import visitor.TranslateVisitor;

public abstract class Translate implements TranslateVisitor {


	private SymbolTable symbolTable;

	private Frame frame;

	private FragList fragList;

	private String currClass = "";

	private String currMethod = "";

	private Frame currFrame;

	private HashMap<Symbol, Exp> vars; 
	
	public Translate(SymbolTable table, Frame frame){
		symbolTable = table;
		fragList = new FragList();
		this.frame = frame;
		vars = new HashMap<Symbol, Exp>();
	}
	
	public FragList getFragList(){
		return fragList;
	}
	
	private void addFrag(Stm stm){		
		fragList.add(new Frag(currFrame,stm));
	}
	

	public Exp visit(Program n) {
		n.m.accept(this);
		for(int i=0;i<n.cl.size();i++) 
        	n.cl.elementAt(i).accept(this);
		return null;
	}

	public Exp visit(MainClass n) {
		currClass = n.i1.s;
		currFrame = frame.newFrame(Symbol.symbol("public static void main"), new BoolList(false, null));
		addFrag(n.s.accept(this).unNx());
		currClass = "";
		return null;
	}

	public Exp visit(ClassDeclSimple n) {
		for(int i=0;i<n.vl.size();i++) 
        	n.vl.elementAt(i).accept(this);
		currClass=n.i.s;                
        for(int i=0;i<n.ml.size();i++) 
        	n.ml.elementAt(i).accept(this);
        currClass="";
		return null;
	}

	public Exp visit(ClassDeclExtends n) {
		currClass=n.i.s;                
        for(int i=0;i<n.ml.size();i++) 
        	n.ml.elementAt(i).accept(this);
        currClass="";
		return null;
	}

	public Exp visit(VarDecl n) {
		Access access = currFrame.allocLocal(false);
		Ex exp = new Ex(access.exp(new TEMP(currFrame.FP())));
		Symbol meth = Symbol.symbol(currMethod+"$"+n.i.s); 
		vars.put(meth, exp);
		return exp;
	}

	public Exp visit(MethodDecl n) {
		Stm varDeclStm = null, stm = null;
		currMethod = n.i.s;
		//aloca espaco para parametros formais
		BoolList boolList = new BoolList(false, null); //argumento this;
		for(int i=0; i<n.fl.size(); i++){
			boolList = new BoolList(false,boolList);
		}
		currFrame = frame.newFrame(Symbol.symbol(currClass+"$"+n.i.s), boolList);
		for(int i=0; i<n.fl.size(); i++){
			n.fl.elementAt(i).accept(this);
		}
		//chama visitor para variaveis locais e faz uma sequencia de comandos de declaracao de variaveis
		if(n.vl.size()>0){
			varDeclStm = n.vl.elementAt(0).accept(this).unNx();
			for(int i=1; i<n.vl.size(); i++)  varDeclStm = new SEQ(varDeclStm, n.vl.elementAt(i).accept(this).unNx());
		}
		//cria statements para os comandos do corpo do metodo
		if(n.sl.size()>0){
			if(varDeclStm==null){
				stm = n.sl.elementAt(0).accept(this).unNx();
			}
			else stm = new SEQ(varDeclStm, n.sl.elementAt(0).accept(this).unNx());
			for(int i=1; i<n.sl.size(); i++)  stm = new SEQ(stm, n.sl.elementAt(i).accept(this).unNx());
		}
		if(varDeclStm==null && stm==null) return new Ex(new CONST(0));
		currMethod = "";
		addFrag(stm);
		return new Nx(stm);
	}

	public Exp visit(Formal n) {
		Access access = currFrame.allocLocal(false);
		Ex exp = new Ex(access.exp(new TEMP(currFrame.FP())));
		Symbol meth = Symbol.symbol(currMethod+"$"+n.i.s); 
		vars.put(meth, exp);
		return exp;
	}

	public Exp visit(IntArrayType n) {
		return new Ex(new CONST(0));
	}

	public Exp visit(BooleanType n) {
		return new Ex(new CONST(0));
	}

	public Exp visit(IntegerType n) {
		return new Ex(new CONST(0));
	}

	public Exp visit(IdentifierType n) {
		return new Ex(new CONST(0));
	}

	public Exp visit(Block n) {
		Stm stm = null;		
		if(n.sl.size()>0){
			stm = n.sl.elementAt(0).accept(this).unNx();
			for(int i=1; i<n.sl.size(); i++)  stm = new SEQ(stm, n.sl.elementAt(i).accept(this).unNx());
		}
		if(stm==null) return new Ex(new CONST(0));		
		return new Nx(stm);
	}

	public Exp visit(If n) { 
		Label t = new Label();
		Label f = new Label();
		Label join = new Label();
		return new Nx(new SEQ( new CJUMP(CJUMP.EQ, n.e.accept(this).unEx(), new CONST(1), t, f),
						new SEQ(new LABEL(f), 
								new SEQ( n.s2.accept(this).unNx(),
										new SEQ( new JUMP(new NAME(join),new LabelList(join,null)),
												new SEQ(new LABEL(t), 
														new SEQ(n.s1.accept(this).unNx(), new LABEL(join))))))));
	}

	public Exp visit(While n) {
		Label test = new Label();
		Label t = new Label();
		Label f = new Label();
	      return new Nx(new SEQ
                  (new SEQ
                   (new SEQ(new LABEL(test),
                        (new CJUMP(CJUMP.EQ, n.accept(this).unEx(), 
                                        new CONST(1),t,f))),
                    (new SEQ( new LABEL(t),n.s.accept(this).unNx()))),
                    new LABEL(f)));
	}

	public Exp visit(Print n) {
		return new Ex(currFrame.externalCall("print", new ExpList(n.e.accept(this).unEx(), null)));
	}

	public Exp visit(Assign n) {
		return new Nx(new MOVE(getAcess(Symbol.symbol(currMethod+"$"+n.i.s)).unEx(), n.e.accept(this).unEx()));
	}

	public Exp visit(ArrayAssign n) {
		return new Nx(new MOVE(new MEM(new BINOP(BINOP.PLUS,getAcess(Symbol.symbol(currMethod+"$"+n.i.s)).unEx(), n.e1.accept(this).unEx())), n.e2.accept(this).unEx()));
	}

	public Exp visit(And n) {
		return new Ex(new BINOP(BINOP.AND,n.e1.accept(this).unEx(),n.e2.accept(this).unEx()));
	}

	public Exp visit(LessThan n) {
		Label t = new Label();
		Label f = new Label();
		Temp r = new Temp();
		return new Ex(new ESEQ (new SEQ( new MOVE(new TEMP(r), new CONST(1)), 
					new SEQ( new CJUMP(CJUMP.LT, n.e1.accept(this).unEx(), n.e2.accept(this).unEx(), t, f),
						new SEQ(new LABEL(f), 
							new SEQ(new MOVE(new TEMP(r), new CONST(0)),
								new LABEL(t))))), new TEMP(r)));
	}

	public Exp visit(Plus n) {
		return new Ex(new BINOP(BINOP.PLUS,n.e1.accept(this).unEx(),n.e2.accept(this).unEx()));
	}

	public Exp visit(Minus n) {
		return new Ex(new BINOP(BINOP.MINUS,n.e1.accept(this).unEx(),n.e2.accept(this).unEx()));
	}

	public Exp visit(Times n) {
		return new Ex(new BINOP(BINOP.MUL,n.e1.accept(this).unEx(),n.e2.accept(this).unEx()));
	}

	public Exp visit(ArrayLookup n) {
		return new Ex(new BINOP(BINOP.PLUS,n.e1.accept(this).unEx(), n.e2.accept(this).unEx()));
	}

	public Exp visit(ArrayLength n) {
		//a primeira posicao do vetor 
		return new Ex(new BINOP(BINOP.PLUS,n.e.accept(this).unEx(), new CONST(0)));
	}

	public Exp visit(Call n) {
		Exp caller = n.e.accept(this);
		ExpList expList = new ExpList(caller.unEx(), null);
		for(int i=n.el.size()-1; i>=0; i--){
			expList = new ExpList(n.el.elementAt(i).accept(this).unEx(), expList);
		}
		return new Ex(new CALL(caller.unEx(), expList));
	}


	public Exp visit(IntegerLiteral n) {
		return new Ex(new CONST(n.i));
	}

	public Exp visit(True n) {
		return new Ex(new CONST(1));
	}

	public Exp visit(False n) {
		return new Ex(new CONST(0));
	}

	public Exp visit(IdentifierExp n) {
		Symbol symbol = Symbol.symbol(currMethod+"$"+n.s);
		Exp exp =  getAcess(symbol);
		if(exp == null)
			exp = getAcess(Symbol.symbol(""+"$"+n.s));
		return new Ex(exp.unEx());
	}

	public Exp visit(This n) {
		List<Access> accList = currFrame.formals;
		Access thisAccess = accList.head;
		
		while(accList.tail!=null){
			thisAccess = accList.head;
			accList = accList.tail;
		}
		
		return new Ex(thisAccess.exp(new TEMP(currFrame.FP())));
	}

	public Exp visit(NewArray n) {
		return new Ex(currFrame.externalCall("initArray", new ExpList(new BINOP(BINOP.MUL,new BINOP(BINOP.PLUS, n.e.accept(this).unEx() , new CONST(1)),new CONST(currFrame.wordSize())),null)));
	}

	public Exp visit(NewObject n) {
		Class objClass = symbolTable.getClass(Symbol.symbol(n.i.s));
		return new Ex(currFrame.externalCall("allocRecord", new ExpList(new BINOP(BINOP.MUL,new CONST(objClass.getNumberOfVariables()+1),new CONST(currFrame.wordSize())),null)));
	}

	public Exp visit(Not n) {
		  return new Ex(new BINOP(BINOP.MINUS, new CONST(1), 
                 (n.e.accept(this)).unEx()));
	}

	public Exp visit(Identifier n) {
		return getAcess(Symbol.symbol(currMethod+"$"+n.s));
	}

	private Exp getAcess(Symbol s) {
		Exp exp = vars.get(s);
		return exp;
	}
	
}
