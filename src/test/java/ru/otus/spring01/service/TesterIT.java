package ru.otus.spring01.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring01.config.TestConfig;
import ru.otus.spring01.controller.Messenger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by хитрый жук on 08.12.2018.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { TestConfig.class})
public class TesterIT {

    @Autowired
    Messenger messenger;

    @Autowired
    private Tester tester;

    private ByteArrayOutputStream out;
    private PrintStream originalOut;

    @Before
    public void init() {
        out = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(out));
    }

    @Test
    public void studentsTesterWhenEnglishLocaleTest() {
        when(messenger.askQuestion(anyString()))
                .thenReturn("Ivan")
                .thenReturn("Ivanov")
                .thenReturn("Russia")
                .thenReturn("Moscow")
                .thenReturn("4")
                .thenReturn("java")
                .thenReturn("spring");
        tester.setLocale(Locale.ENGLISH);

        tester.testStudents();
        tester.close();

        String result = out.toString();
        String expected = "Ivan Ivanov\r\nNumber of correct answers is 5 out of 5 possible\r\n";
        assertThat(result, is(expected));
    }

    @Test
    public void studentsTesterWhenRussianLocaleTest() {
        when(messenger.askQuestion(anyString()))
                .thenReturn("Иван")
                .thenReturn("Иванов")
                .thenReturn("Россия")
                .thenReturn("Москва")
                .thenReturn("4")
                .thenReturn("джава")
                .thenReturn("спринг");
        tester.setLocale(new Locale("ru"));

        tester.testStudents();
        tester.close();

        String result = out.toString();
        String expected = "Иван Иванов\r\nКоличество правильных ответов - 5 из 5 возможных\r\n";
        assertThat(result, is(expected));
    }

    @After
    public void restore() {
        System.setOut(originalOut);
    }
}
