import java.awt.Color;
import java.io.*;
import java.net.*;
import java.util.*;

public class My_TEL2 implements Runnable {
	Socket client; // liaison avec client
	BufferedReader depuisClient, br; // réception de requête
	PrintWriter versClient, pr; // envoi des réponses
	Hashtable<String, String> dico;
	java.util.List<String> trietab;
	String filename = "annuaire2.txt";

	public My_TEL2(Socket client) {
		this.client = client;
		dico = new Hashtable<String, String>();
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
		String prenom = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			while (null != (line = br.readLine())) {
				System.out.println(line);
				st = new StringTokenizer(line, ":", false);
				nom = st.nextToken();
				prenom = st.nextToken();
				numero = st.nextToken();
				dico.put(nom + "_" + prenom, numero);

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
			String nom = "";
			String prenom = "";
			boolean modePrenom = false;
			while (!fini) {
				lue = depuisClient.readLine();
				System.out.println("chaine in : " + lue);
				if (lue.isEmpty()) {
					versClient.println("emptyline");
				} else if (lue.contains("quit")) {
					fini = true;
				} else if (lue.contains(":")) {
					if (modePrenom == true) {
						SaveText(nom+":"+lue);
						String strp = nom + "_" + prenom;
						String phonep = (String) dico.get(strp);
						versClient.println(nom + ":" + prenom + ":" + phonep);
					}else {
						SaveText(lue);
						StringTokenizer st = new StringTokenizer(lue, ":", false);
						nom = st.nextToken();
						prenom = st.nextToken();
						String strp = nom + "_" + prenom;
						String phonep = (String) dico.get(strp);
						versClient.println(nom + ":" + prenom + ":" + phonep);
					}
					modePrenom = false;
				} else if (lue.startsWith("+")) {
					StringTokenizer st = new StringTokenizer(lue, "+", false);
					prenom = st.nextToken();
					String strp = nom + "_" + prenom;
					String phonep = (String) dico.get(strp);
					if (isNameExist(prenom) == true) {
						versClient.println(nom + ":" + prenom + ":" + phonep);
						System.out.println(nom + ":" + prenom + ":" + phonep);
					} else {
						versClient.println("Pas de correspondance prenom trouvee");
						modePrenom = true;
					}
				} else {
					int nbr = getNbLineBeginingBy(lue);
					switch (nbr) {
					case 0:
						versClient.println("Aucune correspondance trouvee");
						break;
					case 1:
						String str = lue + "_" + getPrenom(lue);
						String phone = (String) dico.get(str);
						String rep = lue + ":" + getPrenom(lue) + ":" + phone;
						versClient.println(rep);
						break;
					default:
						nom = lue;
						versClient.println("needName");
						break;
					}
				}
			}
		} catch (

		IOException e) {
			System.out.println("Exception entrée/sortie : " + e.getMessage());
		}
		// fermeture de la connexion
		stop();
	}

	public String getPrenom(String nom) {
		String rep = "";
		String ligne;
		StringTokenizer st;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			while ((ligne = br.readLine()) != null) {
				System.out.println(ligne);
				if (ligne.startsWith(nom)) {
					st = new StringTokenizer(ligne, ":");
					st.nextToken();
					rep = st.nextToken();
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return rep;
	}

	public void SaveText(String line) {
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			String lines = "";
			Vector<String> monBuffer = new Vector<String>();
			while (null != (lines = br.readLine())) {
				monBuffer.addElement(lines);
			}
			br.close();
			monBuffer.addElement(line);
			pr = new PrintWriter(new FileOutputStream(filename));
			for (int i = 0; i < monBuffer.size(); i++) {
				pr.println(monBuffer.get(i).toString());
			}
			pr.close();

		} catch (IOException e) {
			e.toString();
		}
		interrogeAnnuaire();
	}

	public int getNbLineBeginingBy(String str) {
		int nb = 0;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			String lines = "";
			while (null != (lines = br.readLine())) {
				if (lines.startsWith(str)) {
					nb += 1;
				}
			}
			br.close();

		} catch (IOException e) {
			e.toString();
		}
		return nb;
	}

	public boolean isNameExist(String str) {
		boolean b = false;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			String lines = "";
			while (null != (lines = br.readLine())) {
				if (lines.contains(str)) {
					b = true;
				}
			}
			br.close();

		} catch (IOException e) {
			e.toString();
		}
		return b;
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