class Soma{
    public static void main(String[] a){
    	System.out.println(new OP().Somar(1,8));
    }
}

class OP{
	public int Somar(int a, int b){
		int c;
		c = a + b;
		a = a + c;
		return c;
	}
}
$