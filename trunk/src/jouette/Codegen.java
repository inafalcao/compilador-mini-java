package jouette;

import activationRegister.temp.Label;
import activationRegister.temp.LabelList;
import activationRegister.temp.Temp;
import activationRegister.temp.TempList;
import treeIR.*;
import util.List;
import instructionSelection.Assem.*;
public class Codegen
{
    Frame frame;
    public Codegen(Frame f)
    {
        frame=f;        
    }

    private List<Instr> ilist=null;
    private List<Instr> last=null;
    TempList L(Temp h, TempList t) {return new TempList(h,t);} 

    private void emit(Instr inst)
    {
        if (last!=null)
            last = last.tail = new List<Instr>(inst,null);
        else 
            last = ilist = new List<Instr>(inst,null);
    }

    List<Instr> codegen(Stm s)
    {
        List<Instr> l;
        munchStm(s);
        l=ilist;
        ilist=last=null;
        return l;
    }
    
    List<Instr> codegen(List<Stm> body)
    {
        List<Instr> l = null, t = null;
        
        for( ; body != null; body = body.tail )
        {
            munchStm(body.head);
            if ( l == null )
                l = ilist;
            else
                t.tail = ilist;
            t = last;
            ilist=last=null;
        }
        return l;
    }
    
  //STATEMENTS	

	void munchStm(Stm s) { 
		  if (s instanceof treeIR.MOVE) munchMove(((treeIR.MOVE)s).dst, ((treeIR.MOVE)s).src); 
		  // CALL, JUMP, CJUMP unimplemented here 
	      else if(s instanceof EXPSTM) munchExp(((EXPSTM)s).exp);
	      else if(s instanceof CJUMP) munchCJump((CJUMP)s);
          else if(s instanceof JUMP) munchJump((JUMP)s);
	      else munchLabel((treeIR.LABEL)s);
	} 
	
	void munchMove(Exp dst, Exp src) { 
		  // MOVE(d, e) 
		  if (dst instanceof MEM) munchMove((MEM)dst,src); 
		  else if (dst instanceof TEMP) munchMove((TEMP)dst,src);
		  else if (src instanceof TEMP) munchMove(dst,(TEMP)src);
	} 
	
	void munchMove(MEM dst, Exp src) { 
		
		// MOVE(MEM(BINOP(PLUS, e1, CONST(i))), e2) 
		if (dst.exp instanceof BINOP && ((BINOP)dst.exp).binop==BINOP.PLUS 
		    && ((BINOP)dst.exp).right instanceof CONST) 	
		{
			 emit(new OPER("STORE M['s0+" + ((CONST)((BINOP)dst.exp).right).value + "] <- 's1",
					 null,
					 L(munchExp(((BINOP)dst.exp).left), 
							 L(munchExp(src), null))));
		} 
		
		
		// MOVE(MEM(BINOP(PLUS, CONST(i), e1)), e2) 
		  else if (dst.exp instanceof BINOP && ((BINOP)dst.exp).binop==BINOP.PLUS 
		           && ((BINOP)dst.exp).left instanceof CONST) 
		  {
			 emit(new OPER("STORE M['s0+" + ((CONST)((BINOP)dst.exp).left).value + "] <- 's1",
					 null,
					 L(munchExp(((BINOP)dst.exp).right), 
							 L(munchExp(src), null))));
		  } 
		  
		
		// MOVE(MEM(e1), MEM(e2)) 
		  else if (src instanceof MEM) 
		     {
			  emit(new OPER("MOVEM M['s0] <- M['s1]",
					  null,
					  L(munchExp(dst.exp),L(munchExp(((MEM)src).exp),null))));
			 } 
		
		// MOVE(MEM(CONST(i)),e2)
		  else if (dst.exp instanceof MEM && ((MEM)dst.exp).exp instanceof CONST)
		  {
			  emit(new OPER("STORE M[r0+" + ((CONST)(((MEM)dst.exp).exp)).value + "] <- 's0",
					  null,L(munchExp(src),null)));
		  }
		
		// MOVE(MEM(e1), e2) 
		  else
		     {
				  emit(new OPER("STORE M['s0] <- 's1",
						  null,
						  L(munchExp(dst.exp), L(munchExp(src), null))));
			  } 
		} 


