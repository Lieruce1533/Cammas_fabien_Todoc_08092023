package com.cleanup.todoc.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtil {

    public static <T> void observeForTesting(LiveData<T> liveData, Observer<T> observer) {
        liveData.observeForever(observer);
    }

    public static <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);

        liveData.observeForever(value -> {
            data[0] = value;
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        //noinspection unchecked
        return (T) data[0];
    }
}