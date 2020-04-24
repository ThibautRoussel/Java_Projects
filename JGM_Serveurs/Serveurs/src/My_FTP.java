
import java.io.*;
import java.net.*;
import java.util.*;

public class My_FTP implements Runnable {
	Socket client; // liaison avec client
	BufferedReader depuisClient; // réception de requête
	PrintWriter versClient; // envoi des réponses
	String path = ".";

	public My_FTP(Socket client) {
		this.client = client;
		try {
			// création des flots de/vers le client
			depuisClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			versClient = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
			// message d'accueil
			/*
			 * versClient.println("Bienvenue sur le serveur FTP.");
			 * versClient.println("Entrez 'dir' pour obtenir la liste des fichiers.");
			 * versClient.println("Entrez 'pwd' pour obtenir le chemin du repertoir courant"
			 * ); versClient.
			 * println("Entrez 'cd <rep>' pour obtenir la liste des fichiers du repertoire <rep>."
			 * ); versClient.
			 * println("Entrez 'get <file>' pour obtenir le contenu du fichier <file>.");
			 * versClient.println("Envoyez une chaîne vide pour fermer la connexion.");
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

	public void sendClient(String str) {
		File f = new File(str);
		for (int i = 0; i < f.list().length; i++) {
			versClient.println(f.list()[i]);
			System.out.println("out : " + f.list()[i]);
		}
		versClient.println("EOF_HALT");
	}

	public void run() {
		boolean fini = false; // drapeau
		StringTokenizer st;
		try {
			String lue; // la requête
			String pathtmp = "";
			while (!fini) {
				lue = depuisClient.readLine();
				System.out.println("chaine in : " + lue);

				if (lue == null)
					fini = true;

// traitement du dir
				else if (lue.contains("dir")) {
					// System.out.println(path);
					sendClient(path);

// traitement du cd
				} else if (lue.contains("cd")) {
					st = new StringTokenizer(lue);
					st.nextToken();
					String nomdir = st.nextToken();
					pathtmp = (path + "/" + nomdir);
					File f = new File(pathtmp);
					if (f.exists()) {
						path = f.getCanonicalPath();
						sendClient(path);
					} else {
						versClient.println("Le repertoire n'existe pas");
					}

// traitement du get
				} else if (lue.contains("get")) {
					try {
						st = new StringTokenizer(lue);
						st.nextToken();
						String nomfile = st.nextToken();
						pathtmp = (path + "/" + nomfile);
						File f = new File(pathtmp);
						if (f.exists()) {
							InputStream ips = new FileInputStream(pathtmp);
							InputStreamReader ipsr = new InputStreamReader(ips);
							BufferedReader br = new BufferedReader(ipsr);
							String line;
							while ((line = br.readLine()) != null) {
								System.out.println(line);
								versClient.println(line);
							}
							versClient.println("EOF_HALT");
							br.close();

						} else {
							versClient.println("Le fichier n'existe pas");
						}
						System.out.println(path);

					} catch (Exception er) {
						er.toString();
					}

// traitement du pwd
				} else if (lue.contains("pwd")) {
					File f = new File(path);
					versClient.println(f.getCanonicalPath());

// traitement du check
				} else if (lue.contains("check")) {
					st = new StringTokenizer(lue);
					st.nextToken();
					String nmfile = st.nextToken();
					String newpath = (path + "/" + nmfile);
					File f = new File(newpath);
					if (f.isFile()) {
						versClient.println("file");
						System.out.println("file");
					} else if (f.isDirectory()) {
						versClient.println("dir");
						System.out.println("dir");
					}

				} else {
					fini = true;
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