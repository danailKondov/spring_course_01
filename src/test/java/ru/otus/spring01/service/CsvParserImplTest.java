package ru.otus.spring01.service;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by хитрый жук on 30.11.2018.
 */
public class CsvParserImplTest {

    @Test
    public void parserTest() {
        CsvParserImpl parser = new CsvParserImpl();
        Map<String,String> map = parser.parseQuestionsFromFile("test-questions-en.csv");
        String result = map.toString();
        String expected = "{" +
                "\"Biggest country in Europe?\"=Russia, " +
                "\"Capital of Russia?\"=Moscow, " +
                "\"2+2?\"=4, " +
                "\"Best programming language?\"=java, " +
                "\"Best java framework?\"=spring" +
                "}";
        assertThat(result, is(expected));
    }

}