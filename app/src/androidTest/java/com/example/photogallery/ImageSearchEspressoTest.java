package com.example.photogallery;

import android.icu.text.SimpleDateFormat;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import java.util.Date;
import java.util.Locale;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ImageSearchEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Test
    public void dateTest() throws InterruptedException {
        String captionText = "caption";
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lat = "49.220509";
        String lng = "-123.007111";

        onView(withId(R.id.btnSearch)).perform(click());
        Thread.sleep(500);

        onView(withId(R.id.keyword_search_text)).perform(scrollTo(), clearText(), typeText(captionText), closeSoftKeyboard());
        onView(withId(R.id.date_from_text)).perform(clearText(), typeText("2020-08-21"), closeSoftKeyboard());
        onView(withId(R.id.date_to_text)).perform(clearText(), typeText(currentDate), closeSoftKeyboard());
        onView(withId(R.id.lat_text)).perform(clearText(), typeText(lat), closeSoftKeyboard());
        onView(withId(R.id.lng_text)).perform(clearText(), typeText(lng), closeSoftKeyboard());
        onView((withId(R.id.btnApplySearch))).perform(click());
        Thread.sleep(500);

        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        Thread.sleep(500);
        onView(withText(captionText)).check(matches(isDisplayed()));
    }

     @Test
     public void locationTest() throws InterruptedException {
         String wrongLat = "38.220510";
         String wrongLng = "-122.007110";
         String captionText = "caption";

         String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

         onView(withId(R.id.btnSearch)).perform(click());
         Thread.sleep(500);
         onView(withId(R.id.keyword_search_text)).perform(scrollTo(), clearText(), typeText(captionText), closeSoftKeyboard());
         onView(withId(R.id.date_from_text)).perform(clearText(), typeText("2020-08-21"), closeSoftKeyboard());
         onView(withId(R.id.date_to_text)).perform(clearText(), typeText(currentDate), closeSoftKeyboard());
         onView(withId(R.id.lat_text)).perform(clearText(), typeText(wrongLat), closeSoftKeyboard());
         onView(withId(R.id.lng_text)).perform(clearText(), typeText(wrongLng), closeSoftKeyboard());
         onView((withId(R.id.btnApplySearch))).perform(click());
         Thread.sleep(500);

         onView(withId(R.id.gridView)).check(matches(hasChildCount(0)));
     }
}