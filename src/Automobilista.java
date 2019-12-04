
public class Automobilista {
	private int ID;
	protected Ticket ticket;
	protected Automobile automobile;
	protected Parcheggio parcheggio;
	protected ParcheggioThread parcheggioThread;
	protected RitiraThread ritiraThread;
	
	public Automobilista(int ID) {
		this.ID = ID;
		this.automobile = new Automobile();
	}
	
	public Parcheggio getParcheggio() {
		return parcheggio;
	}

	public void setParcheggio(Parcheggio parcheggio) {
		this.parcheggio = parcheggio;
	}
		
	public int getID() {
		return ID;
	}
    
	/* Chiamiamo il metodo parcheggiaAuto per parcheggiare un'auto. Nel fare ciò lasciamo la gestione del parcheggio al thread
	 * parcheggioThread per poter poi andare avanti con l'esecuzione e parcheggiare altre auto.
	 */
	public void parcheggiaAuto(Parcheggio parcheggio) {
			parcheggioThread = new ParcheggioThread(ID, automobile, parcheggio, this);
			if (this.getTicket() == null)
				System.out.println("parcheggiando l'auto dell'automobilista: " + this.getID());
			else {
				System.out.println("ticket gia presente, non puoi parcheggiare di nuovo automobilista " + this.getID());
				return;
			}
			new Thread(parcheggioThread).start();
			return;
	}
	
	/* Chiamiamo il metodo ritiraAuto invece per ritirare un'auto (e quindi riconsegnarla all'automobilista). 
	 * Nel fare ciò lasciamo la gestione del ritiro al thread ritiraThread per poter poi andare avanti 
	 * con l'esecuzione e ritirare altre auto.
	 */
	public void ritiraAuto() {
		ritiraThread = new RitiraThread(ID, automobile, parcheggio, ticket);
		if (this.getTicket() != null)
			System.out.println("restituendo l'auto all'automobilista " + this.getID() + " dal posto " + ticket.getPostoAuto().getNumero() + " dal parcheggio " + parcheggio.getNome());
		else {
			System.out.println("ticket assente, impossibile restituire l'auto all'automobilista " + this.getID());
			return;
		}
		Thread t = new Thread(ritiraThread);
		t.start();
		return;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public Automobile getAutomobile() {
		return automobile;
	}
	
	
}
