
import java.applet.*;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

 


public class Glossaire extends Applet implements ItemListener {
    
    // fichier de donn�es
    String nomFichier = "glossaire.txt";
    // TextArea avec WordWrap
    TextArea zoneTexte = new TextArea("", 10, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
    // Liste des mots du glossaire
    List liste = new List();
    // Structure de stockage des donn�es
    Vector glo = new Vector();

 
    public void init() {
        //positionnement des composants
        setLayout(new BorderLayout(4,4));
        add("Center",zoneTexte);
        zoneTexte.setEditable(false);
        zoneTexte.setBackground(Color.white);
        add("West",liste);
        //propri�t�s de la liste
        liste.addItemListener(this);
        liste.setMultipleMode(false);
        liste.setBackground(Color.white);
    }

 

    public void start() {
        // lecture du fichier
        try {
            URL url = new URL(getCodeBase(), nomFichier);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String ligne;
            while (null != (ligne = br.readLine()))
                if (ligne.indexOf('=') > 0)
                    glo.addElement(ligne);
            br.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        // remplissage de la liste
        for (int i = 0; i < glo.size(); i++)
            liste.addItem(nom(i));
        // s�lection initiale
        liste.select(0);
        affiche(0);
        liste.requestFocus();
    }

 

    public String nom(int n) {
        // retrouver le n i�me mot de la liste
        if (n >= 0 && n < glo.size()) {
            String S = (String) glo.elementAt(n);
            int pos = S.indexOf('=');
            return S.substring(0, pos);
        } else
            return "";
    }

 

    public void affiche(int n) {
        // retrouver la n i�me d�finition
        if (n >= 0 && n < glo.size()) {
            String S = (String) glo.elementAt(n);
            int pos = S.indexOf('=');
            S = S.substring(pos + 1, S.length());
            // affichage, les # sont remplac�s par des retours chariots
            S = S.replace('#', '\n');
            zoneTexte.setText(S);
        }
    }

 

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==ItemEvent.SELECTED) {
            affiche(liste.getSelectedIndex());
        }
    }
}
