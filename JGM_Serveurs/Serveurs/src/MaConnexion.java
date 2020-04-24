import java.io.*;
import java.net.*;
import java.util.*;

public class MaConnexion implements Runnable {
	Socket client; // liaison avec client
	BufferedReader depuisClient; // réception de requête
	PrintWriter versClient; // envoi des réponses

	public MaConnexion(Socket client) {
		this.client = client;
		try {
			// création des flots de/vers le client
			depuisClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			versClient = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
			// message d'accueil
			versClient.println("Bienvenue sur le serveur d'heure.");
			versClient.println("Entrez h pour obtenir l'heure.");
			versClient.println("Envoyez une chaîne vide pour fermer la connexion.");
		} catch (IOException e) {
			try {
				client.close();
			} catch (IOException ee) {
			}
		}
		// mise en route du processus par appel de la méthode run
		new Thread(this).start(); //ca lance le run
	}

	public void run() {
		boolean fini = false; // drapeau
		try {
			String lue; // la requête
			String rep; // la réponse
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
			System.out.println("Exception entrée/sortie : " + e.getMessage());
		}
		// fermeture de la connexion
		stop();
	}

	public void stop() {
		try {
			versClient.println("Au revoir !");
			client.close();
		} catch (IOException e) {
			System.out.println("Exception à la fermeture d'une connexion : " + e);
		}
	}
}