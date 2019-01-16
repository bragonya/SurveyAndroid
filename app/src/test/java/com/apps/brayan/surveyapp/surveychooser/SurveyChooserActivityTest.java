package com.apps.brayan.surveyapp.surveychooser;

import android.widget.TextView;

import com.apps.brayan.surveyapp.BuildConfig;
import com.apps.brayan.surveyapp.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SurveyChooserActivityTest {

    private SurveyChooserActivity activity;

    @Before
    public void setUp() {
        ActivityController controller = Robolectric.buildActivity(SurveyChooserActivity.class);
        activity = (SurveyChooserActivity) controller
                .get();
        doNothing().when(activity.getModel()).fetchSurveysByOrganization("",activity);
        controller.create().resume();

    }

    @Test
    public void activity_show_generic_fallback(){
        activity.showGenericError();
        TextView fallbackView = activity.getFallbackContainter().findViewById(R.id.error_message);
        Assert.assertNotNull(fallbackView);
        Assert.assertEquals(fallbackView.getText(),"Ha ocurrido un error");
    }

    @Test
    public void activity_show_empty_fallback_with_mock_data(){
        ArrayList list = mock(ArrayList.class);
        when(list.size()).thenReturn(0);
        activity.showNewDataSet(list);
        TextView fallbackView = activity.getFallbackContainter().findViewById(R.id.error_message);
        Assert.assertNotNull(fallbackView);
        Assert.assertNotNull(fallbackView);
    }

    @Test
    public void activity_remove_fallback(){
        ArrayList list = mock(ArrayList.class);
        when(list.size()).thenReturn(0);
        activity.showNewDataSet(list);
        TextView fallbackView = activity.getFallbackContainter().findViewById(R.id.error_message);
        Assert.assertNotNull(fallbackView);
        Assert.assertNotNull(fallbackView);
        activity.detachFallback();
        TextView refreshFallbackView = activity.getFallbackContainter().findViewById(R.id.error_message);
        Assert.assertNull(refreshFallbackView);

    }

}
