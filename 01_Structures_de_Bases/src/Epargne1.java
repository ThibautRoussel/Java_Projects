
public class Epargne1 {
	
public static void main(String args[]) {
	
	double capital=10000;
	double taux=1.045;
//traduire l'argument en entier
	
	int n=Integer.parseInt(args[0]);
	
//boucle pour r�p�ter n fois le calcul
	
	for (int i=1; i<=n; i++)
	{
		capital=capital*taux;
	}
	
	System.out.print("Apr�s "+n+" ann�es, le capital vaut ");
	
	System.out.println((int)capital+" F.");
	}

}
