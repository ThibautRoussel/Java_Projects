import java.io.*;
import java.net.*;

public class ServTel {
	
	static int port;

	public static void main(String args[]) {
		
		ServerSocket serveur;
		
//d�finition du port
		try {
			port = Integer.parseInt(args[0]);
			
		} catch (Exception e) {
			
			port = 1236; // valeur par d�faut
		}
//installation
		try {
			serveur = new ServerSocket(port);
			System.out.println(" Serveur Heure en cours sur le port "  + port );
			while (true) {
//cr�ation de nouvelles connexions
				Socket s = serveur.accept();
				new My_Tel(s);   // D�finition du service � rendre au client
			}
			
		} catch (IOException e) {
			System.out.println("Erreur � la creation d'un objet Socket : " + e.getMessage());
			System.exit(1);
		}
	}
}
