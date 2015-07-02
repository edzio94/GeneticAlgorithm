import java.awt.List;
import java.io.IOException;
import java.util.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Phenotype {

	public int genes[];
	public int number;
	int ID;
	float fitnessRate;
	String equation;
	public StringBuilder normalEquation;
	float result;
	boolean elite;
	Queue<Character> operators;
	ArrayList<Integer> numbers;
	Stack<Character> stack;
	ScriptEngineManager manager;
	ScriptEngine engine;

	Phenotype(int id, int number1) {
		this.ID = id;
		this.number = number1;
		normalEquation = new StringBuilder();
		result = 0.0f;
		manager = new ScriptEngineManager();
		engine = manager.getEngineByName("js");
		genes = new int[32];
		numbers = new ArrayList<Integer>();
		operators = new LinkedList<Character>();
		stack = new Stack<>();
		equation = new String();
		generateGenes();
		getSymbol();

	}

	//Losujemy geny [0,1]
	private void generateGenes() {
		Random generator = new Random();
		for (int i = 0; i < genes.length; i++) {
			genes[i] = generator.nextInt(2);
		}

	}
	public void showSymbol()
	{
		System.out.print("liczby: ");
		for (int x : numbers)
			System.out.print(x+",");
		System.out.println();
		System.out.print("Znaki: ");
		for (char x : operators)
		{
			System.out.print(x);
		}
	}

	
	public float showResult() {
		return result;
	}

	//Sprawdzamy dopasowanie do naszej liczby
	public void getFitnessRate() {
		float tmp = (float) ((float) number - result);
		if (number - result == 0.0f) {
			fitnessRate = 999.0f;

		} else
			fitnessRate = (float) 1 / (number - result);
	}

	public void showGenes() {
		for (int i : genes) {
			System.out.println(i);
		}
	}

	// Geny dzielimy po 4 i układamy z nich równanie
	private void getSymbol() {
		for (int i = 0; i < genes.length / 4; i++) {
			String tmp = new String();
			for (int j = 0; j < 4; j++) {
				//Dodawanie kolejnych genów do Stringa
				tmp += genes[i * 4 + j];
			}
			//Zwracanie odpowiedniej liczby lub symbolu do Stringa
			String kkk = returnSymbol(tmp);
			// Dodawanie do równania
			equation += kkk;

		}		
		sortSymbols();
	}

	//Sprawdzanie czy na odpowiednich genach są liczby i operatory i dodawanie ich do listy i stosu
	private void sortSymbols() {
		int i = 0;
		for (char ch : equation.toCharArray()) {
			if (Character.isDigit(ch) && (i % 2 == 0)) {
				System.out.println("Dodaje liczbe "+Character.getNumericValue(ch));
				numbers.add(Character.getNumericValue(ch));
			} else if ((i % 2 != 0) && (!Character.isDigit(ch)) && (ch != 'X')) {
				operators.add(ch);
			}
			i++;
		}
		System.out.println();

	}


	// z wyciagniętych genów zrób równanie liczba->operator->liczba->operator...
	public void getNormalEquation() throws IOException {
		System.out.println();
		//Sprawdzanie czy można zrobić działanie
		if (numbers.size() >= 2 && operators.size() >= 1) {
			System.out.println("SIZE = "+numbers.size());
			boolean op = false;
			int i = 0;
			int counter = 1;
			normalEquation = new StringBuilder();
			//i + 1
			while ((i < numbers.size()) && (!operators.isEmpty())) {
			
				//Dodawanie do równania liczby->operatora->liczby....
				normalEquation.append(numbers.get(i++));
				
				if (i < numbers.size())
				normalEquation.append(operators.remove());
				/*if (i+2 >= numbers.size()) {
					System.out.println(" i < numbers.size()");
					normalEquation.append(operators.remove());
				}*/
				if (operators.isEmpty()) {
					normalEquation.append(numbers.get(i++));
					break;
				}
			}
			//Zamiana dzielenia przez zero na dzielenie przez 1
			String tmp = normalEquation.toString();
			if (tmp.contains("/0")) {
				String xxx = tmp.replaceAll("/0", "/1");

				normalEquation = new StringBuilder(xxx);
			}
			try {
				System.out.println("Jeden wielki test: " + normalEquation);
				Object result1 = engine.eval(normalEquation.toString());
				float foo = Float.parseFloat(result1.toString());
				result = foo;
			} catch (Exception e) {
				System.out.println("ZLE DZIALANIE !!!");
				System.in.read();

			}
		//Jeśli nie wylosowało operatorów to wybiera pierwszą liczbę wylosowaną
		} else if (numbers.size() > 1) {
			result = numbers.get(0);
		} else { // Losowanie jeszcze raz gdy nie wylosowało żadnej liczby.
			generateGenes();
			getSymbol();
			getNormalEquation();
		}
	}
	
	//Nadanie odpowiednich znaków dla genów

	private String returnSymbol(String character) {

		if (character.equals("0000"))
			return "0";
		else if (character.equals("0001"))
			return "1";
		else if (character.equals("0010"))
			return "2";
		else if (character.equals("0011"))
			return "3";
		else if (character.equals("0100"))
			return "4";
		else if (character.equals("0101"))
			return "5";
		else if (character.equals("0110"))
			return "6";
		else if (character.equals("0111"))
			return "7";
		else if (character.equals("1000"))
			return "8";
		else if (character.equals("1001"))
			return "9";
		else if (character.equals("1010"))
			return "+";
		else if (character.equals("1011"))
			return "-";
		else if (character.equals("1100"))
			return "*";
		else if (character.equals("1101"))
			return "/";

		return "X";
	}
}
