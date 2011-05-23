package instructionSelection.Assem;

public class OPER extends Instr {
   public activationRegister.temp.TempList dst;   
   public activationRegister.temp.TempList src;
   public Targets jump;

   public OPER(String a, activationRegister.temp.TempList d, activationRegister.temp.TempList s, activationRegister.temp.LabelList j) {
      assem=a; dst=d; src=s; jump=new Targets(j);
   }
   public OPER(String a, activationRegister.temp.TempList d, activationRegister.temp.TempList s) {
      assem=a; dst=d; src=s; jump=null;
   }

   public activationRegister.temp.TempList use() {return src;}
   public activationRegister.temp.TempList def() {return dst;}
   public Targets jumps() {return jump;}

}
