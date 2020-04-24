import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.StringTokenizer;
import java.io.*;

public class CliTEL2 extends Frame implements Runnable, ActionListener {

	int portServeur;
	String adresseServeur, nom, pren, num;
	Thread processus = null;
	Label tele = new Label("Connexion à l'annuare téléphonique");
	Socket sk;
	BufferedReader depuisServeur;
	PrintWriter versServeur;
	TextField name, prenom, tel;

	public CliTEL2(String adresse, int port) {
		setSize(500, 100);
		portServeur = port;
		adresseServeur = adresse;
//évènement fermeture
		addWindowListener(new Fermeture());
//création de l'interface
		Panel p = new Panel();
		p.setBackground(Color.white);
		add(BorderLayout.CENTER, p);
		p.setLayout(new GridLayout(2, 1));
		p.add(tele);
		tele.setAlignment(Label.CENTER);

		Panel p1 = new Panel();
		p1.setLayout(new GridLayout(1, 2));
		p.add(p1);
		name = new TextField(10);
		name.addActionListener(this);
		prenom = new TextField(10);
		prenom.setEditable(false);
		tel = new TextField(10);
		tel.setEditable(false);

		p1.add(name);
		p1.add(prenom);
		p1.add(tel);

		processus = new Thread(this);
		processus.start();
	}

	public void connect() {
		try {
			sk = new Socket(adresseServeur, portServeur);
			depuisServeur = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			versServeur = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()), true);
		} catch (Exception e) {
			tel.setText(e.toString());
		}
	}

	public void disconnect() {
		try {
			sk.close();
		} catch (IOException e) {
		}
	}

	public void actionPerformed(ActionEvent e) {
		try {
			nom = name.getText();
			versServeur.println(nom);

		} catch (Exception er) {
			er.toString();
		}
	}

	public void run() {
		boolean fini = false;
		try {
			connect();
			String ligne;
			while (!fini) {
				ligne = depuisServeur.readLine();
				if (ligne == null) {
					fini = true;
				} else if (ligne.startsWith("Aucune")) {
					String rep = MsgBox2.getNomPrenomTel(this, nom);
					if (!rep.contains("false")) {
						versServeur.println(rep);
					}
				} else if (ligne.startsWith("Pas de")){
					String rep = MsgBox2.getPrenomTel(this, pren);
					if (!rep.contains("false")) {
						versServeur.println(rep);
					}
				} else if (ligne.contains("revoir")) {
					tel.setText(ligne);
					System.exit(0);
				} else if (ligne.contains("emptyline")) {
					boolean flag = MsgBox.affQuest(this, "Voulez vous vraiment quitter?");
					if (flag == true) {
						versServeur.println("quit");
					}
				} else if (ligne.contains("needName")) {
					String rep = MsgBox2.affPrenomMsg(this, "");
					if (!rep.contains("false")) {
						pren = rep;
						versServeur.println("+" + pren);
					}
				} else {
					StringTokenizer st = new StringTokenizer(ligne, ":", false);
					nom = st.nextToken();
					pren = st.nextToken();
					num = st.nextToken();
					name.setText(nom);
					prenom.setText(pren);
					tel.setText(num);
				}
			}
		} catch (IOException e) {
			tel.setText("Connexion impossible.");
		} finally {
			processus = null;
		}
	}

	public static void main(String args[]) {
		System.out.println("Chargement en cours...");
		String adr = "localhost";

		if (args.length > 0)
			adr = args[0];
		int p = 1236;
		if (args.length > 1) {
			try {
				p = Integer.parseInt(args[1]);
			} catch (Exception e) {
				p = 1234;
			}
		}
		CliTEL2 cli = new CliTEL2(adr, p);
		cli.setTitle("Annuaire Télephonique");
		cli.setVisible(true);

	}
}