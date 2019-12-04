
public class RitiraThread extends Automobilista implements Runnable {

	public RitiraThread(int ID, Automobile automobile, Parcheggio parcheggio, Ticket ticket) {
		super(ID);
		this.automobile = automobile;
		this.parcheggio = parcheggio;
		this.ticket = ticket;
		
	}
	
	public void run() {
		parcheggio.restituisciAuto(ticket);
		System.out.println("L'automobilista " + this.getID() + " ha ritirato l'auto dal posto " + ticket.getPostoAuto().getNumero());
	}

}
