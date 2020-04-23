import java.util.*;

public class Calculette {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		// Tableau pour l'affichage des propositions
		String[] tab = { " 1. Somme de deux entiers", " 2. Soustraction de deux entiers",
				" 3. Multiplication de deux entiers", " 4. Division de deux entiers", " 5. Sortie du programme", "",
				"Veuillez entrer votre choix :" };

		// Boucle d'affichage
		while (true) {

			int a = 0;
			int b = 0;
			int rep = 0;
			boolean flag = true;
			String op ="";

			// Affiche les propositions
			for (int i = 0; i < tab.length; i++) {
				System.out.println(tab[i]);
			}

			String str = sc.nextLine(); // lecture console
			// System.out.println("Vous avez saisi : " + str); //Pour debug

			// verification de numero valide et de demande de sortie
			switch (Integer.parseInt(str)) { 
			case 5:
				sc.close();
				System.out.println("Exit");
				System.exit(0);
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				System.out.println("Entrez les deux entiers séparés par un espace");
				StringTokenizer st = new StringTokenizer(sc.nextLine());
				a = Integer.parseInt(st.nextToken());
				b = Integer.parseInt(st.nextToken());
				break;
			default:
				System.out.println("Le numéro entré n'est pas bon");
				flag = false;
				break;
			}

			// Application des oppérations
			switch (Integer.parseInt(str)) {
			case 1:
				rep = (a + b);
				op = "+";
				break;
			case 2:
				rep = (a - b);
				op = "-";
				break;
			case 3:
				rep = (a * b);
				op = "x";
				break;
			case 4:
				if (b != 0) {
					rep = (a / b);
					op = "/";
				} else {
					System.out.println("Division par 0 impossible");
					flag = false;
				}
				break;
			}
			
			//Affichage final
			if (flag == true) {
				System.out.println("La réponse est : " + a + op + b + "=" + rep + "\n");
			} else {
				System.out.println("La réponse est impossible \n");
			}
		}
	}
}
