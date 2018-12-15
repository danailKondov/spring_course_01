package ru.otus.spring01.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by хитрый жук on 30.11.2018.
 */
@Component
public class CsvParserImpl implements CsvParser {

    private static final Logger log = LoggerFactory.getLogger(CsvParserImpl.class);

    public Map<String, String> parseQuestionsFromFile(String fileName) {
        log.info("Started parsing file " + fileName);
        Map<String, String> result = new LinkedHashMap<>();
        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(getClass().getResourceAsStream("/" + fileName)))) {
            while(reader.ready()) {
                String s = reader.readLine();
                String[] arr = s.split(",");
                String question = arr[0];
                String answer = arr[1];
                result.put(question, answer.substring(1, arr[1].length() - 1));
            }
            log.info(result.size() + " questions was successfully parsed");
        } catch (IOException e) {
            log.error("csv parser error", e);
        }
        return result;
    }
}
