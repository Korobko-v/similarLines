import utils.FileUtils;
import utils.StringUtils;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<List<String>> file = FileUtils.readFromFile(args[0]);
        Map<String, String> stringPairs = StringUtils.getStringPairs(file);
        FileUtils.writeToFile("output.txt", stringPairs);
    }
}
