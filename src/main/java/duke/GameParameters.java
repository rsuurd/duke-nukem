package duke;

import java.util.Arrays;
import java.util.List;

public record GameParameters(boolean asp) {
    public static GameParameters parse(String... parameters) {
        List<String> parametersList = Arrays.stream(parameters).map(String::trim).toList();

        boolean asp = parametersList.stream().anyMatch(ASP::equalsIgnoreCase);

        return new GameParameters(asp);
    }

    private static final String ASP = "asp";
}
