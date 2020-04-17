
import java.awt.*;
import java.applet.*;

/** Applet affichant des carr�s dont la couleur, la position
 *  et la dimension est choisie au hasard.
 *  Il suffit de masquer le contenu de l'applet (par exemple
 *  en iconisant la fen�tre du navigateur) puis de la refaire
 *  apparaitre pour obtenir un nouveau dessin.
 *  Normal : chaque appel � la m�thode paint produit un nouveau
 *  dessin.
 */
public class Exo5 extends Applet {
 
 //nombre de carr�s � dessiner
 int nbCars;
    
 /**
  * Initialisation du nombre de carr�s et de la
  * couleur de fond
  */
 public void init() {
  nbCars=20;
  setBackground(Color.white);
 }
 
 /**
  * Tirer un nombre au hasard entre 0 et n (n exclus)
  */
 int hasard(int n) {
  Double D=new Double(Math.random()*n);
  return D.intValue();
  // ou
  // return (int)Math.floor(Math.random()*n); 
 }

 /**
  * Dessiner un carr� : couleur, position et taille sont
  * tir�s au hasard en utilisant la m�thode hasard.
  */
 void dessineCarre(Graphics g) {
  //choix de la couleur
  Color col=new Color(hasard(256),hasard(256),hasard(256));
  //maximum pour coordonn�es et c�t�
  int min=Math.min(getSize().width/2,getSize().height/2);
  //coordonn�es
  int x=hasard(min);
  int y=hasard(min);
  //c�t�
  int c=hasard(min);
  //on trace
  g.setColor(col);
  g.fillRect(x,y,c,c);
 }

 /**
  * La m�thode paint dessine nbCars carr�s en appelant la
  * la m�thode de dessin d'un carr� autant de fois que
  * n�cessaire.
  */
 public void paint(Graphics g)	{
  for (int i=0; i< nbCars ; i++) dessineCarre(g);
 }

}
	