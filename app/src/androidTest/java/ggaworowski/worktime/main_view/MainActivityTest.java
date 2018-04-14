package ggaworowski.worktime.main_view;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ggaworowski.worktime.R;
import ggaworowski.worktime.screens.history.HistoryActivity;
import ggaworowski.worktime.screens.main.MainActivity;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by gregor on 18.03.18.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testStartEndActions() {
        testStartEndClick();
        testStartEndClick();
    }

    @Test
    public void testShowHistory() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.llShowStats)).perform(click());
        intended(hasComponent(HistoryActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testWifiInput() {
        onView(withId(R.id.ivWifiMore)).perform(click());
        onView(withId(R.id.rlWifiInput)).check(matches(isDisplayed()));
        onView(withId(R.id.ivWifiMore)).perform(click());
        onView(withId(R.id.rlWifiInput)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ivWifiMore)).perform(click());
        onView(withId(R.id.rlWifiInput)).check(matches(isDisplayed()));

        onView(withId(R.id.etWifiName)).perform(typeText("asdasd"));
        onView(withId(R.id.ivConfirmWifi)).perform(click());
        onView(withId(R.id.rlWifiInput)).check(matches(not(isDisplayed())));
        onView(withId(R.id.tvWifiStatus)).check(matches(withText(getStringByID(R.string.wifi_on))));

        onView(withId(R.id.ivWifiMore)).perform(click());
        onView(withId(R.id.rlWifiInput)).check(matches(isDisplayed()));

        onView(withId(R.id.ivCancelWifi)).perform(click());
        onView(withId(R.id.etWifiName)).check(matches(withText("")));
        onView(withId(R.id.rlWifiInput)).check(matches(not(isDisplayed())));
        onView(withId(R.id.tvWifiStatus)).check(matches(withText(getStringByID(R.string.wifi_off))));
    }

    private void testStartEndClick() {
        String endString = getStringByID(R.string.end);
        boolean started = ((TextView) mActivityRule.getActivity().findViewById(R.id.tvStartStop)).getText().toString().equals(endString);
        onView(withId(R.id.tvStartStop)).perform(click());
        onView(withId(R.id.tvStartStop)).check(matches(withText(getStringByID(!started ? R.string.end : R.string.start))));
    }

    private String getStringByID(int id){
        return mActivityRule.getActivity().getString(id);
    }

}
