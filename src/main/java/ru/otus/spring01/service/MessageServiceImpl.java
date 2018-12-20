package ru.otus.spring01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by хитрый жук on 20.12.2018.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private Locale locale;
    private MessageSource messageSource;

    @Value("${file.name.en}")
    private String fileNameEN;

    @Value("${file.name.ru}")
    private String fileNameRu;

    @Value("${language}")
    private String language;

    @Autowired
    public MessageServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
        defineLocale();
    }

    @Override
    public String getQuestionsFileName() {
        return locale == Locale.ENGLISH ? fileNameEN : fileNameRu;
    }

    @Override
    public String getMessage(String messageKey, Object[] objects) {
        return messageSource.getMessage(messageKey, objects, locale);
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
