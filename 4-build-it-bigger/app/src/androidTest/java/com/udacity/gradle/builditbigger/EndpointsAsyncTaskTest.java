package com.udacity.gradle.builditbigger;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class EndpointsAsyncTaskTest {

    @Test
    public void asyncTaskDoesNotReturnEmptyString() throws
            ExecutionException, InterruptedException {

        EndpointsAsyncTask task = new EndpointsAsyncTask();
        task.execute();

        String joke = task.get();
        assertNotNull(joke);
        assertNotEquals("", joke);
    }

}