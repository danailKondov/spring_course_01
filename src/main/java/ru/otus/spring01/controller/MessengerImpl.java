package ru.otus.spring01.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by хитрый жук on 01.12.2018.
 */
@Component
public class MessengerImpl implements Messenger {

    private static final Logger log = LoggerFactory.getLogger(MessengerImpl.class);
    private BufferedReader reader;

    @Override
    public String askQuestion(String question) {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(System.in));
            log.info("Reader initiated");
        }

        String answer = "";
        try {
            System.out.println(question);
            answer = reader.readLine();
        } catch (IOException e) {
            log.error("Error while reading answer", e);
        }
        return answer;
    }

    @Override
    public void textMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                log.error("Reader was not closed properly", e);
            }
            log.info("Reader closed");
        }
    }
}
