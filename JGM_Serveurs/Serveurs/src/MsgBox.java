import java.awt.*;
import java.awt.event.*;

class MsgBox extends Dialog implements ActionListener {
	boolean id = false; // permet de connaître le bouton utilisé
	Button ok, can;

	MsgBox(Frame fr, String msg, boolean okcan) {
		// constructeur hérité
		super(fr, "Message", true);
		// gestionnaire de positionnement
		setLayout(new BorderLayout());
		// ligne de message
		add(BorderLayout.CENTER, new Label(msg, Label.CENTER));
		// boutons
		Panel p = new Panel();
		p.setLayout(new FlowLayout());
		ok = new Button(" OK ");
		p.add(ok);
		ok.addActionListener(this);
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

	public static void affMsg(Frame fr, String msg) {
		MsgBox message = new MsgBox(fr, msg, false);
		message.dispose();
	}

	public static boolean affQuest(Frame fr, String msg) {
		MsgBox message = new MsgBox(fr, msg, true);
		boolean rep = message.id;
		message.dispose();
		return rep;
	}
}