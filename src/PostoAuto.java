
public class PostoAuto {
	private int numero;
	private boolean libero;
	private Automobile automobile;
	
	public PostoAuto(int numero) {
		this.numero = numero;
		this.libero = true;
	}
	
	public Automobile getAutomobile() {
		return automobile;
	}
	public void setAutomobile(Automobile automobile) {
		this.automobile = automobile;
	}
	
	public int getNumero() {
		return numero;
	}
	
	public boolean isLibero() {
		return libero;
	}
	
	public void setLibero(boolean libero) {
		this.libero = libero;
	}
	
}
