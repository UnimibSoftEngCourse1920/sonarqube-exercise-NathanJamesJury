
public class Automobile {
	private PostoAuto posto;
	
	public Automobile() {}
	
	public PostoAuto getPosto() {
		return posto;
	}

	public void setPosto(PostoAuto posto) {
		this.posto = posto;
	}
	
	/* Con questo metodo assegniamo un parcheggiatore a ciascun automobilista, in ordine "first-come-first-serve". 
	 * Deve essere synchronized perché più automobilisti possono arrivare insieme. Se dovessero arrivare insieme entreranno 
	 * uno alla volta dentro questo metodo. Nel caso in cui i parcheggiatori fossero tutti occupati il primo thread (quindi
	 * il primo automobilista) entrerà nel metodo synchronized e resterà in attesa dentro il while finché uno dei 
	 * parcheggiatori non diventerà di nuovo libero, gli altri thread invece si accoderanno fuori dal metodo in attesa
	 * che il primo termini (ovvero che gli venga assegnato un parcheggiatore).
	 */
	public synchronized Parcheggiatore attesaParcheggiatori(Parcheggiatore[] parcheggiatori) {
		boolean attesa = true;
		int i = 0;
		Parcheggiatore parcheggiatore = null;
		while(attesa) {
			i = 0;
			for (i = 0; i < parcheggiatori.length; i++) 
				if (parcheggiatori[i].isLibero()) {
					attesa = false;
					parcheggiatore = parcheggiatori[i];
					break;
				}
		}
		return parcheggiatore;
		
	}
}
