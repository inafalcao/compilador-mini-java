package canon;

import util.List;
import treeIR.*;
import activationRegister.temp.*;

public class BasicBlocks {
  public StmListList blocks;
  public Label done;

  public StmListList lastBlock;
  private StmList lastStm;

  private void addStm(Stm s) {
	lastStm = (StmList) (lastStm.tail = new StmList(s,null));
	//coloquei um cast aqui pra StmList, nao sei se ta certo
  }

  private void doStms(StmList l) {
      if (l==null) 
    	  doStms(new StmList(new JUMP(done), null));
      else if (l.head instanceof JUMP 
	      || l.head instanceof CJUMP) {
	addStm(l.head);
	mkBlocks((StmList) l.tail);
	//coloquei um cast aqui pra StmList, nao sei se ta certo
      } 
      else if (l.head instanceof LABEL)
           doStms(new StmList(new JUMP(((LABEL)l.head).label), 
	  			   l));
      else {
    	  addStm(l.head);
    	  doStms((StmList) l.tail);
      }
  }

  void mkBlocks(StmList l) {
     if (l==null) return;
     else if (l.head instanceof LABEL) {
    	 lastStm = new StmList(l.head,null);
    	 if (lastBlock==null)
    		 lastBlock= blocks= new StmListList(lastStm,null);
    	 else
    		 lastBlock = lastBlock.tail = new StmListList(lastStm,null);
    	 doStms((StmList) l.tail);
     }
     else mkBlocks(new StmList(new LABEL(new Label()), l));
  }
   

  public BasicBlocks(StmList stms) {
    done = new Label();
    mkBlocks(stms);
  }
  
  public BasicBlocks(List<Stm> stms) {
	    done = new Label();
	    mkBlocks(toStmList(stms));
	  }
  
  public static treeIR.StmList toStmList(List<treeIR.Stm> el){
  	if(el==null) return null;
  	return new treeIR.StmList(el.head,toStmList(el.tail));
  }
}
