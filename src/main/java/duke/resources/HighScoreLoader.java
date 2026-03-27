package duke.resources;

import duke.DukeNukemException;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighScoreLoader {
    private Path path;

    public HighScoreLoader(Path path) {
        this.path = path;
    }

    public List<HighScore> load() {
        try {
            return Files.readAllLines(path.resolve("HIGHS.DN1")).stream().map(line -> {
                Matcher matcher = SCORE.matcher(line);

                if (!matcher.matches()) {
                    throw new DukeNukemException(String.format("Invalid high score entry: %s", line));
                }

                int score = new BigInteger(matcher.group("score")).min(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
                String name = matcher.group("name").trim();

                return new HighScore(name, score);
            }).limit(10).toList();
        } catch (IOException e) {
            return DEFAULT_SCORES;
        }
    }

    public void save(List<HighScore> highScores) {
        try {
            List<String> lines = highScores.stream()
                    .map(entry -> String.format("%d%s", entry.score(), entry.name()))
                    .limit(10)
                    .toList();

            Files.write(path.resolve("HIGHS.DN1"), lines);
        } catch (IOException e) {
            throw new DukeNukemException("Could not write high scores", e);
        }
    }

    // TODO duke.score?
    public record HighScore(String name, int score) {
    }

    private static final Pattern SCORE = Pattern.compile("(?<score>\\d+)(?<name>.*)");

    static final List<HighScore> DEFAULT_SCORES = List.of(
            new HighScore("TODD", 40000),
            new HighScore("SCOTT", 30000),
            new HighScore("GEORGE", 20000),
            new HighScore("AL", 10000),
            new HighScore("JOHN", 500),
            new HighScore("ROLF", 100),
            new HighScore("", 0),
            new HighScore("", 0),
            new HighScore("", 0),
            new HighScore("", 0)
    );
}
