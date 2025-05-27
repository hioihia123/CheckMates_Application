 /* written by: Nguyen, Anisha, Amy, Danil
   tested by: Nguyen, Anisha, Amy, Danil
   debugged by: Nguyen, Anisha, Amy, Danil
 */
package form;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("Starting test execution...");

        // Create a launcher discovery request
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        selectClass(LoginTest.class),
                        selectClass(SignUpTest.class),
                        selectClass(AttendanceDashboardTest.class),
                        selectClass(ClassDashboardTest.class),
                        selectClass(DashboardTest.class)
                        //add more tests here.
                )
                .build();

        // Create and configure launcher
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);

        // Execute tests
        launcher.execute(request);

        // Print results
        TestExecutionSummary summary = listener.getSummary();
        printTestSummary(summary);

        // Exit with status code (0 = success, 1 = failures)
        System.exit(summary.getTestsFailedCount() > 0 ? 1 : 0);
    }

    private static void printTestSummary(TestExecutionSummary summary) {
        System.out.println("\nTest Execution Summary:");
        System.out.println("--------------------------------------------------");
        System.out.printf("Total tests found: %d%n", summary.getTestsFoundCount());
        System.out.printf("Tests succeeded: %d%n", summary.getTestsSucceededCount());
        System.out.printf("Tests failed: %d%n", summary.getTestsFailedCount());
        System.out.printf("Tests aborted: %d%n", summary.getTestsAbortedCount());
        System.out.printf("Time taken: %d ms%n", summary.getTimeFinished());

        if (summary.getTestsFailedCount() > 0) {
            System.out.println("\nFailure Details:");
            summary.getFailures().forEach(failure -> {
                System.out.printf("%n- Test: %s%n", failure.getTestIdentifier().getDisplayName());
                System.out.printf("  Exception: %s%n", failure.getException().toString());
                if (failure.getException().getCause() != null) {
                    System.out.printf("  Caused by: %s%n", failure.getException().getCause().toString());
                }
            });
        }
    }
}