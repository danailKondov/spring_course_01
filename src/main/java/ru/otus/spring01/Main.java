package ru.otus.spring01;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring01.controller.Messenger;
import ru.otus.spring01.controller.MessengerImpl;
import ru.otus.spring01.service.CsvParser;
import ru.otus.spring01.service.CsvParserImpl;
import ru.otus.spring01.service.Tester;

@PropertySource("classpath:app.properties")
@Configuration
@ComponentScan
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        Tester tester = context.getBean(Tester.class);
        tester.testStudents();
        tester.close();
    }

    @Bean
    Messenger messenger() {
        return new MessengerImpl();
    }

    @Bean
    CsvParser parser() {
        return new CsvParserImpl();
    }
}
