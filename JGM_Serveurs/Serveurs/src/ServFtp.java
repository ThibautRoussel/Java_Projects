import java.io.*;
import java.net.*;

public class ServFtp {
	static int port;

	public static void main(String args[]) {
		ServerSocket serveur;
//définition du port
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			port = 1235; // valeur par défaut
		}
//installation
		try {
			serveur = new ServerSocket(port);
			System.out.println("Serveur FTP en cours sur port : " + port);
			while (true) {
//création de nouvelles connexions
				Socket s = serveur.accept();
				new My_FTP(s); //definition de service a rendre au client
			}
		} catch (IOException e) {
			System.out.println("Erreur à la creation d'un objet Socket : " + e.getMessage());
			System.exit(1);
		}
	}
}
