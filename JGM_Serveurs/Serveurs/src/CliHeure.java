import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

@SuppressWarnings("serial")
public class CliHeure extends Frame implements Runnable, ActionListener {
	int portServeur;
	String adresseServeur;
	Thread processus = null;
	Label heure = new Label("Connexion au serveur d'heure");
	Socket sk;
	BufferedReader depuisServeur;
	PrintWriter versServeur;

	public CliHeure(String adresse, int port) {
		setSize(400, 200);
		this.setLocation(new Point (700, 450));
		portServeur = port;
		adresseServeur = adresse;
//évènement fermeture
		addWindowListener(new Fermeture());
//création de l'interface
		Panel p = new Panel();
		p.setBackground(Color.white);
		add(BorderLayout.CENTER, p);
		p.setLayout(new GridLayout(2, 1));
		p.add(heure);
		heure.setAlignment(Label.CENTER);
		Panel p1 = new Panel();
		p.add(p1);
		Button b = new Button("Heure");
		p1.add(b);
		b.addActionListener(this);
		//pack();
//processus d'attente des messages
		processus = new Thread(this);
		processus.start();
	}

	public void connect() {
		try {
			sk = new Socket(adresseServeur, portServeur);
			depuisServeur = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			versServeur = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()), true);
//demander l'heure
			versServeur.println("h");
		} catch (Exception e) {
			heure.setText(e.toString());
		}
	}

	public void disconnect() {
		try {
			sk.close();
		} catch (IOException e) {
		}
	}

	public void run() {
		boolean fini = false;
		try {
			connect();
			String ligne;
			while (!fini) {
				ligne = depuisServeur.readLine();
				if (ligne == null)
					fini = true;
				else if (ligne.startsWith("Au revoir"))
					fini = true;
				else if (ligne.startsWith("Il est"))
					heure.setText(ligne);
			}
		} catch (IOException e) {
			heure.setText("Connexion impossible.");
		} finally {
			processus = null;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Heure"))
			versServeur.println("h");
	}

	public static void main(String args[]) {
		System.out.println("Chargement en cours...");
		String adr = "localhost";
		if (args.length > 0)
			adr = args[0];
		int p = 1234;
		if (args.length > 1) {
			try {
				p = Integer.parseInt(args[1]);
			} catch (Exception e) {
				p = 1234;
			}
		}
		CliHeure cli = new CliHeure(adr, p);
		cli.setTitle("Heure");
		cli.setVisible(true);
	}
}