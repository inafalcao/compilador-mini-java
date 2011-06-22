package instructionSelection.Assem;

import activationRegister.temp.LabelList;
import activationRegister.temp.TempList;
import activationRegister.temp.Temp;
import activationRegister.temp.Label;
import activationRegister.temp.TempMap;

public abstract class Instr {
  public String assem;
  public TempList use;
  public TempList def;
  public LabelList jumps;
  public boolean branch;

  public abstract TempList use();
  public abstract TempList def();
  public abstract Targets jumps();

  public void replaceUse(Temp olduse, Temp newuse) {
		if (use != null)
		    for (int i = 0; i< use.size(); i++)
		    	if (use.get(i) == olduse) use.set(i, newuse);
  }
  public void replaceDef(Temp olddef, Temp newdef) {
	    	if (def != null)
	    		for (int i = 0; i< def.size(); i++)
	    			if (def.get(i) == olddef) def.set(i,newdef);
  };

  private Temp nthTemp(TempList l, int i) {
    if (i==0) return l.head;
    else return nthTemp((TempList) l.tail,i-1);
  }

  private Label nthLabel(LabelList l, int i) {
    if (i==0) return l.head;
    else return nthLabel((LabelList) l.tail,i-1);
  }

  public String format(TempMap m) {
	TempList dst = def();
	TempList src = use();
    Targets j = jumps();
    LabelList jump = (j==null)?null:j.labels;
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
