package ru.otus.spring01.service;

import java.util.Map;

/**
 * Created by хитрый жук on 30.11.2018.
 */
public interface CsvParser {
    Map<String,String> parseQuestionsFromFile(String fileName);
}
