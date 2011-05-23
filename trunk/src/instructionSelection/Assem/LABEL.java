package instructionSelection.Assem;

public class LABEL extends Instr {
   public activationRegister.temp.Label label;

   public LABEL(String a, activationRegister.temp.Label l) {
      assem=a; label=l;
   }

   public activationRegister.temp.TempList use() {return null;}
   public activationRegister.temp.TempList def() {return null;}
   public Targets jumps()     {return null;}

}
