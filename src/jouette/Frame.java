package jouette;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ListIterator;
import activationRegister.Access;
import activationRegister.AccessList;

import instructionSelection.Assem.Instr;
import instructionSelection.Assem.InstrList;
import instructionSelection.Assem.OPER;

import symbolTable.Symbol;
import activationRegister.temp.Label;
import activationRegister.temp.Temp;
import activationRegister.temp.TempList;
import activationRegister.util.BoolList;
import treeIR.BINOP;
import treeIR.CALL;
import treeIR.CJUMP;
import treeIR.CONST;
import treeIR.ESEQ;
import treeIR.EXPSTM;
import treeIR.Exp;
import treeIR.ExpList;
import treeIR.JUMP;
import treeIR.MEM;
import treeIR.MOVE;
import treeIR.NAME;
import treeIR.SEQ;
import treeIR.Stm;
import treeIR.StmList;
import treeIR.TEMP;
import util.List;

public class Frame extends activationRegister.Frame {

        private int offset = 0;
        private static final int wordSize = 4;
        private static boolean spilling = true;
        private static HashMap<Symbol, Integer> functions = new HashMap<Symbol, Integer>();
        private static HashMap<String, Label> labels = new HashMap<String, Label>();
        private static final Label badPtr = new Label("BADPTR");
        private static final Label badSub = new Label("BADSUB");
        int maxArgOffset = 0;
        private static TempList returnSink = null;
        private static Temp[] registers = {};
        static final Temp ZERO = new Temp(); // zero reg
        static final Temp AT = new Temp(); // reserved for assembler
        static final Temp V0 = new Temp(); // function result
        static final Temp V1 = new Temp(); // second function result
        static final Temp A0 = new Temp(); // argument1
        static final Temp A1 = new Temp(); // argument2
        static final Temp A2 = new Temp(); // argument3
        static final Temp A3 = new Temp(); // argument4
        static final Temp T0 = new Temp(); // caller-saved
        static final Temp T1 = new Temp();
        static final Temp T2 = new Temp();
        static final Temp T3 = new Temp();
        static final Temp T4 = new Temp();
        static final Temp T5 = new Temp();
        static final Temp T6 = new Temp();
        static final Temp T7 = new Temp();
        static final Temp S0 = new Temp(); // callee-saved
        static final Temp S1 = new Temp();
        static final Temp S2 = new Temp();
        static final Temp S3 = new Temp();
        static final Temp S4 = new Temp();
        static final Temp S5 = new Temp();
        static final Temp S6 = new Temp();
        static final Temp S7 = new Temp();
        static final Temp FP = new Temp(); // frame-pointer
        static final Temp T8 = new Temp(); // caller-saved
        static final Temp T9 = new Temp();
        static final Temp K0 = new Temp(); // reserved for OS kernel
        static final Temp K1 = new Temp(); // reserved for OS kernel
        static final Temp GP = new Temp(); // pointer to global area
        static final Temp SP = new Temp(); // stack pointer
        static final Temp RA = new Temp(); // return address

        // Register lists: must not overlap and must include every register that
        // might show up in code
        private static final Temp[]
        // registers dedicated to special purposes
                        specialRegs = { ZERO, AT, K0, K1, GP, SP },
                        // registers to pass outgoing arguments
                        argRegs = { A0, A1, A2, A3 },
                        // registers that a callee must preserve for its caller
                        calleeSaves = { RA, S0, S1, S2, S3, S4, S5, S6, S7, FP },
                        // registers that a callee may use without preserving
                        callerSaves = { T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, V0, V1 };
        private static final HashMap<Temp, String> tempMap = new HashMap<Temp, String>(
                        32);
        static {
                tempMap.put(ZERO, "$0");
                tempMap.put(AT, "$at");
                tempMap.put(V0, "$v0");
                tempMap.put(V1, "$v1");
                tempMap.put(A0, "$a0");
                tempMap.put(A1, "$a1");
                tempMap.put(A2, "$a2");
                tempMap.put(A3, "$a3");
                tempMap.put(T0, "$t0");
                tempMap.put(T1, "$t1");
                tempMap.put(T2, "$t2");
                tempMap.put(T3, "$t3");
                tempMap.put(T4, "$t4");
                tempMap.put(T5, "$t5");
                tempMap.put(T6, "$t6");
                tempMap.put(T7, "$t7");
                tempMap.put(S0, "$s0");
                tempMap.put(S1, "$s1");
                tempMap.put(S2, "$s2");
                tempMap.put(S3, "$s3");
                tempMap.put(S4, "$s4");
                tempMap.put(S5, "$s5");
                tempMap.put(S6, "$s6");
                tempMap.put(S7, "$s7");
                tempMap.put(T8, "$t8");
                tempMap.put(T9, "$t9");
                tempMap.put(K0, "$k0");
                tempMap.put(K1, "$k1");
                tempMap.put(GP, "$gp");
                tempMap.put(SP, "$sp");
                tempMap.put(FP, "$fp");
                tempMap.put(RA, "$ra");
        }

