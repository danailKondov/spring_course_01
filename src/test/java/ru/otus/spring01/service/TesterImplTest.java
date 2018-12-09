package ru.otus.spring01.service;

import org.junit.Test;
import org.springframework.context.MessageSource;
import ru.otus.spring01.controller.Messenger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by хитрый жук on 01.12.2018.
 */
public class TesterImplTest {

    @Test
    public void testerTest() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        CsvParser parser = mock(CsvParser.class);
        Map<String,String> questionnaire = new LinkedHashMap<>();
        questionnaire.put("Biggest country in Europe?", "Russia");
        questionnaire.put("Capital of Russia?", "Moscow");
        questionnaire.put("2+2?", "4");
        questionnaire.put("Best programming language?", "java");
        questionnaire.put("Best java framework?", "spring");
        when(parser.parseQuestionsFromFile(any())).thenReturn(questionnaire);

        Messenger messenger = mock(Messenger.class);
        when(messenger.askQuestion(anyString()))
                .thenReturn("Ivan")
                .thenReturn("Ivanov")
                .thenReturn("Russia")
                .thenReturn("Moscow")
                .thenReturn("4")
                .thenReturn("java")
                .thenReturn("spring");

        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(eq("correct.answers"), eq(new Object[]{5}), any()))
                .thenReturn("Number of correct answers is 5 out of 5 possible");

        TesterImpl tester = new TesterImpl(parser, messenger, messageSource);
        tester.testStudents();

        String result = out.toString();
        String expected = "Ivan Ivanov\r\nNumber of correct answers is 5 out of 5 possible\r\n";
        assertThat(result, is(expected));

        System.setOut(originalOut);
    }
}