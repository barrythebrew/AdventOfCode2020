package adventOfCode;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static List<String> getFileContents(final String filename) {
        List<String> result;
        URL resource = Utils.class.getClassLoader().getResource(filename);
        try (Stream<String> lines = Files.lines(Paths.get(resource.toURI()))) {
            result = lines.collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

}
