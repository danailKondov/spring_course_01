package ru.otus.spring01.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.spring01.controller.Messenger;

import java.util.Map;
import java.util.Set;

/**
 * Created by хитрый жук on 01.12.2018.
 */
public class TesterImpl implements Tester {

    private static final Logger log = LoggerFactory.getLogger(TesterImpl.class);
    private CsvParser parser;
    private Messenger messenger;

    public TesterImpl(CsvParser parser, Messenger messenger) {
        this.parser = parser;
        this.messenger = messenger;
    }

    @Override
    public void testStudents(String questionsFileName) {
        Map<String,String> questionsAndAnswers = parser.parseQuestionsFromFile(questionsFileName);
        int numOfGoodAnswers = 0;
        Set<String> questions = questionsAndAnswers.keySet();
        String firstName = messenger.askQuestion("Enter first name: ");
        String secondName = messenger.askQuestion("Enter second name: ");
        for (String question : questions) {
            String answer = messenger.askQuestion(question);
            if (questionsAndAnswers.get(question).equals(answer)) {
                numOfGoodAnswers++;
            }
        }

        System.out.println(firstName + " " + secondName);
        System.out.println("Number of correct answers is " + numOfGoodAnswers + " out of 5 possible");
    }
}
