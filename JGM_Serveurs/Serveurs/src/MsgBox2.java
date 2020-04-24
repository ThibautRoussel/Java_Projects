import java.awt.*;
import java.awt.event.*;

class MsgBox2 extends Dialog implements ActionListener {
	boolean id = false; // permet de connaître le bouton utilisé
	Button ok, can;
	TextField nom, prenom, tel;
	Label l, l2;

	MsgBox2(Frame fr, String msg, boolean okcan, boolean mode, boolean b_tel) {
		// constructeur hérité
		super(fr, "Message", true);
		// gestionnaire de positionnement
		setLayout(new BorderLayout());

		// boutons
		Panel p = new Panel();
		p.setLayout(new FlowLayout());
		ok = new Button(" OK ");
		p.add(ok);
		ok.addActionListener(this);

		Panel p1 = new Panel();
		p1.setLayout(new GridLayout(1, 4));

		if (mode == true) { // mode prenom
			l = new Label("Prénom :");
			prenom = new TextField(10);
			prenom.setText(msg);
			p1.add(l);
			p1.add(prenom);
			if (b_tel == true) {
				l2 = new Label("Tel :");
				tel = new TextField(10);
				p1.add(l2);
				p1.add(tel);
			}
		} else {
			l = new Label("Nom :");
			nom = new TextField(10);
			nom.setText(msg);
			p1.add(l);
			p1.add(nom);

			l = new Label("Prénom :");
			prenom = new TextField(10);
			p1.add(l);
			p1.add(prenom);

			l2 = new Label("Tel :");
			tel = new TextField(10);
			p1.add(l2);
			p1.add(tel);
		}

		// ligne de message
		add(BorderLayout.CENTER, p1);

		if (okcan) {
			can = new Button("Annuler");
			p.add(can);
			can.addActionListener(this);
		}
		add(BorderLayout.SOUTH, p);
		// dimensions et positionnement
		pack();
		Dimension d = getToolkit().getScreenSize();
		setLocation(d.width / 3, d.height / 3);
		// affichage
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			id = true;
			setVisible(false);
		} else if (e.getSource() == can) {
			id = false;
			setVisible(false);
		}
	}

	public static String affPrenomMsg(Frame fr, String msg) {
		String rep = "";
		MsgBox2 message = new MsgBox2(fr, msg, false, true, false);
		if (message.prenom.getText().isEmpty()) {
			rep = "false";
		} else {
			rep = message.prenom.getText();
		}
		return rep;
	}

	public static String getNomPrenomTel(Frame fr, String msg) {
		String rep = "";
		MsgBox2 message = new MsgBox2(fr, msg, false, false, false);
		message.dispose();
		if (message.nom.getText().isEmpty() || message.tel.getText().isEmpty()) {
			rep = "false";
		} else {
			rep = message.nom.getText() + ":" + message.prenom.getText() + ":" + message.tel.getText();
		}
		return rep;
	}

	public static String getPrenomTel(Frame fr, String msg) {
		String rep = "";
		MsgBox2 message = new MsgBox2(fr, msg, false, true, true);
		message.dispose();
		if (message.prenom.getText().isEmpty() || message.tel.getText().isEmpty()) {
			rep = "false";
		} else {
			rep = message.prenom.getText() + ":" + message.tel.getText();
		}
		return rep;
	}

	public static boolean affQuest(Frame fr, String msg) {
		MsgBox2 message = new MsgBox2(fr, msg, true, false, false);
		boolean rep = message.id;
		message.dispose();
		return rep;
	}

}