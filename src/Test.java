import java.util.ArrayList;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		Scanner tastiera = new Scanner(System.in);
		int scelta = 0;
		//può scegliere se fare una simulazione prestabilita del funzionamento del parcheggio oppure se gestirla lei
		System.out.println("menu:");
		System.out.println("[1] simulazione automatica di 5 automobilisti in un parcheggio da 10 posti");
		System.out.println("[2] simulazione manuale");
		scelta = tastiera.nextInt();
		if (scelta == 1)
			simulazioneAutomatica();
		else if (scelta == 2) {
			simulazioneManuale();
		}
		tastiera.close();
	}
	
	public static void simulazioneAutomatica() {
		//creo i parcheggiatori
		Parcheggiatore[] parcheggiatori = new Parcheggiatore[3];
		for (int i =0; i <3 ; i++) 
			parcheggiatori[i] = new Parcheggiatore(i+1);
		//creo il parcheggio assegnandogli i parcheggiatori
		Parcheggio parcheggio = new Parcheggio("Car Park", 10, parcheggiatori);
		//creo gli automobilisti
		Automobilista a1 = new Automobilista(1);
		Automobilista a2 = new Automobilista(2);
		Automobilista a3 = new Automobilista(3);
		Automobilista a4 = new Automobilista(4);
		Automobilista a5 = new Automobilista(5);
		Automobilista[] automobilisti = new Automobilista[5];
		automobilisti[0] = a1;
		automobilisti[1] = a2;
		automobilisti[2] = a3;
		automobilisti[3] = a4;
		automobilisti[4] = a5;
		for (int i = 0; i < 5; i++) 
			automobilisti[i].setParcheggio(parcheggio);
		/*faccio una simulazione piuttosto casuale dell'arrivo e della partenza di questi 5 automobilisti.
		 * I millisecondi nei Thread.sleep li ho messi piccoli ma per rendere la simulazione più realistica li può aumentare.  
		 */
		for (int i = 0; i < 3; i++) {
			automobilisti[i].parcheggiaAuto(automobilisti[i].getParcheggio());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println("error");
			}
		}
		try {
			Thread.sleep(6500);
		} catch (InterruptedException e) {
			System.out.println("error");
		}
		automobilisti[0].ritiraAuto();
		automobilisti[3].parcheggiaAuto(automobilisti[3].getParcheggio());
		automobilisti[1].ritiraAuto();
		
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			System.out.println("error");
		}
		automobilisti[4].parcheggiaAuto(automobilisti[4].getParcheggio());	
	}
	
	//questo metodo è praticamente identico al codice dentro la classe ClientParcheggio, ovviamente senza la comunicazione con il server
	public static void simulazioneManuale() {
		Scanner tastiera = new Scanner(System.in);
		ArrayList<Parcheggiatore> parcheggiatori = new ArrayList<>();
		Parcheggio parcheggio;
		boolean isLibero = true;
		int ID = 0;
		
		System.out.println("inserire il nome del parcheggio");
		String nome = tastiera.nextLine();
		System.out.println("inserire numero totale posti auto: ");
		int numeroPosti = tastiera.nextInt();
		System.out.println("inserire ID dei parcheggiatori, inserire -1 per uscire");
		ID = tastiera.nextInt();
		
		while (ID != -1) {
			parcheggiatori.add(new Parcheggiatore(ID));			
			ID = tastiera.nextInt();
		}
		Parcheggiatore[] parcheggiatoriArray = ClientParcheggio.listToArray(parcheggiatori);
		parcheggio = new Parcheggio(nome, numeroPosti, parcheggiatoriArray);
		int scelta, i = 1;
		boolean valido, pieno = false;
		Automobilista a1;
		Automobilista[] automobilisti = new Automobilista[parcheggio.getPostiAuto().length];
		int numeroAutomobilisti = 0, numeroPosto = 0, numeroPosto2 = 0;
		
		do {
			System.out.println("menu:");
			System.out.println("[1] per simulare l'arrivo di un automobilista");
			System.out.println("[2] per simulare la partenza di un automobilista");
			System.out.println("[3] per uscire");
			scelta = tastiera.nextInt();
			valido = ClientParcheggio.checkScelta(scelta, numeroPosto, numeroPosto2, automobilisti, pieno);
			if (valido) {
				if (scelta == 1) {
					a1 = new Automobilista(numeroAutomobilisti+1);
					a1.setParcheggio(parcheggio);
					automobilisti[numeroPosto] = a1;
					automobilisti[numeroPosto].parcheggiaAuto(parcheggio);
					numeroAutomobilisti ++;
					numeroPosto ++;
					if (numeroPosto == parcheggio.getPostiAuto().length)
						numeroPosto = 0;
					if (automobilisti[numeroPosto] != null) {
						pieno = true;
					}
				} else if (scelta == 2) {
					automobilisti[numeroPosto2].ritiraAuto();
					automobilisti[numeroPosto2] = null;
					numeroPosto2 ++;
					if (numeroPosto2 == parcheggio.getPostiAuto().length)
						numeroPosto2 = 0;
					if (pieno) {
						pieno = false;
					}
				} else if (scelta == 3) {
					System.out.println("arrivederci");
					break;
				}
			}
		} while(true);	
		tastiera.close();
	}
}
