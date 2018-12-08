package ru.otus.spring01.controller;

/**
 * Created by хитрый жук on 01.12.2018.
 */
public interface Messenger {

    String askQuestion(String question);

    void close();
}
