package reg_alloc;

import java.util.HashMap;

import util.List;

import activationRegister.temp.Temp;
import activationRegister.temp.TempList;
import flow_graph.AssemFlowGraph;
import flow_graph.FlowGraph;
import graph.Graph;
import graph.Node;

public class InterferenceGraph extends Graph{
	HashMap<Node, Temp> nt;
	HashMap<Temp, Node> tn;
	TempList moves;
	public Liveness liveness;
	
	public InterferenceGraph(Liveness liveness){
		this.liveness = liveness;
		nt = new HashMap<Node, Temp>();
		tn = new HashMap<Temp, Node>();
		for(List<Node> nl = liveness.getFlowGraph().nodes(); nl != null; nl = nl.tail){
			TempList l = (TempList) liveness.g.def(nl.head);
			for(; l != null; l = (TempList) l.tail){
				if(!tn.containsKey(l.head)){
					Node n = new Node(this);
					tn.put(l.head, n);
					nt.put(n, l.head);
				}
			}
			l = (TempList) liveness.g.use(nl.head);
			for(; l != null; l = (TempList) l.tail){
				if(!tn.containsKey(l.head)){
					Node n = new Node(this);
					tn.put(l.head, n);
					nt.put(n, l.head);
				}
			}
		}
		
		for(List<Node> nl = liveness.getFlowGraph().nodes(); nl != null; nl = nl.tail){
			FlowGraph g = liveness.getFlowGraph();
			TempList def = (TempList) liveness.getFlowGraph().def(nl.head);
			TempList p = liveness.getOut().get(nl.head.mykey);
			if(!((AssemFlowGraph)g).isMove(nl.head)){
				for(; def != null; def = (TempList) def.tail){
					for(; p != null; p = (TempList) p.tail){
						if(def.head != p.head){
							List<Node> adj = tn.get(def.head).adj();
							if(adj == null)
								this.addEdge(tn.get(def.head), tn.get(p.head));
							else
								if(!adj.contains(tn.get(p.head)))this.addEdge(tn.get(def.head), tn.get(p.head));
							}
						}
				}
			}
			else{
				for(; def != null; def = (TempList) def.tail){
					for(; p != null; p = (TempList) p.tail){
						if(p.head != g.use(nl.head).head){
							List<Node> adj = tn.get(def.head).adj();
							if(adj == null)
								this.addEdge(tn.get(def.head), tn.get(p.head));
							else
								if(!adj.contains(tn.get(p.head)))this.addEdge(tn.get(def.head), tn.get(p.head));
						}
					}
				}
			}
		}	
	}
	
	public Node tnode(Temp temp){
		return tn.get(temp);	
	}
	
	public Temp gtemp(Node node){
		return nt.get(node);		
	}

	public int size(){
		return nt.keySet().size();
	}

	public void print(){
        for (List<Node> p=nodes(); p!=null; p=p.tail){
            Node n = p.head;
            if(n != null){
              	Temp t2 = null;
            	t2 = nt.get(p.head);
	            if(t2 != null)System.out.print(nt.get(n).toString());
	            System.out.print(": ");
	            List<Node> q=n.adj();
	            for(; q!=null; q=q.tail){
	            	Temp t = null;
	            	if(q.head != null)t = nt.get(q.head);
	            	if(t != null)System.out.print(t.toString());
	            	System.out.print(" ");
	            }
	            System.out.println();
	        }
        }
	}
}
