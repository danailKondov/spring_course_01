package ru.otus.spring01.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring01.controller.MessengerImpl;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by хитрый жук on 15.12.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"
})
@TestPropertySource(locations = "classpath:app-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TesterIT {

    @Autowired
    private Shell shell;

    @MockBean
    private MessengerImpl messenger;

    @Autowired
    private MessageService messageService;

    @Test
    public void studentsTesterWhenEnglishLocaleTest() {
        configureMessengerMockEng();
        messageService.setLocale(Locale.ENGLISH);

        shell.evaluate(() -> "start");
        messenger.close();

        verify(messenger).textMessage("Ivan Ivanov");
        verify(messenger).textMessage("Number of correct answers is 5 out of 5 possible");
    }

    @Test
    public void studentsTesterWhenRussianLocaleTest() {
        configureMessengerMockRus();
        messageService.setLocale(new Locale("ru"));

        shell.evaluate(() -> "start");
        messenger.close();

        verify(messenger).textMessage("Иван Иванов");
        verify(messenger).textMessage("Количество правильных ответов - 5 из 5 возможных");
    }

    @Test
    public void showAllResultWithCliTest() {
        configureMessengerMockRus();
        messageService.setLocale(new Locale("ru"));

        shell.evaluate(() -> "start");
        Object result = shell.evaluate(() -> "show-all");
        messenger.close();

        String expected = "Имя                                               Иван                          \n" +
                          "Фамилия                                           Иванов                        \n" +
                          "Вопрос                                            Оценка                        \n" +
                        "\"Лучший язык программирования\"                    +                             \n" +
                        "\"Лучший фреймворк для джавы\"                      +                             \n" +
                        "\"Самая большая страна Европы\"                     +                             \n" +
                        "\"2+2?\"                                            +                             \n" +
                        "\"Столица России\"                                  +                             \n" +
                          "Количество правильных ответов                     5                             \n";

        assertThat(result.toString()).contains(expected);
    }

    @Test
    public void showResultByNameWithCliTest() {
        when(messenger.askQuestion(anyString()))
                .thenReturn("Ivan")
                .thenReturn("Ivanov")
                .thenReturn("Russia")
                .thenReturn("Moscow")
                .thenReturn("4")
                .thenReturn("java")
                .thenReturn("spring")
                .thenReturn("John")
                .thenReturn("Johnson")
                .thenReturn("USA")
                .thenReturn("New-York")
                .thenReturn("4")
                .thenReturn("C#")
                .thenReturn(".NET");

        messageService.setLocale(Locale.ENGLISH);

        shell.evaluate(() -> "start");
        shell.evaluate(() -> "start");
        messenger.close();

        Object result = shell.evaluate(() -> "show-for John Johnson");

        String expected = "First name                                        John                          \n" +
                          "Second Name                                       Johnson                       \n" +
                          "Question                                          Grade                         \n" +
                        "\"2+2?\"                                            +                             \n" +
                        "\"Capital of Russia?\"                              -                             \n" +
                        "\"Best programming language?\"                      -                             \n" +
                        "\"Biggest country in Europe?\"                      -                             \n" +
                        "\"Best java framework?\"                            -                             \n" +
                          "Number of good answers                            1                             \n";

        String notExpected = "First name                                        Ivan                          \n" +
                             "Second Name                                       Ivanov                        \n" +
                             "Question                                          Grade                         \n" +
                           "\"2+2?\"                                            +                             \n" +
                           "\"Capital of Russia?\"                              +                             \n" +
                           "\"Best programming language?\"                      +                             \n" +
                           "\"Biggest country in Europe?\"                      +                             \n" +
                           "\"Best java framework?\"                            +                             \n" +
                             "Number of good answers                            5                             \n";

        assertThat(result.toString())
                .contains(expected)
                .doesNotContain(notExpected);
    }

    @Test
    public void showAllResultWithCliWhenNoResultsTest() {
        shell.evaluate(() -> "show-all");
        messenger.close();

        verify(messenger).textMessage("No results to show");
    }

    @Test
    public void showResultByNameWithCliWhenNoResultsTest() {
        configureMessengerMockEng();
        messageService.setLocale(Locale.ENGLISH);

        shell.evaluate(() -> "start");
        shell.evaluate(() -> "show-for No Name");
        messenger.close();

        verify(messenger).textMessage("No results to show");
    }

    @Test
    public void defineEnglishLocaleTest() {
        configureMessengerMockEng();
        shell.evaluate(() -> "lang eng");

        shell.evaluate(() -> "start");
        messenger.close();

        verify(messenger).textMessage("Ivan Ivanov");
        verify(messenger).textMessage("Number of correct answers is 5 out of 5 possible");
    }

    @Test
    public void defineRussianLocaleTest() {
        configureMessengerMockRus();
        shell.evaluate(() -> "lang ru");

        shell.evaluate(() -> "start");
        messenger.close();

        verify(messenger).textMessage("Иван Иванов");
        verify(messenger).textMessage("Количество правильных ответов - 5 из 5 возможных");
    }

    private void configureMessengerMockEng() {
        when(messenger.askQuestion(anyString()))
                .thenReturn("Ivan")
                .thenReturn("Ivanov")
                .thenReturn("Russia")
                .thenReturn("Moscow")
                .thenReturn("4")
                .thenReturn("java")
                .thenReturn("spring");
    }

    private void configureMessengerMockRus() {
        when(messenger.askQuestion(anyString()))
                .thenReturn("Иван")
                .thenReturn("Иванов")
                .thenReturn("Россия")
                .thenReturn("Москва")
                .thenReturn("4")
                .thenReturn("джава")
                .thenReturn("спринг");
    }
}
