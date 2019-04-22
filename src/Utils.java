import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Utils {

	public static void readFile(List<Movie> movies, String inputFileName) {
		// this method reads the file that consisted of the moves in a text file and put
		// the movies with their details in a list of movies
		try (Scanner input = new Scanner(Paths.get("./src/" + inputFileName))) {
			while (input.hasNext()) {
				ArrayList<Actor> cast = new ArrayList<>();

				String line = input.nextLine();

				int endOfName = line.indexOf("(");
				int startOfYear = endOfName + 1;
				int endOfYear = endOfName + 5;

				String nameOfMovie = line.substring(0, endOfName);
				int releaseYear = Integer.parseInt(line.substring(startOfYear, endOfYear));
				String ActorsFullNames = line.substring(endOfYear + 1);

				StringTokenizer token = new StringTokenizer(ActorsFullNames, "/"); // split the names of actors
				while (token.hasMoreTokens()) {
					String fullName = token.nextToken();
					int commaIndex = fullName.indexOf(","); // no comma means the name is consisted of first name only
					String Name, SurName;
					if (commaIndex > 0) {
						SurName = fullName.substring(0, commaIndex).trim();
						Name = fullName.substring(commaIndex + 1).trim();
					} else {
						Name = fullName;
						SurName = " ";
					}
					cast.add(new Actor(Name, SurName));
				}

				movies.add(new Movie(nameOfMovie, releaseYear, cast));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String actor1, actor2;

	static List<Movie> Question1(List<Movie> movies) {

		System.out.println("Enter the name and surname of the actor separated by comma (without a space): ");
		Scanner scan = new Scanner(System.in);
		String inputSring = scan.nextLine();
		while (inputSring.indexOf(",") < 0) {
			System.out.println("the name and surname of the actor should be separated by comma");
			inputSring = scan.nextLine();
		}
		actor1 = inputSring.substring(0, inputSring.indexOf(","));
		actor2 = inputSring.substring(inputSring.indexOf(",") + 1);

		Predicate<Movie> actor1exist = movie -> movie.hasActor(actor1);
		Predicate<Movie> actor2exist = movie -> movie.hasActor(actor2);
		Comparator<Movie> ByName = Comparator.comparing(Movie::getName);

		// filter the movies get a list of the movies that had co-starred in the same
		// movie
		List<Movie> commonMovies = movies.stream().filter(actor1exist.and(actor2exist)).sorted(ByName)
				.collect(Collectors.toList());

		return commonMovies;

	}

	static List<Movie> Question2(List<Movie> movies) {

		System.out.println("Enter the first character and ordering type : ");
		Scanner scan = new Scanner(System.in);
		String inputSring = scan.nextLine().trim();

		// check if the input is correct, otherwise re ask the user to enter the
		// character and ordering type again
		while (!(inputSring.substring(1).trim().equalsIgnoreCase("ascending")
				|| inputSring.substring(1).trim().equalsIgnoreCase("descending"))
				|| !(Character.isLetter(inputSring.charAt(0)))) {
			System.out.println("Enter the charater then 'Ascending' or 'Descending' ");
			inputSring = scan.nextLine().trim();
		}

		char character = inputSring.charAt(0);
		String type = inputSring.substring(1).trim();

		Comparator<String> orderType = type.equalsIgnoreCase("descending") ? Comparator.reverseOrder()
				: Comparator.naturalOrder();
		Comparator<Movie> alphabetically = Comparator.comparing(Movie::getName, orderType);
		Predicate<Movie> StartLetter = movie -> movie.getName().charAt(0) == character;

		// filter the movies according to the inserted letter and sort the list
		// alphabetically
		List<Movie> sortedList = movies.stream().filter(StartLetter).sorted(alphabetically)
				.collect(Collectors.toList());

		return sortedList;

	}

	static void Question3(List<Movie> movies) {

		System.out.println("Search movies by first name, please enter the actor’s first name: ");
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine().trim();

		while (input.isEmpty()) {
			System.out.println("please enter the actor’s first name: ");
			input = scan.nextLine().trim();
		}
		String actorFirstName = new String(input);

		Predicate<Movie> firstNameMatch = movie -> movie.hasActorWithFirstName(actorFirstName);
		Comparator<Movie> byReleaseYear = Comparator.comparing(Movie::getReleaseYear, Comparator.reverseOrder());

		// filter the movies that have an actor with a first name same as the entered
		// name
		List<Movie> ListByFirstName = movies.stream().filter(firstNameMatch).collect(Collectors.toList());

		System.out.printf("Movies played by actors with first name “%s”%n", actorFirstName);
		System.out.println("Actor’s Name/Surname    Movie(s) Title(s)");
		System.out.print("--------------------    -------------------");

		// result map is a map that has the actos's name as the key and the value is all
		// the movies that the actor has played in
		Map<String, List<Movie>> result = new HashMap<>();

		// fill the result map with actors alongside the movies they have played inF
		ListByFirstName.forEach((Movie movie) -> {
			movie.getActorByFirstName(actorFirstName).forEach(x -> {
				if (result.containsKey(x)) {
					List<Movie> arr = result.get(x);
					arr.add(movie);
					result.put(x, arr.stream().sorted(byReleaseYear).collect(Collectors.toList()));
				} else {
					List<Movie> arr = new ArrayList<>();
					arr.add(movie);
					result.put(x, arr);
				}
			});
		});

		// since the tree map sorts the key automatically, putting 'result' map as an
		// argument in the tree map results a tree map that has sorted keys linked to
		// their values
		new TreeMap<>(result).forEach((key, Value) -> {
			System.out.printf("%n%-25s", key);
			Value.forEach((Movie m) -> {
				System.out.print(m.toString() + ", ");
			});
		});
	}

	static void Question4(List<Movie> movies) {

		System.out.println(
				"Search movies by release date. \nPlease enter the start year and end year of the period you want to search for separated by a space: ");

		Scanner scan = new Scanner(System.in);
		String inputSring;

		int num1 = 0, num2 = 0;
		while (num1 == 0 || num2 == 0) {
			try {
				inputSring = scan.nextLine().trim();
				num1 = Integer.parseInt(inputSring.substring(0, inputSring.indexOf(" ")));
				num2 = Integer.parseInt(inputSring.substring(inputSring.indexOf(" ") + 1));
			} catch (Exception e) {
				System.out.println(
						"Please enter the start year and end year of the period you want to search for separated by a space: \"");
			}
		}

		int smaller = num1, bigger = num2;

		Predicate<Movie> startYear = movie -> movie.getReleaseYear() >= smaller;
		Predicate<Movie> endtYear = movie -> movie.getReleaseYear() <= bigger;
		Predicate<Movie> period = startYear.and(endtYear);
		Comparator<Movie> byReleasedYear = Comparator.comparing(Movie::getReleaseYear);

		// list of movies that were released between the entered years inclusive
		List<Movie> moviesSortedByYear = movies.stream().filter(period).sorted(byReleasedYear)
				.collect(Collectors.toList());

		if (moviesSortedByYear.size() > 0) {
			System.out.printf("Movies released between %d – %d%n", smaller, bigger);
			moviesSortedByYear.forEach((Movie movie) -> {
				System.out.printf("%-25s (%d)%n", movie.getName(), movie.getReleaseYear());
			});
		} else {
			System.out.printf("no Movies were released between %d – %d%n", smaller, bigger);
		}
	}

	static void Question5(List<Movie> movies) {

		// this map maps every actor with how many movies he has played in
		Map<String, Integer> actorsMoviesCount = new HashMap<>();

		// link every actor with the number of movies he/she has played in
		movies.stream().map((Movie movie) -> {
			return movie.getCast();
		}).collect(Collectors.toList()).forEach((List<Actor> actorList) -> {
			actorList.forEach((Actor actor) -> {
				if (actorsMoviesCount.containsKey(actor.getFullName())) {
					int num = actorsMoviesCount.get(actor.getFullName());
					actorsMoviesCount.put(actor.getFullName(), num + 1);
				} else {
					actorsMoviesCount.put(actor.getFullName(), 1);
				}
			});
		});

		// Getting the name of the actor that has played in movies more than any other
		// actor
		String winnerName = actorsMoviesCount.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.max((actor1, actor2) -> actor1.getValue() > actor2.getValue() ? 1 : -1).get().getKey();
		int NumberOfTimes = actorsMoviesCount.get(winnerName);

		System.out.printf("The actor with the maximum number of movies played in is %s who played in %d movies.%n",
				winnerName, NumberOfTimes);

		// list of movies that has been played by the actor that has played in movies
		// more than any other actor
		List<Movie> moviesOfWinner = movies.stream().filter((Movie movie) -> movie.hasActor(winnerName))
				.collect(Collectors.toList());

		// this Map maps each year (key) with how many movies the actor played in
		Map<Integer, Integer> yearMoviesCount = new HashMap<>();

		// filling yearMoviesCount Map
		moviesOfWinner.forEach((Movie movie) -> {
			int year = movie.getReleaseYear();
			if (yearMoviesCount.containsKey(year)) {
				int count = yearMoviesCount.get(year);
				yearMoviesCount.put(year, count + 1);
			} else {
				yearMoviesCount.put(year, 1);
			}
		});

		Map<Integer, List<Integer>> resultedMap = yearMoviesCount.entrySet().stream().collect(
				Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

		// getting the maximum number of movies (key) that were played in the same year
		int productiveCountKey = resultedMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.max((entry1, entry2) -> entry1.getKey() > entry2.getKey() ? 1 : -1).get().getKey();

		// using productiveCountKey (key), we are getting the list of the most
		// productive years
		String str = resultedMap.get(productiveCountKey).toString().substring(1,
				resultedMap.get(productiveCountKey).toString().length() - 1);

		// printing the results
		if (productiveCountKey == 1) {
			System.out.printf("%s were his/her most productive year with %d movie .%n", str, productiveCountKey);
		} else {
			System.out.printf("%s were his/her most productive year(s) with %d movies in each.%n", str,
					productiveCountKey);
		}
	}

	public static void printMoviesQuestion1(List<Movie> movies) {
		int numOfCommonMovies = movies.size();

		// print the final result
		if (numOfCommonMovies > 0) {
			if (numOfCommonMovies == 1) {
				System.out.printf("‘%s’ and ‘%s’ had co-starred in one movie :%n", actor1, actor2);
			} else {
				System.out.printf("‘%s’ and ‘%s’ had co-starred in more than one movie :%n", actor1, actor2);
			}
			movies.forEach((Movie m) -> {
				System.out.println(m.getName());
			});
		} else {
			System.out.printf("‘%s’ and ‘%s’ had not co-starred in any movie.%n", actor1, actor2);
		}
	}

	public static void printMoviesQuestion2(List<Movie> movies) {
		movies.forEach((Movie movie) -> {
			System.out.println(movie.getName());
		});
	}
}
