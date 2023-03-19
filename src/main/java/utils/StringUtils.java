package utils;

import exceptions.StringListException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Методы для работы со строками
 */
public class StringUtils {

    public static Map<String, String> getStringPairs(List<List<String>> stringLists) {
        if (stringLists.size() != 2) {
            throw new StringListException("Количество списков для формирования пар должно быть равно двум");
        }
        List<String> keys;
        List<String> values;
        Map<String,String> pairs = new HashMap<>();

        if (stringLists.get(0).size() < stringLists.get(1).size()) {
            keys = stringLists.get(0);
            values = stringLists.get(1);
        } else {
            keys = stringLists.get(1);
            values = stringLists.get(0);
        }

        keys.forEach(key -> {
            //для выбора наиболее длинной подстроки, входящей в строку из другого списка
            AtomicInteger count = new AtomicInteger();
            values.forEach(value -> {

                List<String> keyWords = Arrays.asList(key.split(" "));
                List<String> valueWords = Arrays.asList(value.split(" "));

                AtomicReference<String> keyToPut = new AtomicReference<>("");
                AtomicReference<String> valueToPut = new AtomicReference<>("");

                valueWords.forEach(valueWord -> {
                    if (key.toLowerCase().contains(valueWord.toLowerCase()) && valueWord.length() > count.get()) {
                        count.set(valueWord.length());
                        keyToPut.set(key);
                        valueToPut.set(value);
                    }
                });

                for (String keyWord : keyWords) {
                    if (value.toLowerCase().contains(keyWord.toLowerCase()) && keyWord.length() > count.get()) {
                        count.set(keyWord.length());
                        keyToPut.set(key);
                        valueToPut.set(value);
                    }
                }

                if (!keyToPut.get().isBlank() && !valueToPut.get().isBlank()) {
                    pairs.put(keyToPut.get(), valueToPut.get());
                }
            });
        });
        if (keys.size() == 1 && values
                .size() == 1) {
            pairs.put(keys.get(0), values.get(0));
        }
        String singleKey = keys
                .stream()
                .filter(s -> !pairs.containsKey(s))
                .findAny()
        .orElse("?");
            List<String> singleValues = values
                    .stream()
                    .filter(v -> !pairs.containsValue(v))
                    .collect(Collectors.toList());

        if (!singleValues.isEmpty()) {
            pairs.put(singleValues.get(0), singleKey);
            singleValues.removeIf(value -> pairs.containsKey(value));
            singleValues.forEach(v -> pairs.put(v, "?"));
        }

        return pairs;
    }
}
