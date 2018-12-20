package ru.otus.spring01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring01.controller.Messenger;
import ru.otus.spring01.model.TestResult;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by хитрый жук on 15.12.2018.
 */
@Service
public class TesterImpl implements Tester {

    private CsvParser parser;
    private MessageService messageService;
    private Messenger messenger;
    private List<TestResult> testResults = new ArrayList<>();

    @Autowired
    public TesterImpl(CsvParser parser, MessageService messageService, Messenger messenger) {
        this.parser = parser;
        this.messageService = messageService;
        this.messenger = messenger;
    }

    @Override
    public void testStudents() {
        String fileName = messageService.getQuestionsFileName();
        Map<String,String> questionsAndAnswers = parser.parseQuestionsFromFile(fileName);
        int numOfGoodAnswers = 0;
        Set<String> questions = questionsAndAnswers.keySet();

        TestResult result = new TestResult();
        String firstName = messenger.askQuestion(messageService.getMessage("enter.first.name", new Object[]{}));
        result.setFirstName(firstName);
        String secondName = messenger.askQuestion(messageService.getMessage("enter.second.name", new Object[]{}));
        result.setSecondName(secondName);
        for (String question : questions) {
            String answer = messenger.askQuestion(question);
            if (questionsAndAnswers.get(question).equals(answer)) {
                numOfGoodAnswers++;
                result.getResults().put(question, "+");
            } else {
                result.getResults().put(question, "-");
            }
        }
        result.setNumOfGoodAnswers(numOfGoodAnswers);
        testResults.add(result);

        messenger.textMessage(firstName + " " + secondName);
        messenger.textMessage(messageService.getMessage("correct.answers", new Object[]{numOfGoodAnswers}));
    }

    @Override
    public void close() {
        messenger.close();
    }

    @Override
    public List<TestResult> getTestResults() {
        return testResults;
    }

    @PreDestroy
    public void closeResources() {
        close();
    }
}
