package io.github.plastix.rxschedulerrule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * JUnit Test Rule which overrides RxJava and Android schedulers for use in unit tests.
 * <p>
 * All schedulers are replaced with Schedulers.trampoline().
 */
public class RxSchedulerRule implements TestRule {

    private Scheduler SCHEDULER_INSTANCE = Schedulers.trampoline();

    private Function<Scheduler, Scheduler> schedulerFunction = new Function<Scheduler, Scheduler>() {
        @Override
        public Scheduler apply(Scheduler scheduler) throws Exception {
            return SCHEDULER_INSTANCE;
        }
    };

    private Function<Callable<Scheduler>, Scheduler> schedulerFunctionLazy = new Function<Callable<Scheduler>, Scheduler>() {
        @Override
        public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
            return SCHEDULER_INSTANCE;
        }
    };

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxAndroidPlugins.reset();
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerFunctionLazy);

                RxJavaPlugins.reset();
                RxJavaPlugins.setIoSchedulerHandler(schedulerFunction);
                RxJavaPlugins.setNewThreadSchedulerHandler(schedulerFunction);
                RxJavaPlugins.setComputationSchedulerHandler(schedulerFunction);

                base.evaluate();

                RxAndroidPlugins.reset();
                RxJavaPlugins.reset();
            }
        };
    }
}
