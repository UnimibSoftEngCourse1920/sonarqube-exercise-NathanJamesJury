
public class Parcheggiatore {
	private int ID;
	private boolean libero;
	
	public Parcheggiatore(int ID) {
		this.ID = ID;
		this.libero = true;
	}

	public boolean isLibero() {
		return libero;
	}

	public void setLibero(boolean libero) {
		this.libero = libero;
	}

	public int getID() {
		return ID;
	}
	
	/* Con il metodo parcheggiaAuto simuliamo la reale esecuzione del parcheggio da parte del parcheggiatore.
	 * Il tempo per parcheggiare lo abbiamo posto in funzione del numero del posto in cui deve essere parcheggiato,
	 * ovvero, più il numero del posto è alto, più sarà lontano dall'ingresso e quindi più tempo ci si metterà a parcheggiare.
	 * Abbiamo posto come tempo di default 4 secondi, per poi incrementarlo di 1 secondo per ogni posto. Ovviamente se vuole 
	 * delle simulazioni un pò più realistiche può alzare questi tempi
	 * 
	 */
	public void parcheggiaAuto(Automobile automobile) {
		int defaultTime = 4000;
		int numeroPosto = automobile.getPosto().getNumero();
		long temp = defaultTime + (long)(numeroPosto)*1000;
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		};
		//una volta finito il suo lavoro, il parcheggiatore diventa di nuovo libero
		libero = true;
	}
	
	//Stesso discorso del parcheggiaAuto
	public void restituisciAuto(Automobile automobile) {
		int defaultTime = 4000;
		int numeroPosto = automobile.getPosto().getNumero();
		long temp = defaultTime + (long)(numeroPosto)*1000;
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		};
		//una volta finito il suo lavoro, il parcheggiatore diventa di nuovo libero
		libero = true;
	}
	
}
