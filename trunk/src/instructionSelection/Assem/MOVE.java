package instructionSelection.Assem;
import activationRegister.temp.*;

public class MOVE extends Instr {
   public Temp dst;   
   public Temp src;

   public MOVE(String a, Temp d, Temp s) {
      assem=a; dst=d; src=s;
   }
   @Override
public TempList use() {return new TempList(src,null);}
   @Override
public TempList def() {return new TempList(dst,null);}
   @Override
public Targets jumps()     {return null;}

}