        /***/
        private activationRegister.AccessList actuals;{
                LinkedList<Temp> l = new LinkedList<Temp>();
                addAll(l, callerSaves);
                addAll(l, calleeSaves);
                addAll(l, argRegs);
                addAll(l, specialRegs);
                registers = l.toArray(registers);
        }

        /****/
        // Registers defined by a call
        static TempList calldefs;{
                LinkedList<Temp> l = new LinkedList<Temp>();
                l.add(RA);
                addAll(l, argRegs);
                addAll(l, callerSaves);
                calldefs = new TempList(l);
        }
        
        /**
         * 
         * */
        public static Temp[] argRegs() {
                return argRegs;
        }

        /***
         * 
         * */
        public static TempList calldefs() {
                return calldefs;
        }

        /***
         * 
         * */
        public static Temp R0() {
                return ZERO;
        }

        /**
         * 
         * */
        public Frame newFrame(Symbol l, BoolList formals) {
                if (this.name != null) {
                        Symbol aux = Symbol.symbol(this.name + "." + name);
                        name = new Label(aux);
                }
                return new Frame(Symbol.symbol(l.toString()), formals);
        }

        /**
         * 
         * */
        public String tempMap(Temp temp) {
                return tempMap.get(temp);
        }

        /***
         * 
         * 
         * */
        public int wordSize() {
                return wordSize;
        }

        /**
         * 
         * */
        public Temp[] registers() {
                return registers;
        }

        /**
         * 
         * */
        public Temp RV() {
                return V0;
        }

        /**
         * 
         * 
         * */
        public Temp FP() {
                return FP;
        }

        /**
         * 
         * 
         * */
        private static TEMP TEMP(Temp t) {
                return new TEMP(t);
        }

        /***
         * 
         * */
        public Label badPtr() {
                return badPtr;
        }

        /**
         * 
         * */
        public Label badSub() {
        	return badSub;
        }

        /**
         * 
         * */
        private static MOVE MOVE(Exp dst, Exp src) {
        	return new MOVE(dst, src);
        }

        /**
         * 
         * 
         * */
        private static Instr OPER(String assem, TempList dst, TempList src) {
        	return new OPER(assem, dst, src, null);
        }

        /**
         * 
         * */
        private static <R> void addAll(Collection<R> c, R[] a) {
        	for (int i = 0; i < a.length; i++)
                        c.add(a[i]);
        }
        
        {
        	LinkedList<Temp> l = new LinkedList<Temp>();
        	l.add(V0);
        	addAll(l, specialRegs);
        	addAll(l, calleeSaves);
        	returnSink = new TempList(l);
        }

        /**
         * 
         * 
         * */
        public Frame() {
        }

        /**
         * 
         * 
         * */
        private Frame(Symbol n, BoolList f) {
        	Integer count = functions.get(n);
        	if (count == null) {
        		count = new Integer(0);
        		name = new Label(n);
            } else {
            	count = new Integer(count.intValue() + 1);
            	name = new Label(n + "." + count);
            }
        	functions.put(n, count);
        	actuals = new activationRegister.AccessList();
        	formals = new activationRegister.AccessList();
        	if (f != null) {
        		int offset = 0;
        		Iterator<Boolean> escapes = f.iterator();
        		formals.add(allocLocal(escapes.next().booleanValue()));
        		actuals.add(new InReg(V0));
        		for (int i = 0; i < argRegs.length; ++i) {
        			if (!escapes.hasNext())
        				break;
        			offset += wordSize;
        			actuals.add(new InReg(argRegs[i]));
        			if (escapes.next().booleanValue())
        				formals.add(new InFrame(offset));
        			else
        				formals.add(new InReg(new Temp()));
        		}
        		while (escapes.hasNext()) {
        			offset += wordSize;
        			Access actual = new InFrame(offset);
        			actuals.add(actual);
        			if (escapes.next().booleanValue())
        				formals.add(actual);
        			else
        				formals.add(new InReg(new Temp()));
        		}
            }
        }

        /**
         * 
         * 
         * */
        public Access allocLocal(boolean escape) {
        	if (escape) {
        		Access result = new InFrame(offset);
        		offset -= wordSize;
        		return result;
        	} else
        		return new InReg(new Temp());
        }

