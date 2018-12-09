package ru.otus.spring01.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.spring01.controller.Messenger;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by хитрый жук on 01.12.2018.
 */
@Service
public class TesterImpl implements Tester {

    private static final Logger log = LoggerFactory.getLogger(TesterImpl.class);
    private CsvParser parser;
    private Messenger messenger;
    private Locale locale;
    private MessageSource messageSource;

    @Value("${file.name.en}")
    private String fileNameEN;

    @Value("${file.name.ru}")
    private String fileNameRu;

    @Value("${language}")
    private String language;

    @Autowired
    public TesterImpl(CsvParser parser, Messenger messenger, MessageSource messageSource) {
        this.parser = parser;
        this.messenger = messenger;
        this.messageSource = messageSource;
    }

    @Override
    public void testStudents() {
        if (locale == null) {
            defineLocale();
        }
        String fileName = locale == Locale.ENGLISH ? fileNameEN : fileNameRu;
        Map<String,String> questionsAndAnswers = parser.parseQuestionsFromFile(fileName);
        int numOfGoodAnswers = 0;
        Set<String> questions = questionsAndAnswers.keySet();
        String firstName = messenger.askQuestion(messageSource.getMessage("enter.first.name", new Object[]{}, locale));
        String secondName = messenger.askQuestion(messageSource.getMessage("enter.second.name", new Object[]{}, locale));
        for (String question : questions) {
            String answer = messenger.askQuestion(question);
            if (questionsAndAnswers.get(question).equals(answer)) {
                numOfGoodAnswers++;
            }
        }

        System.out.println(firstName + " " + secondName);
        System.out.println(messageSource.getMessage("correct.answers", new Object[]{numOfGoodAnswers}, locale));
    }

    @Override
    public void close() {
        messenger.close();
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    private void defineLocale() {
        if (language == null || language.isEmpty()) {
            locale = LocaleContextHolder.getLocale();
        } else if (language.equals("en")) {
            locale = Locale.ENGLISH;
        } else {
            locale = new Locale("ru");
        }
    }
}
