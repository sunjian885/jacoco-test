package com.common.testng;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import java.util.*;

public class TestResultListener extends TestListenerAdapter {

    public TestResultListener() {
    }

    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        System.out.println(tr.getName() + " Failure");
    }

    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        System.out.println(tr.getName() + " Skipped");
    }

    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        System.out.println(tr.getName() + " Success");
    }

    public void onTestStart(ITestResult tr) {
        super.onTestStart(tr);
        System.out.println(tr.getName() + " Start");
    }

    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        ArrayList<ITestResult> testsToBeRemoved = new ArrayList();
        Set<Integer> passedTestIds = new HashSet();
        Iterator var4 = testContext.getPassedTests().getAllResults().iterator();

        while(var4.hasNext()) {
            ITestResult passedTest = (ITestResult)var4.next();
            Reporter.log("PassedTests = " + passedTest.getName());
            passedTestIds.add(this.getId(passedTest));
        }

        Set<Integer> skipTestIds = new HashSet();
        Iterator var10 = testContext.getSkippedTests().getAllResults().iterator();

        while(true) {
            while(var10.hasNext()) {
                ITestResult skipTest = (ITestResult)var10.next();
                Reporter.log("skipTest = " + skipTest.getName());
                int skipTestId = this.getId(skipTest);
                if (!skipTestIds.contains(skipTestId) && !passedTestIds.contains(skipTestId)) {
                    skipTestIds.add(skipTestId);
                } else {
                    testsToBeRemoved.add(skipTest);
                }
            }

            Set<Integer> failedTestIds = new HashSet();
            Iterator iterator = testContext.getFailedTests().getAllResults().iterator();

            while(true) {
                ITestResult testResult;
                while(iterator.hasNext()) {
                    testResult = (ITestResult)iterator.next();
                    Reporter.log("failedTest = " + testResult.getName());
                    int failedTestId = this.getId(testResult);
                    if (!failedTestIds.contains(failedTestId) && !passedTestIds.contains(failedTestId) && !skipTestIds.contains(failedTestId)) {
                        failedTestIds.add(failedTestId);
                    } else {
                        testsToBeRemoved.add(testResult);
                    }
                }

                iterator = testContext.getFailedTests().getAllResults().iterator();

                while(iterator.hasNext()) {
                    testResult = (ITestResult)iterator.next();
                    if (testsToBeRemoved.contains(testResult)) {
                        Reporter.log("Remove repeat Fail Test: " + testResult.getName());
                        iterator.remove();
                    }
                }

                return;
            }
        }
    }

    private int getId(ITestResult result) {
        int id = result.getTestClass().getName().hashCode();
        id += result.getMethod().getMethodName().hashCode();
        id += result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0;
        return id;
    }
}
