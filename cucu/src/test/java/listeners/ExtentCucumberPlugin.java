package listeners;

import com.aventstack.extentreports.Status;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStepFinished;
import utils.ExtentManager;

public class ExtentCucumberPlugin implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, this::handleStepFinished);
    }

    private void handleStepFinished(TestStepFinished event) {
        if (!(event.getTestStep() instanceof PickleStepTestStep)) {
            return;
        }

        if (ExtentManager.getTest() == null) {
            return;
        }

        PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
        String stepName = (step.getStep().getKeyword() + step.getStep().getText()).trim();
        Status status = mapStatus(event.getResult().getStatus().name());
        String details = event.getResult().getError() != null
                ? stepName + " - " + event.getResult().getError().getMessage()
                : stepName;

        ExtentManager.getTest().log(status, details);
    }

    private Status mapStatus(String cucumberStatus) {
        switch (cucumberStatus) {
            case "PASSED":
                return Status.PASS;
            case "FAILED":
                return Status.FAIL;
            case "SKIPPED":
                return Status.SKIP;
            case "PENDING":
            case "UNDEFINED":
            case "AMBIGUOUS":
                return Status.WARNING;
            default:
                return Status.INFO;
        }
    }
}
