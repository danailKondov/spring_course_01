package ru.otus.spring01.config;

import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.spring01.controller.Messenger;
import ru.otus.spring01.service.CsvParser;
import ru.otus.spring01.service.CsvParserImpl;
import ru.otus.spring01.service.Tester;
import ru.otus.spring01.service.TesterImpl;

/**
 * Created by хитрый жук on 08.12.2018.
 */
@Profile("test")
@Configuration
@PropertySource("classpath:app-test.properties")
public class TestConfig {

    @Bean
    CsvParser parser() {
        return new CsvParserImpl();
    }

    @Bean
    Messenger messenger() {
        return Mockito.mock(Messenger.class);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:messages");
        ms.setDefaultEncoding("windows-1251");
        return ms;
    }

    @Bean
    Tester tester() {
        return new TesterImpl(parser(), messenger(), messageSource());
    }
}
