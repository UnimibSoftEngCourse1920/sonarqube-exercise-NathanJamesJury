import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CarParksServer {
	//lista dei parcheggi liberi
	private static ArrayList<String> parcheggi = new ArrayList<>();
	
	public static class CPParcheggioThreadHandler extends Thread {
		private ServerSocket socket;
		
		public CPParcheggioThreadHandler(ServerSocket socket) {
			super("CPParcheggioThreadHandler");
			this.socket = socket;
		}
		
		public void run() {
			System.out.println("Sono in ascolto per parcheggi");
			while (true) {
				try {
					//ad ogni nuova connessione di un parcheggio faccio partire un thread per gestire quella comunicazione
					new CPParcheggioServerThread(socket.accept()).start();
				} catch (IOException e) {
					System.err.println("couldn't listen on port 53535");
				}
			}
		}
	}
	
	public static class CPAutomobilistaThreadHandler extends Thread {
		private ServerSocket socket;
	
		public CPAutomobilistaThreadHandler(ServerSocket socket) {
			super("CPAutomobilistaThreadHandler");
			this.socket = socket;
		}
		
		public void run() {
			System.out.println("Sono in ascolto per automobilisti");
			while (true) {
				try {
					//ad ogni nuova connessione di un automobilista faccio partire un thread per gestire quella comunicazione
					new CPAutomobilistaServerThread(socket.accept()).start();
				} catch (IOException e) {
					System.err.println("couldn't listen on port 53536");
				}
			}
		}
	}
	
	public static class CPParcheggioServerThread extends Thread {
		private Socket socket = null; 
		
		public CPParcheggioServerThread(Socket socket) {
			super("CPParcheggioServerThread");
			this.socket = socket;
		}
		
		public void run() {
			try {
				//ricevo il nome del parcheggio che vuole comunicare con il server
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream()); 
				String nomeParcheggio = (String) inputStream.readObject();
				boolean isLibero = inputStream.readBoolean();
				synchronized(this) {
					if (isLibero)
						//se isLibero è true vuol dire che il parcheggio ha posti liberi e deve essere aggiunto alla lista 
						//del server
						parcheggi.add(nomeParcheggio);
					else
						//se isLibero è false vuol dire che il parcheggio non ha più posti liberi e deve essere tolto dalla lista 
						//del server
						parcheggi.remove(nomeParcheggio);
				}
				System.out.println(parcheggi.toString());
			} catch (Exception e) {}
		}
	}
	
	public static class CPAutomobilistaServerThread extends Thread {
		private Socket socket = null;
		
		public CPAutomobilistaServerThread(Socket socket) {
			super("CPAutomobilistaServerThread");
			this.socket = socket;
		}
		
		public void run() {
			ObjectInputStream inputStream;
			ObjectOutputStream outputStream;
			int scelta = 0;
			
			try {
				inputStream = new ObjectInputStream(socket.getInputStream());
				//ricevo la scelta dell'automobilista, cioé se vuole la lista dei parcheggi liberi oppure se vuole uscire
				while ((scelta = inputStream.readInt()) == 0) {};
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				if(scelta == 1) {
					System.out.println("ho ricevuto la scelta dal client");
					//ritorno la lista dei parcheggi liberi
					outputStream.writeObject(parcheggi);
				}
				if (scelta == 2) {
					System.out.println("client ha terminato");
					inputStream.close();
					outputStream.close();
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static ServerSocket serverSocket = null;
	private static ServerSocket serverSocket2 = null;
	
	public static void main(String[] args) {
		 //lancio subito due thread che saranno in ascolto sulle porte 53535 (per parcheggi) e 53536 (per automobilisti)
		 try {
			 serverSocket = new ServerSocket(53535);
			 new CPParcheggioThreadHandler(serverSocket).start();
			 
		 } catch (Exception e) {
			 System.err.println("errore nel main");
			 System.exit(-1);
		 } finally {
			 Runtime.getRuntime().addShutdownHook(new Thread() { public void run() {
				    try {
				        serverSocket.close();
				        System.out.println("The server is shut down!");
				    } catch (IOException e) { /* failed */ }
			}});
		 }
		 try {
			 serverSocket2 = new ServerSocket(53536);
			 new CPAutomobilistaThreadHandler(serverSocket2).start();
			 
		 } catch (Exception e) {
			 System.err.println("errore nel main");
			 System.exit(-1);
		 } finally {
			 Runtime.getRuntime().addShutdownHook(new Thread() { public void run() {
				    try {
				        serverSocket2.close();
				        System.out.println("The server is shut down!");
				    } catch (IOException e) { /* failed */ }
			}});
		 }
	}

}
