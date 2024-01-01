package com.voxeldev.canoe.utils;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.koin.core.Koin;
import org.koin.mp.KoinPlatformTools;
import org.koin.test.KoinTest;
import org.koin.test.mock.MockProviderRule;
import org.mockito.Mockito;

import kotlin.jvm.internal.ClassBasedDeclarationContainer;

/**
 * @author nvoxel
 */
public class BaseUnitTest implements KoinTest {

    @Rule
    public MockProviderRule mockProviderRule = MockProviderRule.Companion
            .create(kClass -> Mockito.mock(((ClassBasedDeclarationContainer) kClass).getJClass()));

    @Rule
    public MainDispatcherRule mainDispatcherRule = new MainDispatcherRule();

    @NotNull
    @Override
    public Koin getKoin() {
        return KoinPlatformTools.INSTANCE.defaultContext().get();
    }
}

