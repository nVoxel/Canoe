package com.voxeldev.canoe.utils;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.test.TestCoroutineDispatchersKt;
import kotlinx.coroutines.test.TestDispatcher;
import kotlinx.coroutines.test.TestDispatchers;

public class MainDispatcherRule extends TestWatcher {

    private final TestDispatcher testDispatcher;

    public MainDispatcherRule() {
        this(TestCoroutineDispatchersKt.UnconfinedTestDispatcher(null, null));
    }

    public MainDispatcherRule(TestDispatcher testDispatcher) {
        this.testDispatcher = testDispatcher;
    }

    @Override
    protected void starting(Description description) {
        TestDispatchers.setMain(Dispatchers.INSTANCE, testDispatcher);
    }

    @Override
    protected void finished(Description description) {
        TestDispatchers.resetMain(Dispatchers.INSTANCE);
    }
}