	void munchMove(TEMP dst, Exp src) { 
	  // MOVE(TEMP(t1), e) 
	  emit(new OPER("ADD 'd0 <- 's0 + r0", 
              L(dst.temp,null), L(munchExp(src), null)));
	} 

    private void munchLabel(treeIR.LABEL s) {
        emit(new instructionSelection.Assem.LABEL("["+s.label.toString()+"]",s.label));
    }
    

    private void munchJump(JUMP s) {
    	emit(new instructionSelection.Assem.OPER("JUMP to "+s.targets.head.print(), null, L(new Temp(),null), s.targets));      
    }
    
    private void munchCJump(CJUMP s){

        Temp r = new Temp();
        Temp t1 = new Temp();
        Temp left = munchExp(s.left);
        Temp right = munchExp(s.right);
        Label lTrue = s.iftrue;
        Label lFalse = s.iffalse;
        
        switch(s.relop){
        case CJUMP.GE:
                emit(new instructionSelection.Assem.OPER("SUB `d0 <- `s0 - `s1", L(r, null), L(left, L(right,null)), null));
                emit(new instructionSelection.Assem.OPER(true,"BRANCHGE if `s0 >= 0 goto "+lTrue+"", null, L(r, null), new LabelList(lTrue, null)));
                emit(new instructionSelection.Assem.OPER("JUMP to "+lFalse+"", null, L(t1,null), new LabelList(lFalse, null)));
                break;
        case CJUMP.LT:
                emit(new instructionSelection.Assem.OPER("SUB `d0 <- `s0 - `s1", L(r, null), L(left, L(right,null)), null));
                emit(new instructionSelection.Assem.OPER(true,"BRANCHLT if `s0 < 0 goto "+lTrue+"", null, L(r, null), new LabelList(lTrue, null)));
                emit(new instructionSelection.Assem.OPER("JUMP to "+lFalse+"", null, L(t1,null), new LabelList(lFalse, null)));
                break;
        case CJUMP.EQ:
                emit(new instructionSelection.Assem.OPER("SUB `d0 <- `s0 - `s1", L(r, null), L(left, L(right,null)), null));
                emit(new instructionSelection.Assem.OPER(true,"BRANCHEQ if `s0 == 0 goto "+lTrue+"", null, L(r, null), new LabelList(lTrue, null)));
                emit(new instructionSelection.Assem.OPER("JUMP to "+lFalse+"", null, L(t1,null), new LabelList(lFalse, null)));
                break;
        case CJUMP.NE:
                emit(new instructionSelection.Assem.OPER("SUB `d0 <- `s0 - `s1", L(r, null), L(left, L(right,null)), null));
                emit(new instructionSelection.Assem.OPER(true,"BRANCHNE if `s0 != 0 goto "+lTrue+"", null, L(r, null), new LabelList(lTrue, null)));
                emit(new instructionSelection.Assem.OPER("JUMP goto "+lFalse+"", null, L(t1,null), new LabelList(lFalse, null)));
        }
}

    
//EXPRESSION

