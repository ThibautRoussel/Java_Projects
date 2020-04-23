import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.StringTokenizer;

public class NBOcc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 2) {
			System.out.println("Erreur du nombre d'arguments");
			System.exit(0);
		}
		String arg0 = args[0];
		String arg1 = args[1];
		String line = "";
		String token = "";
		int nb = 0;
		try {
			StringTokenizer st;
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arg0)));
			while (null != (line = br.readLine())) { // on lit la ligne
				//System.out.println(line);
				st = new StringTokenizer(line, " ", false); // on la parse en fonction des espaces
				do { //tant que la ligne n'est pas vide
					token = st.nextToken();
					//System.out.println(token);					
					if (token.contains(arg1)) { //si l'argument correpondant au mot cherché est présent dans le token
						nb++; // on incrémente le nombre total
					}
				}while (!token.isEmpty());
			}

			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		// On répond
		System.out.println("Le mot « " + arg1 + " » figure " + nb + " fois dans le fichier « " + arg0 + " »");
	}


}
