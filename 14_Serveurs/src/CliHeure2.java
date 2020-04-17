
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class CliHeure2 extends Frame implements Runnable, ActionListener {
	
	int portServeur;
	String adresseServeur;
	Thread processus = null;
	Label heure = new Label("Connexion au serveur d'heure");
	Socket sk;
	BufferedReader depuisServeur;
	PrintWriter versServeur;

	public CliHeure2(String adresse, int port) {
		setSize(200,200);
		portServeur = port;
		adresseServeur = adresse;
//�v�nement fermeture
		addWindowListener(new Fermeture());
//cr�ation de l'interface
		Panel p = new Panel();
		p.setBackground(Color.white);
		add(BorderLayout.CENTER, p);
		p.setLayout(new GridLayout(2, 1));
		p.add(heure);
		heure.setAlignment(Label.CENTER);
		Panel p1 = new Panel();
		p.add(p1);
		Button b = new Button("Stop");
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

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Stop"))
			versServeur.println("");
		try
		{
			processus.stop();
		}
		catch (Exception ex)
		{
			System.out.println(ex.toString());
		}
		System.exit(0);
			
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

	public static void main(String args[]) {
		System.out.println("Chargement en cours...");
		String adr="localhost";
		
		if (args.length>0) adr=args[0];
		int p=1234;
		if (args.length>1) {
		try {
		p=Integer.parseInt(args[1]);
		}
		catch (Exception e) {p=1234;}
		}
		CliHeure2 cli=new CliHeure2(adr,p);
		cli.setTitle("Heure");
		cli.setVisible(true);
		
		}
}
