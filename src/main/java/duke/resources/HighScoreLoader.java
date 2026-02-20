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
            }).toList();

        } catch (IOException e) {
            throw new DukeNukemException("Could not read high scores", e);
        }
    }

    // TODO duke.score?
    public record HighScore(String name, int score) {
    }

    private static final Pattern SCORE = Pattern.compile("(?<score>\\d+)(?<name>.*)");
}
