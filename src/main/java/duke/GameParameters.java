package duke;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record GameParameters(Path path, boolean asp) {
    public static GameParameters parse(String... parameters) {
        List<String> parametersList = Arrays.stream(parameters).map(String::trim).toList();

        Path path = parsePath(parametersList, () -> Paths.get(".dn1"));
        boolean asp = parametersList.stream().anyMatch(ASP::equalsIgnoreCase);

        return new GameParameters(path, asp);
    }

    private static Path parsePath(List<String> parametersList, Supplier<Path> defaultPath) {
        return Stream.of(PATH, PATH_SHORT)
                .map(prefix -> parsePath(parametersList, prefix))
                .filter(Objects::nonNull)
                .findAny()
                .orElseGet(defaultPath);
    }

    private static Path parsePath(List<String> parametersList, String prefix) {
        return parametersList.stream().filter(parameter -> parameter.toLowerCase().startsWith(prefix)).findAny().map(parameter ->
                Paths.get(parameter.substring(parameter.indexOf('=') + 1).trim())
        ).orElse(null);
    }

    private static final String ASP = "asp";
    private static final String PATH = "--path";
    private static final String PATH_SHORT = "-p";
}
