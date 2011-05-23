package instructionSelection.Assem;

public class MOVE extends Instr {
   public activationRegister.temp.Temp dst;   
   public activationRegister.temp.Temp src;

   public MOVE(String a, activationRegister.temp.Temp d, activationRegister.temp.Temp s) {
      assem=a; dst=d; src=s;
   }
   public activationRegister.temp.TempList use() {return new activationRegister.temp.TempList(src,null);}
   public activationRegister.temp.TempList def() {return new activationRegister.temp.TempList(dst,null);}
   public Targets jumps()     {return null;}

}
