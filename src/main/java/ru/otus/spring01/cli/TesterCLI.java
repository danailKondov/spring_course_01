package ru.otus.spring01.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.AbsoluteWidthSizeConstraints;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.shell.table.TableModelBuilder;
import ru.otus.spring01.controller.Messenger;
import ru.otus.spring01.model.TestResult;
import ru.otus.spring01.service.MessageService;
import ru.otus.spring01.service.Tester;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.shell.table.CellMatchers.column;

/**
 * Created by хитрый жук on 19.12.2018.
 */

@ShellComponent
public class TesterCLI {

    private Tester tester;
    private MessageService messageService;
    private Messenger messenger;

    @Autowired
    public TesterCLI(Tester tester, MessageService messageService, Messenger messenger) {
        this.tester = tester;
        this.messageService = messageService;
        this.messenger = messenger;
    }

    @ShellMethod(value = "Start testing students", key = "start")
    public void startTesting() {
        tester.testStudents();
    }

    @ShellMethod(value = "Define language: english (\"eng\") or russian (\"ru\")", key = "lang")
    public void defineLanguage(
            @ShellOption(help = "language") String lang) {
        switch (lang) {
            case "eng":
                messageService.setLocale(Locale.ENGLISH);
                break;
            case "ru":
                messageService.setLocale(new Locale("ru"));
                break;
            default:
                messenger.textMessage("There is only two options: english (\"eng\") or russian (\"ru\")");
        }
    }

    @ShellMethod(value = "Show all testing results", key = "show-all")
    public String showAllResults() {
        List<TestResult> testResults = tester.getTestResults();
        if (testResults.size() == 0) {
            messenger.textMessage("No results to show");
            return null;
        }

        TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();

        for (TestResult testResult : testResults) {
            addResultsToModelBuilder(modelBuilder, testResult);
        }

        TableModel model = modelBuilder.build();

        return new TableBuilder(model)
                .on(column(0)).addSizer(new AbsoluteWidthSizeConstraints(50)).addAligner(SimpleHorizontalAligner.left)
                .on(column(1)).addSizer(new AbsoluteWidthSizeConstraints(30)).addAligner(SimpleHorizontalAligner.left)
                .build()
                .render(400);
    }

    @ShellMethod(value = "Show testing results by name", key = "show-for")
    public String showResultsForName(
            @ShellOption(help = "first name") String firstName,
            @ShellOption(help = "second name") String secondName) {
        List<TestResult> testResults = tester.getTestResults();
        Optional<TestResult> optional = testResults.stream()
                .filter(res -> res.getFirstName().equals(firstName) && res.getSecondName().equals(secondName))
                .findAny();
        if (!optional.isPresent()) {
            messenger.textMessage("No results to show");
            return null;
        }

        TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();
        TestResult testResult = optional.get();
        addResultsToModelBuilder(modelBuilder, testResult);

        TableModel model = modelBuilder.build();

        return new TableBuilder(model)
                .on(column(0)).addSizer(new AbsoluteWidthSizeConstraints(50)).addAligner(SimpleHorizontalAligner.left)
                .on(column(1)).addSizer(new AbsoluteWidthSizeConstraints(30)).addAligner(SimpleHorizontalAligner.left)
                .build()
                .render(400);
    }

    private void addResultsToModelBuilder(TableModelBuilder<String> modelBuilder, TestResult testResult) {
        modelBuilder.addRow()
                .addValue(messageService.getMessage("date.of.testing", new Object[]{}))
                .addValue(new SimpleDateFormat("dd MMM yyyy").format(testResult.getDateOfTesting()))
                .addRow()
                .addValue(messageService.getMessage("first.name", new Object[]{}))
                .addValue(testResult.getFirstName())
                .addRow()
                .addValue(messageService.getMessage("second.name", new Object[]{}))
                .addValue(testResult.getSecondName())
                .addRow()
                .addValue(messageService.getMessage("cli.question", new Object[]{}))
                .addValue(messageService.getMessage("cli.grade", new Object[]{}));
        testResult.getResults().forEach((question, grade) -> {
            modelBuilder.addRow()
                    .addValue(question)
                    .addValue(grade);
        });
        modelBuilder.addRow()
                .addValue(messageService.getMessage("num.of.good.answers", new Object[]{}))
                .addValue(String.valueOf(testResult.getNumOfGoodAnswers()));
    }
}
