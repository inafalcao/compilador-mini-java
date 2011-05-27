package activationRegister.temp;

public class DefaultMap implements TempMap {
	

	@Override
	public String tempMap(Temp t) {
		if(t == null) return "";
	   return t.toString();
	}

	public DefaultMap() {}
}
