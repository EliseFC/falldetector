package com.android.falldetector;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private HistoryDataSource mDataSource;
    @Before
    public void setUp() {
        mDataSource = new HistoryDataSource(InstrumentationRegistry.getTargetContext());
        mDataSource.open();
    }

    @After
    public void finish() {
        mDataSource.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mDataSource);
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.android.falldetector", appContext.getPackageName());
    }

    @Test
    public void testInsertRecords() {
        assertTrue(mDataSource.insertRec());
    }

    @Test
    public void testDeleteOneRec() {
        mDataSource.deleteRec(1);
        assertTrue(mDataSource.getAllRec().isEmpty());
    }

    @Test
    public void testDeleteAllRec() {
        mDataSource.insertRec();
        mDataSource.insertRec();
        mDataSource.insertRec();
        mDataSource.deleteAll();
        assertTrue(mDataSource.getAllRec().isEmpty());
    }
}
