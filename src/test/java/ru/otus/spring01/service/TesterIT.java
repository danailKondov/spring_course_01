package ru.otus.spring01.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring01.controller.MessengerImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by хитрый жук on 15.12.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:app-test.properties")
public class TesterIT {

    @MockBean
    MessengerImpl messenger;

    @Autowired
    private TesterImpl tester;

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
        assertThat(result).contains(expected);
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
        assertThat(result).contains(expected);
    }

    @After
    public void restore() {
        System.setOut(originalOut);
    }
}
