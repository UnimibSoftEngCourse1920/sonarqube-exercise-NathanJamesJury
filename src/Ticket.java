import java.util.Date;

public class Ticket {
	private long codice;
	private Date data = null;
	//ogni ticket una volta creato dal parcheggio si riferisce ad uno specifico postoAuto
	private PostoAuto postoAuto = null;

	public Ticket(long codice, Date data, PostoAuto postoAuto) {
		this.codice = codice;
		this.data = data;
		this.postoAuto = postoAuto;
	}

	public long getCodice() {
		return codice;
	}

	public PostoAuto getPostoAuto() {
		return postoAuto;
	}

	public Date getData() {
		return data;
	}

}
