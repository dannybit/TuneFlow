package com.dannybit.tuneflow.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.test.mock.MockContext;
import android.test.mock.MockResources;
import android.util.DisplayMetrics;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by andy on 8/10/15.
 */
public class MainUtilTest {

    private static final double EPSILON = 0.0001;

    private Context context;
    private Resources resources;

    @Before
    public void setUp() {
        context = new MockContext();
        resources = new MockResources();
    }

    @Test
    public void testConvertDpToPixel() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.densityDpi = 1;

        when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(context.getResources()).thenReturn(resources);

        float pixels = MainUtils.convertDpToPixel(0, context);

        assertEquals(0.393701, pixels, EPSILON);
    }

}
