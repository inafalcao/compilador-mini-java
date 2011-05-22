package canon;

import treeIR.ExpList;

class MoveCall extends treeIR.Stm {
  treeIR.TEMP dst;
  treeIR.CALL src;
  MoveCall(treeIR.TEMP d, treeIR.CALL s) {dst=d; src=s;}
  public treeIR.ExpList kids() {return src.kids();}
  public treeIR.Stm build(treeIR.ExpList kids) {
	return new treeIR.MOVE(dst, src.build(kids));
  }
  public String print(){
	  return "fazer";
  }   
  
class ExpCall extends treeIR.Stm {
  treeIR.CALL call;
  ExpCall(treeIR.CALL c) {call=c;}
  public treeIR.ExpList kids() {return call.kids();}
  public treeIR.Stm build(treeIR.ExpList kids) {
	return new treeIR.EXPSTM(call.build(kids));
  }
  public String print(){
	  return "fazer";
  }
}   
  
class StmExpList {
  treeIR.Stm stm;
  treeIR.ExpList exps;
  StmExpList(treeIR.Stm s, treeIR.ExpList e) {stm=s; exps=e;}
}

public class Canon {
  
 static boolean isNop(treeIR.Stm a) {
   return a instanceof treeIR.EXPSTM
          && ((treeIR.EXPSTM)a).exp instanceof treeIR.CONST;
 }

 static treeIR.Stm seq(treeIR.Stm a, treeIR.Stm b) {
    if (isNop(a)) return b;
    else if (isNop(b)) return a;
    else return new treeIR.SEQ(a,b);
 }

 static boolean commute(treeIR.Stm a, treeIR.Exp b) {
    return isNop(a)
        || b instanceof treeIR.NAME
        || b instanceof treeIR.CONST;
 }

 static treeIR.Stm do_stm(treeIR.SEQ s) { 
	return seq(do_stm(s.left), do_stm(s.right));
 }

 static treeIR.Stm do_stm(treeIR.MOVE s) { 
	if (s.dst instanceof treeIR.TEMP 
	     && s.src instanceof treeIR.CALL) 
		return reorder_stm(new MoveCall((treeIR.TEMP)s.dst,
						(treeIR.CALL)s.src));
	else if (s.dst instanceof treeIR.ESEQ)
	    return do_stm(new treeIR.SEQ(((treeIR.ESEQ)s.dst).stm,
					new treeIR.MOVE(((treeIR.ESEQ)s.dst).exp,
						  s.src)));
	else return reorder_stm(s);
 }

 static treeIR.Stm do_stm(treeIR.EXP s) { 
	if (s.exp instanceof treeIR.CALL)
	       return reorder_stm(new ExpCall((treeIR.CALL)s.exp));
	else return reorder_stm(s);
 }

 static treeIR.Stm do_stm(treeIR.Stm s) {
     if (s instanceof treeIR.SEQ) return do_stm((treeIR.SEQ)s);
     else if (s instanceof treeIR.MOVE) return do_stm((treeIR.MOVE)s);
     else if (s instanceof treeIR.EXPSTM) return do_stm((treeIR.EXP)s);
     else return reorder_stm(s);
 }

 static treeIR.Stm reorder_stm(treeIR.Stm s) {
     StmExpList x = reorder(s.kids());
     return seq(x.stm, s.build(x.exps));
 }

 static treeIR.ESEQ do_exp(treeIR.ESEQ e) {
      treeIR.Stm stms = do_stm(e.stm);
      treeIR.ESEQ b = do_exp(e.exp);
      return new treeIR.ESEQ(seq(stms,b.stm), b.exp);
  }

 static treeIR.ESEQ do_exp (treeIR.Exp e) {
       if (e instanceof treeIR.ESEQ) return do_exp((treeIR.ESEQ)e);
       else return reorder_exp(e);
 }
         
 static treeIR.ESEQ reorder_exp (treeIR.Exp e) {
     StmExpList x = reorder(e.kids());
     return new treeIR.ESEQ(x.stm, e.build(x.exps));
 }

 static StmExpList nopNull = new StmExpList(new treeIR.EXPSTM(new treeIR.CONST(0)),null);

 static StmExpList reorder(treeIR.ExpList exps) {
     if (exps==null) return nopNull;
     else {
       treeIR.Exp a = exps.head;
       if (a instanceof treeIR.CALL) {
    	   activationRegister.temp.Temp t = new activationRegister.temp.Temp();
	 treeIR.Exp e = new treeIR.ESEQ(new treeIR.MOVE(new treeIR.TEMP(t), a),
				    new treeIR.TEMP(t));
         return reorder(new treeIR.ExpList(e, (ExpList) exps.tail));
         //adicionei um cast aqui tb, nao sei se ta certo
       } else {
	 treeIR.ESEQ aa = do_exp(a);
	 StmExpList bb = reorder((ExpList) exps.tail);
	 if (commute(bb.stm, aa.exp))
	      return new StmExpList(seq(aa.stm,bb.stm), 
				    new treeIR.ExpList(aa.exp,bb.exps));
	 else {
		 activationRegister.temp.Temp t = new activationRegister.temp.Temp();
	   return new StmExpList(
			  seq(aa.stm, 
			    seq(new treeIR.MOVE(new treeIR.TEMP(t),aa.exp),
				 bb.stm)),
			  new treeIR.ExpList(new treeIR.TEMP(t), bb.exps));
	 }
       }
     }
 }
        
 static treeIR.StmList linear(treeIR.SEQ s, treeIR.StmList l) {
      return linear(s.left,linear(s.right,l));
 }
 static treeIR.StmList linear(treeIR.Stm s, treeIR.StmList l) {
    if (s instanceof treeIR.SEQ) return linear((treeIR.SEQ)s, l);
    else return new treeIR.StmList(s,l);
 }

 static public treeIR.StmList linearize(treeIR.Stm s) {
    return linear(do_stm(s), null);
 }
}
}
