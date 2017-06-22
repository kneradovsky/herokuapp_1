import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by vk on 22.06.17.
 */
@RunWith(Cucumber.class)
@CucumberOptions(glue = "ru.open.meetup.steps")
public class CucumberTest {
}
