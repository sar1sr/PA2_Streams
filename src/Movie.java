import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Movie {

	private String name;
	private int releaseYear;
	private List<Actor> cast;

	public Movie(String name, int releaseYear, List<Actor> cast) {
		super();
		this.name = name;
		this.releaseYear = releaseYear;
		this.cast = cast;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

	public List<Actor> getCast() {
		return cast;
	}

	@Override
	public String toString() {
		return name + "  (" + releaseYear + ")";
	}

	public void setCast(List<Actor> cast) {
		this.cast = cast;
	}

	public boolean hasActor(Actor actor) {
		String name = actor.getFullName();
		return cast.stream().map(Actor::getFullName).anyMatch(e -> e.equalsIgnoreCase(name));
	}

	public boolean hasActor(String ActorFullName) {
		return cast.stream().map(Actor::getFullName).anyMatch(e -> e.equalsIgnoreCase(ActorFullName));
	}

	public boolean hasActorWithFirstName(String firstName) {
		return cast.stream().map(Actor::getName).anyMatch(e -> e.equalsIgnoreCase(firstName));
	}

	public List<String> getActorByFirstName(String firstName) {
		return cast.stream().filter((Actor e) -> e.getName().equalsIgnoreCase(firstName))
				.sorted(Comparator.comparing(Actor::getSurName)).map(Actor::getFullName).collect(Collectors.toList());
	}

}
