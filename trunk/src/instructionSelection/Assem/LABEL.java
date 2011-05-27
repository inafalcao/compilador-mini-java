package instructionSelection.Assem;
import activationRegister.temp.*;

public class LABEL extends Instr {
   public Label label;

   public LABEL(String a, Label l) {
      assem=a; label=l;
   }

   @Override
public TempList use() {return null;}
   @Override
public TempList def() {return null;}
   @Override
public Targets jumps()     {return null;}

}
