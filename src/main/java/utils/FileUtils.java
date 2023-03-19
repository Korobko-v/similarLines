package utils;

import exceptions.FileFormatException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Методы для работы с файлами
 */
public class FileUtils {

    /**
     * Получить данные из файла
     *
     * @param fileName имя файла
     * @return списки строк из файла
     */
    public static List<List<String>> readFromFile(String fileName) {
        try {
            return validateAndSeparateStringsList(Files.lines(Path.of(fileName))
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            throw new RuntimeException("Файл не существует");
        }
    }

    /**
     * Запись в файл
     * @param fileName имя файла
     * @param stringPairs пары похожих строк
     * @return "SUCCESS", если файл записан удачно, иначе ошибка
     */
    public static String writeToFile(String fileName, Map<String, String> stringPairs) {
        try (FileWriter writer = new FileWriter(fileName)) {
            stringPairs.keySet().forEach(key ->
            {
                try {
                    writer.write(key + ":" + stringPairs.get(key) + "\n");
                } catch (IOException e) {
                    throw new FileFormatException("Failed to write data to file");
                }
            });
        } catch (IOException e) {
            throw new FileFormatException("Failed to write data to file");
        }
        return "SUCCESS";
    }

    /**
     * Проверка содержимого файла на заданный формат (n - число строк, далее - n строк)
     * @param strings список строк
     * @return два списка для дальнейшего формирования пар строк
     */
    public static List<List<String>> validateAndSeparateStringsList(List<String> strings) {
        try {
            int n = Integer.parseInt(strings.get(0));
            int m = Integer.parseInt(strings.get(n + 1));
            if (strings.size() != n + m + 2) {
                throw new FileFormatException("Количество строк не соответствует указанному в файле числу");
            }
            return Arrays.asList(strings.subList(1, n + 1), strings.subList(n + 2, strings.size()));
        } catch (NumberFormatException e) {
            throw new FileFormatException("Файл не соответствует заданному формату");
        }

    }
}
