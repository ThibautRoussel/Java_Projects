import java.awt.Color;
import java.io.*;
import java.net.*;
import java.util.*;

public class My_TEL implements Runnable {
	Socket client; // liaison avec client
	BufferedReader depuisClient; // réception de requête
	PrintWriter versClient, pr; // envoi des réponses
	Hashtable dico;
	java.util.List<String> trietab;

	public My_TEL(Socket client) {
		this.client = client;
		dico = new Hashtable();
		interrogeAnnuaire();

		try {
			// création des flots de/vers le client
			depuisClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			versClient = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
			// message d'accueil
			/*
			 * versClient.println("Bienvenue sur le serveur Annuaire");
			 * versClient.println("Entrez un nom pour obtenir le numero de telephone");
			 * versClient.println("Envoyez une chaîne vide pour fermer la connexion.");
			 */
			/*
			 * for (int i = 0; i <= 20; i++) { depuisClient.read(); }
			 */

		} catch (IOException e) {
			try {
				client.close();
			} catch (IOException ee) {
			}
		}
		// mise en route du processus par appel de la méthode run
		new Thread(this).start(); // ca lance le run
	}

	public void interrogeAnnuaire() {
		StringTokenizer st;
		String nom = "";
		String line;
		String numero = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("annuaire.txt")));
			while (null != (line = br.readLine())) {
				System.out.println(line);
				st = new StringTokenizer(line, ":", false);
				nom = st.nextToken();
				numero = st.nextToken();
				dico.put(nom, numero);

			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void run() {
		boolean fini = false; // drapeau
		try {
			String lue; // la requête
			while (!fini) {
				lue = depuisClient.readLine();
				System.out.println("chaine in : " + lue);
				if (lue.isEmpty()) {
					versClient.println("emptyline");
				} else if (lue.contains("quit")) {
					fini = true;
				} else if (lue.contains(":")) {
					SaveText(lue);

				} else {
					String rep = (String) dico.get(lue);
					if (rep != null) {
						versClient.println(rep);
					} else {
						versClient.println("Aucune correspondance trouvee");
					}
				}
			}
		}catch(

	IOException e)
	{
		System.out.println("Exception entrée/sortie : " + e.getMessage());
	}

	// fermeture de la connexion
	stop();

	}

	public void SaveText(String line) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("annuaire.txt")));
			String lines = "";
			Vector<String> monBuffer = new Vector<String>();
			while (null != (lines = br.readLine())) {
				monBuffer.addElement(lines);
			}
			br.close();
			monBuffer.addElement(line);
			pr = new PrintWriter(new FileOutputStream("annuaire.txt"));
			for (int i = 0; i < monBuffer.size(); i++) {
				pr.println(monBuffer.get(i).toString());
			}
			pr.close();

		} catch (IOException e) {
			e.toString();
		}
		interrogeAnnuaire();
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