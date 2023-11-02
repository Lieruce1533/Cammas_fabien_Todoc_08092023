package com.cleanup.todoc;

import androidx.lifecycle.LiveData;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import androidx.lifecycle.Observer;

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