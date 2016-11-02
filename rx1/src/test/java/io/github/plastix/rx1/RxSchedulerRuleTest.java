package io.github.plastix.rx1;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rx.android.schedulers.AndroidSchedulers;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class RxSchedulerRuleTest {

    @Rule
    public RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();

    private TestSubscriber<Integer> testSubscriber;
    private PublishSubject<Integer> source;

    @Before
    public void setUp() {
        testSubscriber = TestSubscriber.create();
        source = PublishSubject.create();
    }

    @Test
    public void overrideNewThread() {
        source.subscribeOn(Schedulers.newThread())
                .subscribe(testSubscriber);

        source.onNext(0);

        testSubscriber.assertValue(0);
        testSubscriber.assertNoTerminalEvent();

        verifyCorrectThread();
    }

    @Test
    public void overrideIOThread() {
        source.subscribeOn(Schedulers.io())
                .subscribe(testSubscriber);

        source.onNext(0);

        testSubscriber.assertValue(0);
        testSubscriber.assertNoTerminalEvent();

        verifyCorrectThread();
    }

    @Test
    public void overrideComputationThread() {
        source.subscribeOn(Schedulers.computation())
                .subscribe(testSubscriber);

        source.onNext(0);

        testSubscriber.assertValue(0);
        testSubscriber.assertNoTerminalEvent();

        verifyCorrectThread();
    }

    private void verifyCorrectThread() {
        Assert.assertTrue(testSubscriber.getLastSeenThread().getName().equals("main"));
    }

    @Test
    public void overrideAndroidThread() {
        source.observeOn(AndroidSchedulers.mainThread())
                .subscribe(testSubscriber);

        source.onNext(0);

        testSubscriber.assertValue(0);
        testSubscriber.assertNoTerminalEvent();

        verifyCorrectThread();
    }
}