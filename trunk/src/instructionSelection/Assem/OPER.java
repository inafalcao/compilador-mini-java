package instructionSelection.Assem;
import activationRegister.temp.*;

public class OPER extends Instr {
   public TempList dst;   
   public TempList src;
   public Targets jump;

   public OPER(String a, TempList d, TempList s, activationRegister.temp.LabelList j) {
      assem=a; dst=d; src=s; jump=new Targets(j);
   }
   public OPER(String a, TempList d, TempList s) {
      assem=a; dst=d; src=s; jump=null;
   }

   @Override
public TempList use() {return src;}
   @Override
public TempList def() {return dst;}
   @Override
public Targets jumps() {return jump;}

}
