package ru.otus.spring01.service;

import ru.otus.spring01.model.TestResult;

import java.util.List;

/**
 * Created by хитрый жук on 01.12.2018.
 */
public interface Tester {

    void testStudents();

    void close();

    List<TestResult> getTestResults();
}
