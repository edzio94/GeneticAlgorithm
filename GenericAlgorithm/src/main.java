import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Algorytm genetyczny który układa optymalne równanie
// dla liczby danej przez uzytkownika

// UWAGA: Dla różnych liczb liczba potomstwa może wyjść różna (od 15 defaultowo do ponad tysiąca 
// jak i w nieskończoność)


public class main {

	public static void main(String[] args) throws IOException {
		int counter = 0;
		int value = 19;
		boolean end = false;
		float totalFitness = 0.f;

		List<Phenotype> phenotypes = new ArrayList<Phenotype>();

		for (int i = 0; i < 15; i++) {
			System.out.println("I = " + i);
			phenotypes.add(new Phenotype(i, value));

		}
		counter = 15;
//Zatrzymanie gdy mamy nasze równanie lub potomstwo osiągneło liczbe 1000
		while (!end && phenotypes.size () < 1000) {
		
			for (int i = 0; i < phenotypes.size(); i++) {
				System.out.println("=============");
				System.out.println("ID = " + phenotypes.get(i).ID);
				phenotypes.get(i).showSymbol();
				phenotypes.get(i).getNormalEquation();
				System.out.println(phenotypes.get(i).result);
				phenotypes.get(i).getFitnessRate();
			
				System.out.println("Równanie: "+phenotypes.get(i).normalEquation);
				
				//System.in.read();
			}
			//Suma wszystkich fitnessRate fenotypów
			for (Phenotype x : phenotypes) {
				totalFitness += x.fitnessRate;
			}
			//Losowanie 2 phenotypów
			Phenotype phen1 = roullette(phenotypes, totalFitness);
			Phenotype phen2 = roullette(phenotypes, totalFitness);
			// Dodanie nowego genotypa i zapisanie mu genów z mutacji 2 wybranych fenotypów.
			phenotypes.add(new Phenotype(counter, value));
			swapsGenes(phenotypes.get(counter).genes, phen1.genes, phen2.genes);
			counter++;
			for (Phenotype x : phenotypes) {
				//Sprawdzamy, czy warunek został spełniony (równanie == liczbie podanej przez użytkownika)
				if (x.fitnessRate == 999.0f) {
					System.out.println("Zrobione dla " + phenotypes.size()
							+ " potomkow");
					System.out.println("Równanie = " + x.normalEquation);
					System.in.read();
					end = true;
					break;
				}
			}

		}
		if (!end){
			System.out.println("Przekroczono limit 1000 potomków.");
			System.out.println("Niestety nie znaleziono danego równania");
				}
	}

	//Funkcja do swapowania 2 wybranych genów;
	private static void swapsGenes(int swap[], int[] p1, int[] p2) {
		int tab[] = p1;

		for (int i = 0; i < p1.length; i++) {
			Random generator = new Random();
			float tmp = (float) generator.nextInt(10) / 10;
			if (tmp == 0.7) {
				for (int j = i; j < p1.length; j++) {
					tab[j] = p2[j];
				}
			}
			swap = tab;
		}
	}

	// Funckja ,która zwraca Phenotym wylosowany poprzez losowanie ruletki.
	private static Phenotype roullette(List<Phenotype> p, float TF) {
		Phenotype ret = null;
		Boolean done = false;
		Random gen = new Random();
		while (!done) {
			float Slice = (float) gen.nextInt() * TF;
			float fitnessSoFar = 0.0f;
			for (int i = 0; i < p.size(); i++) {
				fitnessSoFar += p.get(i).fitnessRate;
				if (fitnessSoFar >= Slice) {
					ret = p.get(i);
					done = true;
					break;
				}
			}

		}
		return ret;
	}

}
