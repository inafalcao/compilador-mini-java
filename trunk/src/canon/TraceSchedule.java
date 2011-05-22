package canon;

import treeIR.StmList;

public class TraceSchedule {

  public treeIR.StmList stms;
  BasicBlocks theBlocks;
  java.util.Dictionary table = new java.util.Hashtable();

  treeIR.StmList getLast(treeIR.StmList block) {
     treeIR.StmList l=block;
     while (l.tail.tail!=null)  l=(StmList) l.tail;
     return l;
  }

  void trace(treeIR.StmList l) {
   for(;;) {
     treeIR.LABEL lab = (treeIR.LABEL)l.head;
     table.remove(lab.label);
     treeIR.StmList last = getLast(l);
     treeIR.Stm s = last.tail.head;
     if (s instanceof treeIR.JUMP) {
	treeIR.JUMP j = (treeIR.JUMP)s;
        treeIR.StmList target = (treeIR.StmList)table.get(j.targets.head);
	if (j.targets.tail==null && target!=null) {
               last.tail=target;
	       l=target;
        }
	else {
	  last.tail.tail=getNext();
	  return;
        }
     }
     else if (s instanceof treeIR.CJUMP) {
	treeIR.CJUMP j = (treeIR.CJUMP)s;
        treeIR.StmList t = (treeIR.StmList)table.get(j.iftrue);
        treeIR.StmList f = (treeIR.StmList)table.get(j.iffalse);
        if (f!=null) {
	  last.tail.tail=f; 
	  l=f;
	}
        else if (t!=null) {
	  last.tail.head=new treeIR.CJUMP(treeIR.CJUMP.notRel(j.relop),
					j.left,j.right,
					j.iffalse,j.iftrue);
	  last.tail.tail=t;
	  l=t;
        }
        else {
      activationRegister.temp.Label ff = new activationRegister.temp.Label();
	  last.tail.head=new treeIR.CJUMP(j.relop,j.left,j.right,
					j.iftrue,ff);
	  last.tail.tail=new treeIR.StmList(new treeIR.LABEL(ff),
		           new treeIR.StmList(new treeIR.JUMP(j.iffalse),
					    getNext()));
	  return;
        }
     }
     else throw new Error("Bad basic block in TraceSchedule");
    }
  }

  treeIR.StmList getNext() {
      if (theBlocks.blocks==null) 
	return new treeIR.StmList(new treeIR.LABEL(theBlocks.done), null);
      else {
	 treeIR.StmList s = theBlocks.blocks.head;
	 treeIR.LABEL lab = (treeIR.LABEL)s.head;
	 if (table.get(lab.label) != null) {
          trace(s);
	  return s;
         }
         else {
	   theBlocks.blocks = theBlocks.blocks.tail;
           return getNext();
         }
      }
  }

  public TraceSchedule(BasicBlocks b) {
    theBlocks=b;
    for(StmListList l = b.blocks; l!=null; l=l.tail)
       table.put(((treeIR.LABEL)l.head.head).label, l.head);
    stms=getNext();
    table=null;
  }        
}


