package acme.testing.manager.workplan;

import acme.testing.AcmePlannerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class ManagerWorkplanAddTaskTest extends AcmePlannerTest {
    @ParameterizedTest
    @CsvFileSource(resources = "/manager/workplan/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void NegativeAddTaskWorkplan() {
        super.signIn("Managers1", "Managers1");
        super.clickOnMenu("Managers", "Workplan list");
        super.clickOnListingRecord(0);
        super.clickOnSubmitButton("Add task");
        super.checkErrorsExist();
    }
    @ParameterizedTest
    @CsvFileSource(resources = "/manager/workplan/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(20)
    public void positiveAddTaskWorkplan() {
        super.signIn("Managers1", "Managers1");
        super.clickOnMenu("Managers", "Workplan list");
        super.clickOnListingRecord(0);
        super.fillInputBoxIn("begin", "2022/10/09 09:00");
        super.fillInputBoxIn("end", "2025/10/09 09:00");
        super.clickOnSubmitButton("Update");
        super.clickOnListingRecord(0);
        super.clickOnSubmitButton("Add task");
        super.checkNotPanicExists();
    }

}
