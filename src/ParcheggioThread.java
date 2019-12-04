
public class ParcheggioThread extends Automobilista implements Runnable {
	private Automobilista automobilista;
	
	public ParcheggioThread(int ID, Automobile automobile, Parcheggio parcheggio, Automobilista automobilista) {
		super(ID);
		this.automobile = automobile;
		this.parcheggio = parcheggio;
		this.automobilista = automobilista;
	}
	
	public void run() {
		try {
			ticket = parcheggio.ritiraAuto(automobile);
			//il metodo "ritiraAuto" ritorna un ticket, dunque qui assegno il ticket all'automobilista 
			automobilista.setTicket(ticket);
			System.out.println("Parcheggio riuscito all'automobilista " + this.getID() + " al posto " + ticket.getPostoAuto().getNumero() );
		} catch (ParcheggioPienoException e) {
			System.out.println("Parcheggio non riuscito all'automobilista " + this.getID() + " perche pieno");  
		}
	}

}
