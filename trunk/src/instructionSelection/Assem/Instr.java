package instructionSelection.Assem;

import activationRegister.temp.LabelList;
import activationRegister.temp.TempList;

public abstract class Instr {
  public String assem;
  public abstract activationRegister.temp.TempList use();
  public abstract activationRegister.temp.TempList def();
  public abstract Targets jumps();

  private activationRegister.temp.Temp nthTemp(activationRegister.temp.TempList l, int i) {
    if (i==0) return l.head;
    else return nthTemp((TempList) l.tail,i-1);
  }

  private activationRegister.temp.Label nthLabel(activationRegister.temp.LabelList l, int i) {
    if (i==0) return l.head;
    else return nthLabel((LabelList) l.tail,i-1);
  }

  public String format(activationRegister.temp.TempMap m) {
	activationRegister.temp.TempList dst = def();
	activationRegister.temp.TempList src = use();
    Targets j = jumps();
    activationRegister.temp.LabelList jump = (j==null)?null:j.labels;
    StringBuffer s = new StringBuffer();
    int len = assem.length();
    for(int i=0; i<len; i++)
	if (assem.charAt(i)=='`')
	   switch(assem.charAt(++i)) {
              case 's': {int n = Character.digit(assem.charAt(++i),10);
			 s.append(m.tempMap(nthTemp(src,n)));
			}
			break;
	      case 'd': {int n = Character.digit(assem.charAt(++i),10);
			 s.append(m.tempMap(nthTemp(dst,n)));
			}
 			break;
	      case 'j': {int n = Character.digit(assem.charAt(++i),10);
			 s.append(nthLabel(jump,n).toString());
			}
 			break;
	      case '`': s.append('`'); 
			break;
              default: throw new Error("bad Assem format");
       }
       else s.append(assem.charAt(i));

    return s.toString();
  }


}
