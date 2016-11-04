package io.github.plastix.rxschedulerrule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

/**
 * JUnit Test Rule which overrides RxJava and Android schedulers for use in unit tests.
 * <p>
 * All schedulers are replaced with Schedulers.immediate().
 */
public class RxSchedulerRule implements TestRule {

    private final RxAndroidSchedulersHook rxAndroidSchedulersHook = new RxAndroidSchedulersHook() {
        @Override
        public Scheduler getMainThreadScheduler() {
            return Schedulers.immediate();
        }
    };

    private final Func1<Scheduler, Scheduler> rxJavaSchedulerHook = new Func1<Scheduler, Scheduler>() {
        @Override
        public Scheduler call(Scheduler scheduler) {
            return Schedulers.immediate();
        }
    };

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxAndroidPlugins.getInstance().reset();
                RxAndroidPlugins.getInstance().registerSchedulersHook(rxAndroidSchedulersHook);

                RxJavaHooks.reset();
                RxJavaHooks.setOnIOScheduler(rxJavaSchedulerHook);
                RxJavaHooks.setOnNewThreadScheduler(rxJavaSchedulerHook);
                RxJavaHooks.setOnComputationScheduler(rxJavaSchedulerHook);

                base.evaluate();

                RxAndroidPlugins.getInstance().reset();
                RxJavaHooks.reset();
            }
        };
    }
}
