package reg_alloc;

import java.util.HashMap;

import util.List;

import flow_graph.AssemFlowGraph;
import flow_graph.FlowGraph;
import graph.Node;
import activationRegister.temp.Temp;
import activationRegister.temp.TempList;
public class Liveness {
	private HashMap<Integer, TempList> in;
	private HashMap<Integer, TempList> out;
	private HashMap<Integer, TempList> inAnt ;
	private HashMap<Integer, TempList> outAnt ;
	public FlowGraph g;
	
	public Liveness(FlowGraph g) {
		in = new HashMap<Integer, TempList>();
		out = new HashMap<Integer, TempList>();
		for(List<Node> nl = g.nodes(); nl != null ; nl = nl.tail){
			in.put(nl.head.mykey, null);
			out.put(nl.head.mykey, null);
		}
		this.g = g;
		liveness();
	}

	private void liveness() {
		int i = 0;
		inAnt = new HashMap<Integer, TempList>();
		outAnt = new HashMap<Integer, TempList>();
		// recalcula In and Out de cada nó enquanto diferir do calculo anterior!
		do{
			copyIn();
			copyOut();
			for(List<Node> nl = g.nodes(); nl != null ; nl = nl.tail){
				updateIn(nl.head);
				updateOut(nl.head);
				
			}
			i++;
		}while(!equalsAnt(inAnt, outAnt));
	}

	private void updateOut(Node v) {
		TempList list = null;
		List<Node> suc = v.succ();
		if(suc != null){
			for(suc = v.succ(); suc != null ; suc = suc.tail){
				TempList tl = in.get(suc.head.mykey);
				for( ; tl != null; tl = (TempList) tl.tail){
					if(list == null) list = new TempList(tl.head, null);
					if(!list.contains(tl.head))list = new TempList(tl.head, list);
				}
			}
		}
		out.remove(v.mykey);
		out.put(v.mykey, list);
	}

	private void updateIn(Node v) {
		TempList list = null;
		TempList aux = (TempList) g.use(v);
		TempList aux2 = out.get(v.mykey);
		for(; aux != null; aux = (TempList)aux.tail){
			if(list == null) list = new TempList(aux.head, null);
			if(!list.contains(aux.head))list = new TempList(aux.head, list);
		}
		for(; aux2 != null; aux2 = (TempList) aux2.tail){
			Temp t = aux2.head;
			if(g.def(v) == null ){
				if(list == null)list = new TempList(t, list);
				else if(!list.contains(t)) list = new TempList(t, list);
			}
			else{
				if(!g.def(v).contains(t)){
					if(list == null)list = new TempList(t, list);
					else if(!list.contains(t)) list = new TempList(t, list);
				}
			}
		}
		in.remove(v.mykey);
		in.put(v.mykey, list);
	}

	private void copyIn(){
		inAnt = new HashMap<Integer, TempList>();
		for(int i : in.keySet()){
			TempList tl = in.get(i);
			inAnt.put(i, tl);
		}	
	}

	private void copyOut() {
		outAnt = new HashMap<Integer, TempList>();
		for(int i : out.keySet()){
			TempList tl = out.get(i);
			outAnt.put(i, tl);
		}		
	}

	private boolean equalsAnt(HashMap<Integer, TempList> inPrime, HashMap<Integer, TempList> outPrime) {
		for(List<Node> n = g.nodes(); n != null ; n = n.tail){
			if(out.get(n.head.mykey)==  null){
				if(outPrime.get(n.head.mykey) != null) return false;
			}else{
				if(!out.get(n.head.mykey).equal(outPrime.get(n.head.mykey)))return false;
			}
		}
		for(List<Node> n = g.nodes(); n != null ; n = n.tail){
			if(in.get(n.head.mykey)==  null){
				if(inPrime.get(n.head.mykey) != null) return false;
			}else{
				if(!in.get(n.head.mykey).equal(inPrime.get(n.head.mykey)))return false;
			}
		}
		return true;
	}

	public TempList inList(Node node){
		return in.get(node);
	}

	public TempList outList(Node node){
		return out.get(node);
	}
	
	public HashMap<Integer, TempList> getOut(){
		return out;
	}
	
	public HashMap<Integer, TempList> getIn(){
		return in;
	}

	public FlowGraph getFlowGraph(){
		return g;
	}

	public void print() {
		for(List<Node> nodes = g.nodes(); nodes != null ; nodes = nodes.tail){
			System.out.println();
			System.out.println("No Instr.:  "+ ((AssemFlowGraph)g).instr(nodes.head).assem);
			System.out.print("IN:");
			for(TempList in2 = in.get(nodes.head.mykey); in2 != null; in2 = (TempList) in2.tail){
				if(in2.head != null)System.out.print(in2.head.toString()+ " ");
			}
			System.out.println();
			System.out.print("OUT:");		
			for(TempList in2 = out.get(nodes.head.mykey); in2 != null; in2 = (TempList) in2.tail){
				if(in2.head != null)System.out.print(in2.head.toString()+ " ");
			}
			
			System.out.println();
		}
	}
}
