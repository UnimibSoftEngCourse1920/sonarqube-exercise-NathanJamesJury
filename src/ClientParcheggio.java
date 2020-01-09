import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientParcheggio {
	public static class ClientParcheggioThread extends Thread {
		private String host;
		private int port;
		private Parcheggio parcheggio;
		private boolean isLibero;
		private Socket socket;
		
		public ClientParcheggioThread(Parcheggio parcheggio, boolean isLibero) {
			super("ClientParcheggioThread");
			this.host = "localhost";
			this.port = 53535;
			this.parcheggio = parcheggio;
			this.isLibero = isLibero;
		}
		
		public void run() {
			ObjectOutputStream outputStream;
			
			try {
				socket = new Socket(host, port);
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				outputStream.writeObject(parcheggio.getNome());
				outputStream.writeBoolean(isLibero);
				outputStream.flush();
				outputStream.close();
				socket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				 Runtime.getRuntime().addShutdownHook(new Thread() { public void run() {
					    try {
					        socket.close();
					        System.out.println("The server is shut down!");
					    } catch (IOException e) { /* failed */ }
				}});
			 }
		}
	}
	
	public static void main(String[] args) {
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
		//qui devono essere inseriti gli ID dei parcheggiatori diversi tra loro per poterli distinguere
		while (ID != -1) {
			parcheggiatori.add(new Parcheggiatore(ID));			
			ID = tastiera.nextInt();
		}
		
		Parcheggiatore[] parcheggiatoriArray = listToArray(parcheggiatori);
		//creo il parcheggio 
		parcheggio = new Parcheggio(nome, numeroPosti, parcheggiatoriArray);
		//se il parcheggio è appena stato creato allora avrà tutti i posti liberi, dunque creo una connessione con il server
		//e chiedo di inserire il nome del mio parcheggio nella sua lista di parcheggi liberi 
		new ClientParcheggioThread(parcheggio, isLibero).start();
		int scelta;
		boolean valido, pieno = false;
		Automobilista a1;
		//questo array conterrà tutti gli automobilisti inseriti successivamente da console
		Automobilista[] automobilisti = new Automobilista[parcheggio.getPostiAuto().length];
		int numeroAutomobilisti = 0, numeroPosto = 0, numeroPosto2 = 0;
		do {
			/*qui sarà libero di inserire o rimuovere ciclicamente a piacere automobilisti dal parcheggio, attenzione che se
			 *il parcheggio fosse vuoto, non può inserire un nuovo automobilista e rimuoverlo prima che abbia finito di parcheggiare
			 */
			System.out.println("menu:");
			System.out.println("[1] per simulare l'arrivo di un automobilista");
			System.out.println("[2] per simulare la partenza di un automobilista");
			System.out.println("[3] per uscire");
			scelta = tastiera.nextInt();
			//controllo che la scelta sia valida
			valido = checkScelta(scelta, numeroPosto, numeroPosto2, automobilisti, pieno);
			if (valido) {
				if (scelta == 1) {
					a1 = new Automobilista(numeroAutomobilisti+1);
					a1.setParcheggio(parcheggio);
					//aggiungo un nuovo automobilista al parcheggio
					automobilisti[numeroPosto] = a1;
					//parcheggio la sua auto
					automobilisti[numeroPosto].parcheggiaAuto(parcheggio);
					numeroAutomobilisti ++;
					/*aggiungo (per poi parcheggiare) e rimuovo (per poi ritirare) gli automobilisti dall'array "automobilisti"
					 *in ordine, dal primo all'ultimo posto dell'array. Le varabili numeroPosto e numeroPosto2 mi servono
					 *per scorrere questo array, per aggiungere e rimuovere gli automobilisti nell'ordine giusto (il primo
					 *arrivato sarà poi il primo ad essere rimosso
					 */
					numeroPosto ++;
					if (numeroPosto == parcheggio.getPostiAuto().length)
						//una volta che ho iterato tutto l'array torno al primo elemento
						numeroPosto = 0;
					if (automobilisti[numeroPosto] != null) {
						//una volta che ho aggiornato numeroPosto, se questo punta a un valore che non è null (cioè ad un altro
						//automobilista) vuol dire che il parcheggio è pieno 
						new ClientParcheggioThread(parcheggio, false).start();
						pieno = true;
					}
				} else if (scelta == 2) {
					automobilisti[numeroPosto2].ritiraAuto();
					automobilisti[numeroPosto2] = null;
					numeroPosto2 ++;
					if (numeroPosto2 == parcheggio.getPostiAuto().length)
						numeroPosto2 = 0;
					if (pieno) {
						//se il parcheggio precedentemente era pieno, ora che ho rimosso un autmobilista sarà di nuovo libero
						pieno = false;
						//dunque chiedo al server di rimettere il nome del mio parcheggio dentro la sua lista di parcheggi liberi
						new ClientParcheggioThread(parcheggio, true).start();
					}
				} else if (scelta == 3) {
					System.out.println("arrivederci");
					//se esco dal programma (magari il parcheggio si è chiuso) lo comunico al server dicendo di togliere il mio
					//nome dalla lista di parcheggi liberi
					new ClientParcheggioThread(parcheggio, false).start();
					break;
				}
			}
		} while(true);	
		tastiera.close();
	}
	
	//mi trasforma l'arrayList passato come parametro in un array
	public static Parcheggiatore[] listToArray(ArrayList<Parcheggiatore> parcheggiatori) {
		Parcheggiatore[] parcheggiatoriArray = new Parcheggiatore[parcheggiatori.size()];
		int i = 0;
		for (Parcheggiatore parcheggiatore : parcheggiatori) {
			parcheggiatoriArray[i] = parcheggiatore;
			i ++;
		}
		return parcheggiatoriArray;
	}
	
	public static boolean checkScelta(int scelta, int numeroPosto, int numeroPosto2, Automobilista[] automobilisti, boolean pieno) {
		if (scelta < 1 || scelta > 3) {
			System.out.println("scelta non valida, inserisca 1, 2, 3");
			return false;
		}  else if (scelta == 1) {
			if (pieno) {
				//non potrà inserire altri automobilisti se il parcheggio è già pieno
				System.out.println("parcheggio pieno, non è possibile inserire altri automobilisti");
				return false;
			}
			else 
				return true;
		}  else if (scelta == 2) {
			if (automobilisti[numeroPosto2] == null) {
				//non potrà rimuovere altri automobilisti se il parcheggio è già vuoto
				System.out.println("parcheggio vuoto, non è possibile rimuovere altri automobilisti");
				return false;
			}
			else 
				return true;
		}  else if (scelta == 3)
			return true;
		else
			return false;
	}
}

