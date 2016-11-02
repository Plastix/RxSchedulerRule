package io.github.plastix.rx2;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RxSchedulerRuleTest {

    @Rule
    public RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();

    private PublishSubject<Integer> source;

    @Before
    public void setUp() {
        source = PublishSubject.create();
    }

    @Test
    public void overrideNewThread() {
        TestObserver<Integer> test = source.subscribeOn(Schedulers.newThread())
                .test();

        source.onNext(0);

        test.assertValue(0);
        test.assertNotTerminated();

        verifyCorrectThread(test.lastThread());
    }

    private void verifyCorrectThread(Thread thread) {
        Assert.assertTrue(thread.getName().equals("main"));
    }

    @Test
    public void overrideIOThread() {
        TestObserver<Integer> test = source.subscribeOn(Schedulers.io())
                .test();

        source.onNext(0);

        test.assertValue(0);
        test.assertNotTerminated();

        verifyCorrectThread(test.lastThread());
    }

    @Test
    public void overrideComputationThread() {
        TestObserver<Integer> test = source.subscribeOn(Schedulers.computation())
                .test();

        source.onNext(0);

        test.assertValue(0);
        test.assertNotTerminated();

        verifyCorrectThread(test.lastThread());
    }

    @Test
    public void overrideAndroidThread() {
        TestObserver<Integer> test = source.observeOn(AndroidSchedulers.mainThread())
                .test();

        source.onNext(0);

        test.assertValue(0);
        test.assertNotTerminated();

        verifyCorrectThread(test.lastThread());
    }
}