    public Temp munchExp(treeIR.Exp e){
        if(e instanceof MEM) return munchMem((MEM)e);
        if(e instanceof BINOP)
			try {
				return munchBinop((BINOP)e);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        if(e instanceof CONST) return munchConst((CONST)e);
        if(e instanceof CALL) return munchCall((CALL)e);
        if(e instanceof NAME) return munchName((NAME)e);
        else return munchTemp((TEMP) e);
    }
    
    public Temp munchCall(treeIR.CALL e){
    	Temp r = munchExp(e.func);
    	TempList l = munchArgs(0,e.args);
    	emit(new OPER("CALL 's0",null,L(r,l)));
    	return r;
    }
    
    public TempList munchArgs(int iter, ExpList args){
    	if(args==null) return null;
    	Temp r = munchExp(args.head);
    	TempList l = new TempList(r, munchArgs(iter+1,(ExpList)args.tail));
    	if(iter==0){
    		TempList l2=null;
    		// reverte a lista
    		for(;l!=null;l =(TempList) l.tail){
    			l2 = new TempList(l.head,l2);
    		}
    		return l2;
    	}
    	return l;
    }

    private Temp munchMem(MEM e){
        // MEM(BINOP(PLUS, e1, CONST(i)))
        if(e.exp instanceof BINOP && ((BINOP)e.exp).binop == BINOP.PLUS && 
                        ((BINOP)e.exp).right instanceof CONST ){
                Temp r = new Temp();
                
                emit(new OPER("LOAD 'd0 <- M['s0 + "+((CONST)((BINOP)e.exp).right).value+ "]", 
                		L(r,null), 
                		L(munchExp(((BINOP)e.exp).left), null)));
                return r;
        }
        // MEM(BINOP(PLUS, CONST(i), e1))
        else if(e.exp instanceof BINOP && ((BINOP)e.exp).binop == BINOP.PLUS && 
                        ((BINOP)e.exp).left instanceof CONST){
                Temp r = new Temp();
                
                emit(new OPER("LOAD 'd0 <- M['s0 + "+((CONST)((BINOP)e.exp).left).value+ "]", 
                		L(r,null), 
                		L(munchExp(((BINOP)e.exp).right), null)));
                return r;
        } 
        // MEM(CONST(i))
        else if(e.exp instanceof CONST){
                Temp r = new Temp();
                
                emit(new OPER("LOAD 'd0 <- M[r0 + "+ ((CONST)e.exp).value +"]",
                		L(r,null),null));
                return r;
        }
        // MEM(e1)
        else{
                Temp r = new Temp();
                
                emit(new OPER("LOAD 'd0 <- M['s0+0]", 
                		L(r,null),L(munchExp(e.exp),null)));
                return r;
        }        
    }

    private Temp munchBinop(BINOP e) throws Exception {
        // BINOP(PLUS, e1, CONST(i))
        if(e.binop == BINOP.PLUS && e.right instanceof CONST){
                Temp r = new Temp();
                               
                emit(new OPER("ADDI 'd0 <- 's0 + " + ((CONST)e.right).value+"", 
                		L(r,null), L(munchExp(e.left),null)));
                return r;
        }
        // BINOP(PLUS, CONST(i), e1)
        else if(e.binop == BINOP.PLUS && e.left instanceof CONST){
                Temp r = new Temp();
               
                emit(new OPER("ADDI 'd0 <- 's0 + " + ((CONST)e.left).value+"", 
                		L(r,null),
                		L(munchExp(e.right),null)));
                return r;
        }
        // BINOP(PLUS, e1, e2)
        else if(e.binop == BINOP.PLUS){
                Temp r = new Temp();
               
                emit(new OPER("ADD 'd0 <- 's0 + 's1", 
                		L(r,null),L(munchExp(e.left),L(munchExp(e.right),null))));
                return r;
        }               
        // BINOP(MINUS, e1, CONST(i))
        else if(e.binop == BINOP.MINUS && e.right instanceof CONST){
                        Temp r = new Temp();
                        
                        emit(new OPER("SUBI 'd0 <- 's0 -" + ((CONST)e.right).value+"",
                        		L(r,null),
                        		L(munchExp(e.left),null)));
                        return r;
                }
        // BINOP(MINUS, e1, e2)
        else if(e.binop == BINOP.MINUS){
                Temp r = new Temp();
                
                emit(new OPER("SUB 'd0 <- 's0 - 's1",
                		L(r,null),L(munchExp(e.left),L(munchExp(e.right),null))));
                return r;
        }
        
        // BINOP(MUL, e1, e2)
        else if(e.binop == BINOP.MUL){
                Temp r = new Temp();
                
                emit(new OPER("MUL 'd0 <- 's0 * 's1",
                		L(r,null),L(munchExp(e.left),L(munchExp(e.right),null))));
                return r;
        }
        // BINOP(DIV, e1, e2)
        else{
                Temp r = new Temp();
                
                emit(new OPER("DIV 'd0 <- 's0 / 's1",
                		L(r,null),L(munchExp(e.left),L(munchExp(e.right),null))));
                return r;
        }
    }
    
    private Temp munchConst(CONST e) {
        // CONST(i)
        Temp r = new Temp();
        
        emit(new OPER("ADDI 'd0 <- r0 + "+e.value+"", null,L(r,null),null));
        return r;
    }

    private Temp munchTemp(TEMP e) {
        return e.temp;
    }
    
    private Temp munchName(NAME e) {
        return new Temp();
    }

}
