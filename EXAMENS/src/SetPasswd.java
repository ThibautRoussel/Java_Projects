import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.JPasswordField;

public class SetPasswd extends Frame implements ActionListener {
	private static final long serialVersionUID = 1L;
	Button but;
	Panel p1, p2, pmain;
	TextField nom;
	JPasswordField mdp, nmdp, nmdp2;
	PrintWriter pr;
	BufferedReader br;

	public SetPasswd() {
		addWindowListener(new Fermeture());
		setTitle("Changer un mot de passe");
		setLayout(new BorderLayout());
		setSize(600, 300);

		but = new Button("Lancer la modification");
		but.addActionListener(this);
		add("South", but);

		Panel p1 = new Panel();
		p1.setLayout(new GridLayout(4, 1));
		Panel p2 = new Panel();
		p2.setLayout(new GridLayout(4, 1));
		Panel pmain = new Panel();
		pmain.setLayout(new GridLayout(1, 2));

		nom = new TextField();
		mdp = new JPasswordField();
		nmdp = new JPasswordField();
		nmdp2 = new JPasswordField();

		p1.add(nom);
		p1.add(mdp);
		p1.add(nmdp);
		p1.add(nmdp2);

		p2.add(new Label("Nom :"));
		p2.add(new Label("Mot de passe :"));
		p2.add(new Label("Nouveau mot de passe :"));
		p2.add(new Label("Retapez le mot de passe :"));

		pmain.add(p2);
		pmain.add(p1);
		add("Center", pmain);

	}

	public boolean veriFier(String nom, String pass) {
		boolean flag = false;
		try {
			String line = "";
			br = new BufferedReader(new InputStreamReader(new FileInputStream("passwd.txt")));
			while (null != (line = br.readLine())) { // on lit la ligne
				if (line.startsWith(nom + ":" + pass)) {
					flag = true;
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return flag;
	}

	public boolean estIdentique(String st, String st2) {
		boolean flag = false;
		if (st.equals(st2)) {
			flag = true;
		}
		return flag;
	}

	public void remPlace(String ligne1, String ligne2) {
		StringTokenizer st = new StringTokenizer(ligne1, ":", false);
		if (veriFier(st.nextToken(), st.nextToken())) {

			try {
				String line = "";
				br = new BufferedReader(new InputStreamReader(new FileInputStream("passwd.txt")));
				Vector<String> v = new Vector<String>();
				while (null != (line = br.readLine())) { // on lit la ligne
					if (!line.startsWith(ligne1)) {
						v.addElement(line);
					}
				}
				br.close();
				v.addElement(ligne2);
				pr = new PrintWriter(new FileOutputStream("passwd.txt"));
				for (int i = 0; i < v.size(); i++) {
					pr.println(v.get(i).toString());
				}
				pr.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		} else {
			System.out.println("Cette paire utilisateur/mot de passe n'existe pas");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SetPasswd spw = new SetPasswd();
		spw.setVisible(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String st1 = nom.getText();
		String st2 = mdp.getText();
		String st3 = nmdp.getText();
		String st4 = nmdp2.getText();
		if (st1.isEmpty() || st2.isEmpty() || st3.isEmpty() || st4.isEmpty()) {
			System.out.println("Veuillez remplir tous les champs");
		} else {

			if (veriFier(st1, st2)) {
				if (!estIdentique(st2, st3)) {
					if (estIdentique(st3, st4)) {
						remPlace(st1 + ":" + st2, st1 + ":" + st3);
					} else {
						System.out.println("Les deux mots de passe entrés ne sont pas identiques");
					}
				} else {
					System.out.println("Veuillez saisir un nouveau mot de passe différent de l'ancien");
				}

			} else {
				System.out.println("Cette paire utilisateur/mot de passe n'existe pas");
			}

		}

	}
}
