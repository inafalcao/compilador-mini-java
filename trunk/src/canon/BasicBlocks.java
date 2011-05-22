package canon;

import treeIR.StmList;

public class BasicBlocks {
  public StmListList blocks;
  public activationRegister.temp.Label done;

  private StmListList lastBlock;
  private treeIR.StmList lastStm;

  private void addStm(treeIR.Stm s) {
	lastStm = (StmList) (lastStm.tail = new treeIR.StmList(s,null));
	//coloquei um cast aqui pra StmList, nao sei se ta certo
  }

  private void doStms(treeIR.StmList l) {
      if (l==null) 
	doStms(new treeIR.StmList(new treeIR.JUMP(done), null));
      else if (l.head instanceof treeIR.JUMP 
	      || l.head instanceof treeIR.CJUMP) {
	addStm(l.head);
	mkBlocks((StmList) l.tail);
	//coloquei um cast aqui pra StmList, nao sei se ta certo
      } 
      else if (l.head instanceof treeIR.LABEL)
           doStms(new treeIR.StmList(new treeIR.JUMP(((treeIR.LABEL)l.head).label), 
	  			   l));
      else {
	addStm(l.head);
	doStms((StmList) l.tail);
      }
  }

  void mkBlocks(treeIR.StmList l) {
     if (l==null) return;
     else if (l.head instanceof treeIR.LABEL) {
	lastStm = new treeIR.StmList(l.head,null);
        if (lastBlock==null)
  	   lastBlock= blocks= new StmListList(lastStm,null);
        else
  	   lastBlock = lastBlock.tail = new StmListList(lastStm,null);
	doStms((StmList) l.tail);
     }
     else mkBlocks(new treeIR.StmList(new treeIR.LABEL(new activationRegister.temp.Label()), l));
  }
   

  public BasicBlocks(treeIR.StmList stms) {
    done = new activationRegister.temp.Label();
    mkBlocks(stms);
  }
}
