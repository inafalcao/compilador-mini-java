package activationRegister.temp;

public class DefaultMap implements TempMap {
	

	public String tempMap(Temp t) {
		if(t == null) return "";
	   return t.toString();
	}

	public DefaultMap() {}
}
