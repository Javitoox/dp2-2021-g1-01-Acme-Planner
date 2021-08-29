package acme.testing.authenticated.consumer;

import acme.testing.AcmeWorkPlansTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;


public class AuthenticatedConsumerCreateTest extends AcmeWorkPlansTest {
	
	// This test case check that the input boxes are empty, so the authenticated can't become consumer and system throws error.
    @ParameterizedTest
    @CsvFileSource(resources = "/authenticated/consumer/negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void negativeBecomeConsumer(final String username, final String password, final String name, final String surname, final String email, final String company, final String sector, final String phone) {
        super.signUp(username, password, name, surname, email, phone);
        super.signIn(username, password);
        super.clickOnMenu("Account", "Become a consumer");
        super.fillInputBoxIn("company", company);
        super.fillInputBoxIn("sector", sector);
        super.clickOnSubmitButton("Register");
        super.checkErrorsExist();
    }
    
    // This test case check that the input boxes are fill without errors, so the authenticated can become consumer
    @ParameterizedTest
    @CsvFileSource(resources = "/sign-up/positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(20)
    public void positiveBecomeConsumer(final String username, final String password, final String name, final String surname, final String email, final String company, final String sector, final String phone) {
        super.signIn(username, password);
        super.clickOnMenu("Account", "Become a consumer");
        super.fillInputBoxIn("company", company);
        super.fillInputBoxIn("sector", sector);

        super.clickOnSubmitButton("Register");
        super.clickOnMenu("Account", "Consumer data");
        super.checkInputBoxHasValue("company", company);
        super.checkInputBoxHasValue("sector", sector);

    }




    }
