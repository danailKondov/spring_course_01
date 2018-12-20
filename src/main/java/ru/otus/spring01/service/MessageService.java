package ru.otus.spring01.service;

import java.util.Locale;

/**
 * Created by хитрый жук on 20.12.2018.
 */
public interface MessageService {
    String getQuestionsFileName();

    String getMessage(String messageKey, Object[] objects);

    void setLocale(Locale locale);
}
