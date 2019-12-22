
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
	 * ovvero, pi� il numero del posto � alto, pi� sar� lontano dall'ingresso e quindi pi� tempo ci si metter� a parcheggiare.
	 * Abbiamo posto come tempo di default 4 secondi, per poi incrementarlo di 1 secondo per ogni posto. Ovviamente se vuole 
	 * delle simulazioni un p� pi� realistiche pu� alzare questi tempi
	 * 
	 */
	public void parcheggiaAuto(Automobile automobile) {
		int defaultTime = 4000;
		int numeroPosto = automobile.getPosto().getNumero();
		long temp = defaultTime + (long)(numeroPosto)*1000;
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e) {};
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
		} catch (InterruptedException e) {};
		//una volta finito il suo lavoro, il parcheggiatore diventa di nuovo libero
		libero = true;
	}
	
}
