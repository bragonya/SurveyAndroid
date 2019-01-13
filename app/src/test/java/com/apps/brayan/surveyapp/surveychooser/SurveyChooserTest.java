package com.apps.brayan.surveyapp.surveychooser;

import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.brayan.surveyapp.BuildConfig;
import com.apps.brayan.surveyapp.R;
import com.apps.brayan.surveyapp.coreApp.SurveyConstants;
import com.apps.brayan.surveyapp.models.Survey;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SurveyChooserTest {

    private SurveyChooser activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(SurveyChooser.class)
                .get();
    }

    @Test
    public void activity_show_generic_fallback(){
        activity.showEmptyError();
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

}
