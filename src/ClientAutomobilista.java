import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientAutomobilista {
	
	public static class ClientAutomobilistaThread extends Thread {
		private int port;
		private String host;
		private int scelta;
		private Socket socket;
		
		public ClientAutomobilistaThread(int sceltaClient) {
			super("ClientAutomobilistaThread");
			this.port = 53536;
			this.host = "localhost";
			this.scelta = sceltaClient;
		}
		
		public void run() {
			ObjectOutputStream outputStream;
			ObjectInputStream inputStream;
			ArrayList<String> parcheggi = null;
			
			try {
				//mi connetto al server
				socket = new Socket(host, port);
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				//invio la mia scelta (1 o 2) al server
				outputStream.writeInt(scelta);
				outputStream.flush();
				inputStream = new ObjectInputStream(socket.getInputStream());
				if (scelta == 1) {
					//se avevo scelto di avere la lista di parcheggi liberi, mi verrà ritornata dal server
					parcheggi = (ArrayList<String>) inputStream.readObject();
						if (parcheggi.size() == 0) 
							//se la lista è vuota, al momento non ci sono parcheggi liberi
							System.out.println("nessun parcheggio libero al momento");
						else  {
							System.out.println("Lista parcheggi: " + parcheggi.toString());	
						}
				}
				if (scelta == 2) {
					System.out.println("arrivederci");
					inputStream.close();
					outputStream.close();
					socket.close();
				}
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
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
		int scelta = 0;
		// l'automobilista può scegliere se richiedere la lista dei parcheggi liberi o terminare
		while (scelta != 2) {
			System.out.println("menù: ");
			System.out.println("[1] lista parcheggi liberi");
			System.out.println("[2] per uscire");
			
			scelta = tastiera.nextInt();
			if (scelta < 1 || scelta > 2) 
				System.out.println("inserimento invalido");
			else
				new ClientAutomobilistaThread(scelta).start();
		}
		
		tastiera.close();
	}
}


