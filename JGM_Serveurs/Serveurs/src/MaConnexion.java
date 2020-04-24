import java.io.*;
import java.net.*;
import java.util.*;

public class MaConnexion implements Runnable {
	Socket client; // liaison avec client
	BufferedReader depuisClient; // r�ception de requ�te
	PrintWriter versClient; // envoi des r�ponses

	public MaConnexion(Socket client) {
		this.client = client;
		try {
			// cr�ation des flots de/vers le client
			depuisClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			versClient = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
			// message d'accueil
			versClient.println("Bienvenue sur le serveur d'heure.");
			versClient.println("Entrez h pour obtenir l'heure.");
			versClient.println("Envoyez une cha�ne vide pour fermer la connexion.");
		} catch (IOException e) {
			try {
				client.close();
			} catch (IOException ee) {
			}
		}
		// mise en route du processus par appel de la m�thode run
		new Thread(this).start(); //ca lance le run
	}

	public void run() {
		boolean fini = false; // drapeau
		try {
			String lue; // la requ�te
			String rep; // la r�ponse
			while (!fini) {
				lue = depuisClient.readLine();
				System.out.println("chaine" + lue);
				if (lue == null)
					fini = true;
				else if (!lue.contains("h"))
					fini = true;
				else {
					// on envoie l'heure
					Calendar cal = Calendar.getInstance();
					rep = "Il est " + cal.get(Calendar.HOUR) + "h" + cal.get(Calendar.MINUTE) + "mn"
							+ cal.get(Calendar.SECOND) + "s.";
					versClient.println(rep);
				}
			}
		} catch (IOException e) {
			System.out.println("Exception entr�e/sortie : " + e.getMessage());
		}
		// fermeture de la connexion
		stop();
	}

	public void stop() {
		try {
			versClient.println("Au revoir !");
			client.close();
		} catch (IOException e) {
			System.out.println("Exception � la fermeture d'une connexion : " + e);
		}
	}
}