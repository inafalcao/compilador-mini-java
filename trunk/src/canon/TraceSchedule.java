package canon;
import translate.Frag;
import translate.FragList;
import treeIR.StmList;

public class TraceSchedule {
	public static FragList canonlize(FragList frags ) {
		for(int i = 0 ; i < frags.size(); i++){
			Frag f = frags.get(i);
			f.setBody( Canon.linearize(f.getBody().get(0)));
		}
		return frags;
	}
	
	public static StmListList generateCode(FragList frags){
		frags = canonlize(frags);
		StmListList stml = null;
		StmListList aux;
		for(int i = 0 ; i < frags.size(); i++){
			Frag f = frags.get(i);
			aux = new BasicBlocks(f.getBody()).blocks;
			int j = 0;
			System.out.println("~~~~~~~~~~~~~ INICIO FRAG "+i+" ~~~~~~~~~~~~~~~");
			
			for(; aux!= null; aux = (StmListList) aux.tail){
				System.out.println("* Bloco "+j+":");
				j++;
				aux.head.print();
				stml = new StmListList(aux.head, stml);
				if(aux.tail!=null) System.out.println("----------------------------");
			}
			
			System.out.println("~~~~~~~~~~~~~~~ FIM FRAG "+i+" ~~~~~~~~~~~~~~~~");
		}
		
		return stml;
	}
	
	public static StmList generateCode(StmListList list){
		StmList stml = null;
		StmListList aux;
		for(aux = list; aux!= null; aux = (StmListList) aux.tail){
			StmList aux2 = aux.head;
			for(; aux2!= null; aux2 = (StmList) aux2.tail){
				stml = new StmList(aux2.head, stml);
			}
		}
		return stml;
	}
}
