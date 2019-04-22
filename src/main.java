import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class main {

	final static String inputFileName = "movies_full.txt";
	final static String sample = "movies_sample.txt";

	public static void main(String[] args) {

		List<Movie> movies = new ArrayList<>();
		Utils.readFile(movies, inputFileName);

		System.out.println("\n-----------*Q1*-----------\n");
		List<Movie> moviesQuestion1 = Utils.Question1(movies);
		Utils.printMoviesQuestion1(moviesQuestion1);

		System.out.println("\n-----------*Q2*-----------\n");
		List<Movie> moviesQuestion2 = Utils.Question2(movies);
		Utils.printMoviesQuestion2(moviesQuestion2);

		System.out.println("\n-----------*Q3*-----------\n");
		Utils.Question3(movies);

		System.out.println("\n-----------*Q4*-----------\n");
		Utils.Question4(movies);

		System.out.println("\n-----------*Q5*-----------\n");
		Utils.Question5(movies);

		System.out.println("\n-----------End-----------\n");
	}

}
