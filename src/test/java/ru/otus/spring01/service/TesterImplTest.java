package ru.otus.spring01.service;

import org.junit.Test;
import ru.otus.spring01.controller.Messenger;
import ru.otus.spring01.model.TestResult;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by хитрый жук on 01.12.2018.
 */
public class TesterImplTest {

    @Test
    public void testerTest() {
        CsvParser parser = mock(CsvParser.class);
        Map<String,String> questionnaire = new LinkedHashMap<>();
        questionnaire.put("Biggest country in Europe?", "Russia");
        questionnaire.put("Capital of Russia?", "Moscow");
        questionnaire.put("2+2?", "4");
        questionnaire.put("Best programming language?", "java");
        questionnaire.put("Best java framework?", "spring");
        when(parser.parseQuestionsFromFile(any())).thenReturn(questionnaire);

        MessageService messageService = mock(MessageService.class);
        when(messageService.getMessage(eq("enter.first.name"), any())).thenReturn("Ivan");
        when(messageService.getMessage(eq("enter.second.name"), any())).thenReturn("Ivanov");

        Messenger messenger = mock(Messenger.class);
        when(messenger.askQuestion(anyString()))
                .thenReturn("Ivan")
                .thenReturn("Ivanov")
                .thenReturn("Russia")
                .thenReturn("Moscow")
                .thenReturn("4")
                .thenReturn("java")
                .thenReturn("spring");

        TesterImpl tester = new TesterImpl(parser, messageService, messenger);
        tester.testStudents();

        verify(messenger).textMessage("Ivan Ivanov");
        verify(messageService).getMessage("correct.answers", new Object[]{5});

        TestResult result = tester.getTestResults().get(0);
        assertThat(result.getFirstName(), is("Ivan"));
        assertThat(result.getSecondName(), is("Ivanov"));
        assertThat(result.getNumOfGoodAnswers(), is(5));
        assertThat(result.getResults().size(), is(5));
    }
}