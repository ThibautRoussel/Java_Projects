import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

@SuppressWarnings({ "serial" })
public class CliFTP extends Frame implements ItemListener, ActionListener {

	TextField console, chemin;
	TextField host, port;
	Button connect;
	List liste;
	TextArea contenu;
	Socket sk;
	BufferedReader depuisServeur;
	PrintWriter versServeur;
	boolean bstate = true;
	FileDialog fDial;
	PrintWriter pr;
	boolean fileSaved = false;

	public CliFTP() {
		setLayout(new BorderLayout());
		setSize(1100, 800);
		setFont(new Font("TimesRoman", Font.BOLD, 18));
		addWindowListener(new Fermeture());

		fDial = new FileDialog(this, "Choisir", FileDialog.SAVE);

		MenuItem mi;

		MenuBar mb = new MenuBar();
		setMenuBar(mb);

		Menu m1 = new Menu("Fichier");
		mb.add(m1);
		mi = new MenuItem("Save");
		mi.addActionListener(this);
		m1.add(mi);
		mi = new MenuItem("Save as");
		mi.addActionListener(this);
		m1.add(mi);
		m1.addSeparator();
		mi = new MenuItem("Quitter");
		mi.addActionListener(this);
		m1.add(mi);

		console = new TextField(10);
		console.setEditable(false);

		chemin = new TextField(10);
		chemin.setEditable(false);

		host = new TextField(10);
		host.setText("127.0.0.1");

		port = new TextField(10);
		port.setText("1235");

		connect = new Button("Connect");
		connect.addActionListener(this);

		Panel nord = new Panel();
		Panel nordfull = new Panel();
		nordfull.setLayout(new GridLayout(2, 1));
		nord.setLayout(new GridLayout(1, 5));
		nord.add(new Label("Host : "));
		nord.add(host);
		nord.add(new Label("Port : "));
		nord.add(port);
		nord.add(connect);

		nordfull.add(nord);
		nordfull.add(chemin);

		liste = new List();
		liste.addItemListener(this);

		contenu = new TextArea();
		contenu.setEditable(false);

		add("North", nordfull);
		add("South", console);
		add("West", liste);
		add("Center", contenu);
	}

	public void cleanInterface() {
		contenu.setText("");
		liste.removeAll();
		chemin.setText("");
	}

	public void modButton(String str) {
		bstate = !bstate;
		port.setEnabled(bstate);
		host.setEnabled(bstate);
		connect.setLabel(str);
		connect.setActionCommand(str);
	}

	public void sendToServ(String cmd) {
		versServeur.println(cmd);
	}

	@SuppressWarnings("deprecation")
	public void readFromServ() {
		String line;
		try {
			liste.removeAll();
			liste.addItem(".");
			liste.addItem("..");
			line = depuisServeur.readLine();
			do {

				if (line.contains(".") && line.indexOf('.') > 0) {
					if (line.endsWith(".txt") || line.endsWith(".java") || line.endsWith(".xml")) {
						liste.addItem(line);
					}
				} else {
					liste.addItem(line);
				}
				line = depuisServeur.readLine();
			} while (!line.startsWith("EOF_HALT"));
		} catch (Exception e) {
			e.toString();
		}
	}

	public void readFromServFile() {
		contenu.setText("entre dans lecture fichier");
		String line;
		String content = "";
		try {
			line = depuisServeur.readLine();
			do {
				content += (line + "\n");
				line = depuisServeur.readLine();
			} while (!line.startsWith("EOF_HALT"));
			contenu.setText(content);
		} catch (Exception exp) {
			exp.toString();
		}
	}

	public void readPwd(boolean b, String str) {
		try {
			sendToServ("pwd");
			if (b == true) {
				chemin.setText(depuisServeur.readLine() + "\\" + str);
			} else {
				chemin.setText(depuisServeur.readLine());
			}
		} catch (IOException e) {
		}
	}

	public boolean isFile() {
		boolean flag = false;
		String ligne;
		try {
			ligne = depuisServeur.readLine();
			if (ligne.startsWith("file")) {
				flag = true;
			} else if (ligne.startsWith("dir")) {
				flag = false;
			}
		} catch (Exception e) {
			console.setText(e.toString());
		}
		return flag;
	}

	public void Connect() {
		try {
			sk = new Socket(host.getText(), Integer.parseInt(port.getText()));
			depuisServeur = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			versServeur = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()), true);
			console.setText("Connexion établie avec le serveur " + host.getText() + "...");

			modButton("Disconnect");

			sendToServ("dir");
			readFromServ();
			readPwd(false, "");

		} catch (Exception e) {
			console.setText(e.toString());
		}
	}

	public void Disconnect() {
		try {
			sk.close();
			console.setText("Connexion terminée avec le serveur " + host.getText() + "...");

			cleanInterface();

			modButton("Connect");

		} catch (IOException e) {
		}
	}

	public void saveText(String fic) {
		try {
			if (fic.length() != 0) {
				pr = new PrintWriter(new FileOutputStream(fic));
				String st = contenu.getText();
				if (st.length() != 0) {
					pr.write(contenu.getText());
					console.setText("Sauvegarde effectuée");
					fileSaved = true;
				}
				pr.close();
			} else {
				console.setText("Aucun fichier selectionné");
			}
		} catch (IOException e) {
			console.setText(e.toString());
		}
	}

	public void saveAs() {
		String fichier = "";
		fDial.setVisible(true);
		fichier = fDial.getDirectory() + "/" + fDial.getFile();
		saveText(fichier);
	}

	public static void main(String args[]) {
		CliFTP cli = new CliFTP();
		cli.setTitle("FTP");
		cli.setVisible(true);
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		sendToServ("check " + liste.getSelectedItem());
		if (isFile() == true) {
			console.setText(liste.getSelectedItem() + " est un fichier");
			sendToServ("get " + liste.getSelectedItem());
			readFromServFile();
			readPwd(true, liste.getSelectedItem());

		} else {
			console.setText(liste.getSelectedItem() + " est un répertoire");
			sendToServ("cd " + liste.getSelectedItem());
			readFromServ();
			readPwd(false, "");
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getActionCommand()) {
		case "Connect":
			Connect();
			break;
		case "Disconnect":
			if (fileSaved == true) {
				fileSaved = false;
				Disconnect();
			} else {
				Boolean rep = MsgBox.affQuest(this, "Vous allez vous deconnecter sans sauvegarder");
				if (rep == true) {
					Disconnect();
				}
			}
			break;
		case "Quitter":
			if (fileSaved == true) {
				fileSaved = false;
				Disconnect();
				System.exit(0);
			} else {
				Boolean rep = MsgBox.affQuest(this, "Vous allez quitter sans sauvegarder");
				if (rep == true) {
					Disconnect();
					System.exit(0);
				}
			}
			break;
		case "Save":
			Boolean rep = MsgBox.affQuest(this, "Etes vous sûr de vouloir sauvegarder?");
			if (rep == true) {
				saveText("./" + liste.getSelectedItem());
			}
			break;
		case "Save as":
			saveAs();
			break;
		}
	}
}
