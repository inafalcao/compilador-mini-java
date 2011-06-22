package instructionSelection.Assem;
import util.List;
import activationRegister.temp.*;

public class OPER extends Instr {
   public TempList dst;   
   public TempList src;
   public Targets jump;

   public OPER(String a, TempList d, TempList s, activationRegister.temp.LabelList j) {
      assem=a; dst=d; src=s; jump=new Targets(j); jumps = j;
      branch = false;
   }
   
   public OPER(String a, List<Temp> d, List<Temp> s, activationRegister.temp.LabelList j) {
	      assem=a; dst=toTempList(d); src=toTempList(s); jump=new Targets(j); jumps = j;
	      branch = false;
	   }
   
   public OPER(String a, TempList d, TempList s) {
      assem=a; dst=d; src=s; jump=null;
      branch = false;
   }
   
   public OPER(boolean m,String a, TempList d, TempList s, activationRegister.temp.LabelList j) {
	      assem=a; dst=d; src=s; jump=new Targets(j); jumps = j;
	      branch = m;
	   }
	   
	   public OPER(boolean m,String a, List<Temp> d, List<Temp> s, activationRegister.temp.LabelList j) {
		      assem=a; dst=toTempList(d); src=toTempList(s); jump=new Targets(j);jumps = j;
		      branch = m;
		   }
	   
	   public OPER(boolean m,String a, TempList d, TempList s) {
	      assem=a; dst=d; src=s; jump=null;
	      branch = m;
	   }
   
   public activationRegister.temp.TempList toTempList(List<activationRegister.temp.Temp> el){
   	if(el==null) return null;
   	return new activationRegister.temp.TempList(el.head,toTempList(el.tail));
   }

   @Override
public TempList use() {return src;}
   @Override
public TempList def() {return dst;}
   @Override
public Targets jumps() {return jump;}

}