        /**
         * 
         * 
         * */
        private void assignCallees(int i, StmList body) {
        	if (i >= calleeSaves.length)
        		return;
        	Access a = allocLocal(!spilling);
        	assignCallees(i + 1, body);
        	body.add( MOVE(a.exp(TEMP(FP)), TEMP(calleeSaves[i])));
        	body.add(MOVE(TEMP(calleeSaves[i]), a.exp(TEMP(FP))));
        }

        /**
         * 
         * */
        private void assignFormals(Iterator<Access> formals,
        	Iterator<Access> actuals, StmList body) {
        	if (!formals.hasNext() || !actuals.hasNext())
        		return;
        	Access formal = formals.next();
        	Access actual = actuals.next();
        	assignFormals(formals, actuals, body);
        	body.add( MOVE(formal.exp(TEMP(FP)), actual.exp(TEMP(FP))));
        }

        /**
         * 
         * 
         * */
        public InstrList codegen(StmList stmList) {
        	Codegen codegen = new Codegen(this);
        	InstrList l = null;
        	InstrList instrs = null;
        	List<Instr> aux;
        	for(; stmList!= null; stmList = (StmList) stmList.tail){
        		aux = codegen.codegen(stmList.head);
        		for(; aux!= null; aux = aux.tail){
        			instrs = new InstrList(aux.head, instrs);
    				}
    			}
        	return instrs;
        }

        /**
         * 
         * 
         * */
        public Exp externalCall(String s, ExpList args) {
        	String func = s.intern();
        	Label l = labels.get(func);
                if (l == null) {
                	l = new Label("_" + func);
                	labels.put(func, l);
                }
               
                args.addHead( new CONST(0));
                return new CALL(new NAME(l), new ExpList((ExpList) args));
        }

        /**
         * 
         * 
         * */
        private int countMaxArgs(Stm s) {
        	if (s instanceof SEQ)
        		return Math.max(countMaxArgs(((SEQ) s).left),countMaxArgs(((SEQ) s).right));
        	else if (s instanceof EXPSTM)
        		return countMaxArgs(((EXPSTM) s).exp);
        	else if (s instanceof MOVE)
        		return Math.max(countMaxArgs(((MOVE) s).dst), countMaxArgs(((MOVE) s).src));
        	else if (s instanceof JUMP)
        		return countMaxArgs(((JUMP) s).exp);
        	else if (s instanceof CJUMP)
        		return Math.max(countMaxArgs(((CJUMP) s).left),countMaxArgs(((CJUMP) s).right));
        	else
        		return 0;
        }

        /**
         * 
         * 
         * */
        private int countMaxArgs(Exp e) {
        	if (e instanceof BINOP)
        		return Math.max(countMaxArgs(((BINOP) e).left),countMaxArgs(((BINOP) e).right));
        	else if (e instanceof MEM)
        		return countMaxArgs(((MEM) e).exp);
        	else if (e instanceof ESEQ)
        		return Math.max(countMaxArgs(((ESEQ) e).stm),countMaxArgs(((ESEQ) e).exp));
        	else if (e instanceof CALL) {
        		int i = 0;
        		ExpList l = ((CALL) e).args;
        		while (l != null) {
        			i++;
        			l = (ExpList) l.tail;
        		}
        		return Math.max(i, countMaxArgs(((CALL) e).func));
        	} else
        		return 0;
        }

        /***
         * 
         * 
         * */
        public StmList procEntryExit1(StmList body) {
        	maxArgOffset = countMaxArgs(body.get(0)) * wordSize;
        	assignFormals(formals.iterator(), actuals.iterator(), body);
        	assignCallees(0, body);
        	return body;
        }

        /***
         * 
         * 
         * */
        public InstrList procEntryExit2 (InstrList body) {
        	body.add( OPER("STORE `d0 <- `s0 + " + name + "_framesize\n",new TempList(FP, null), new TempList(SP, null)));
                body.add(OPER("", null, returnSink));
                return body;
        }

        /**
         * 
         * 
         * */
        public InstrList procEntryExit3(InstrList body) {
        	int frameSize = maxArgOffset - offset;
        	ListIterator<Instr> cursor = (ListIterator<Instr>) body.iterator();
        	cursor.add(OPER("\t.text", null, null));
        	cursor.add(OPER(name + ":", null, null));
        	cursor.add(OPER(name + "_framesize=" + frameSize, null, null));
        	if (frameSize != 0) {
        	    cursor.add(OPER("\tsubu $sp " + name + "_framesize",  new TempList(SP, null), new TempList(SP, null)));
        	    body.add(OPER("\taddu $sp " + name + "_framesize",new TempList(SP, null), new TempList(SP, null)));
        	}
        	body.add(OPER("\tj $ra", null, new TempList(RA, null)));
        	return body;
        }

}

