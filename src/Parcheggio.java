
import java.util.Date;

public class Parcheggio {
	private String nome;
	private PostoAuto postiAuto[];
	private Parcheggiatore parcheggiatori[];
	
	public Parcheggio(String nome, int numeroPostiAuto, Parcheggiatore[] parcheggiatori) {
		postiAuto = new PostoAuto[numeroPostiAuto];
		for (int i = 0; i < numeroPostiAuto; i++) {
			PostoAuto posto = new PostoAuto(i + 1);
			postiAuto[i] = posto;
		}
		this.parcheggiatori = parcheggiatori;
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

	public Parcheggiatore[] getParcheggiatori() {
		return parcheggiatori;
	}

	public void setParcheggiatori(Parcheggiatore[] parcheggiatori) {
		this.parcheggiatori = parcheggiatori;
	}

	public PostoAuto[] getPostiAuto() {
		return postiAuto;
	}
	
	// Questo metodo scorre la lista dei postiAuto controllando se qualcuno è libero 
	private synchronized PostoAuto checkPostiLiberi() {
		for (int i = 0; i < postiAuto.length; i++) {
			if (postiAuto[i].isLibero()) {
				postiAuto[i].setLibero(false);
				return postiAuto[i];
			}	
		}
		return null;
	}
	
	/* Questo metodo esegue il ritiro dell'auto dall'automobilista (e il conseguente parcheggio) ritornando all'automobilista
	 * un ticket, per fare ciò consegna l'auto ad un parcheggiatore tra quelli liberi
	 */
	public Ticket ritiraAuto(Automobile automobile)  throws ParcheggioPienoException {
		PostoAuto posto = checkPostiLiberi();
		if (posto == null) throw new ParcheggioPienoException();
		posto.setAutomobile(automobile);
		automobile.setPosto(posto);
		//assegno un parcheggiatore all'automobilista
		Parcheggiatore parcheggiatore = automobile.attesaParcheggiatori(parcheggiatori);
		parcheggiatore.setLibero(false);
		System.out.println("Il parcheggiatore " + parcheggiatore.getID() + " sta parcheggiando l'auto al posto " + posto.getNumero());
		parcheggiatore.parcheggiaAuto(automobile);
		Date data = new Date();
		long codice = data.getTime() + posto.getNumero();
		Ticket ticket = new Ticket(codice, data, posto);
		return ticket;
	}
	
	/* Questo metodo esegue la restituzione dell'auto all'automobilista, liberando conseguentemente il posto
	 * che aveva occupato
	 */
	public void restituisciAuto(Ticket ticket) {
		PostoAuto posto = ticket.getPostoAuto();
		Automobile automobile = posto.getAutomobile();
		//assegno un parcheggiatore all'automobilista
		Parcheggiatore parcheggiatore = automobile.attesaParcheggiatori(parcheggiatori);
		parcheggiatore.setLibero(false);
		System.out.println("il parcheggiatore " + parcheggiatore.getID() + " sta ritirando l'auto dal posto " + posto.getNumero());
		parcheggiatore.restituisciAuto(automobile);
		postiAuto[posto.getNumero()-1].setLibero(true);
		automobile.setPosto(null);
	}
}
