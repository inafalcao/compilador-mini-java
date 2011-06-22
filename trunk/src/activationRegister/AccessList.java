package activationRegister;


import util.List;

public class AccessList extends List<Access>{

	public AccessList(Access h, AccessList t) {
		super(h, t);
	}

	public AccessList() {
		super();
	}	
}
