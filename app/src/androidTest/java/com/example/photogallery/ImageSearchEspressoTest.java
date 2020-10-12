package com.example.photogallery;

import android.Manifest;
import android.content.res.AssetManager;
import android.os.Environment;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.GrantPermissionRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.anything;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class ImageSearchEspressoTest {
    @Rule
    public ActivityScenarioRule activityRule = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule readRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule locationRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void createTestImage() throws IOException {
        String[] filenames = getInstrumentation().getContext().getAssets().list("testimgs");
        AssetManager assets = getInstrumentation().getContext().getAssets();
        File downloadFolder = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString());
        if (!downloadFolder.exists())
            downloadFolder.mkdir();
        assert filenames != null;

        for (String filename : filenames) {
            InputStream testImg = assets.open("testimgs/" + filename);
            File f = new File(downloadFolder + "/" + filename);
            FileOutputStream fo = new FileOutputStream(f);
            copyFile(testImg, fo);
            testImg.close();
            testImg = null;
            fo.flush();
            fo.close();
            fo = null;
        }
    }

    @Test
    public void dateTest() {
        String captionText = "caption";
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lat = "49.220509";
        String lng = "-123.007111";

        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.keyword_search_text)).perform(scrollTo(), clearText(), typeText(captionText), closeSoftKeyboard());
        onView(withId(R.id.date_from_text)).perform(clearText(), typeText("2020-08-21"), closeSoftKeyboard());
        onView(withId(R.id.date_to_text)).perform(clearText(), typeText(currentDate), closeSoftKeyboard());
        onView(withId(R.id.lat_text)).perform(clearText(), typeText(lat), closeSoftKeyboard());
        onView(withId(R.id.lng_text)).perform(clearText(), typeText(lng), closeSoftKeyboard());
        onView((withId(R.id.btnApplySearch))).perform(click());
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        onView(withText(captionText)).check(matches(isDisplayed()));
    }

    @Test
    public void locationTest() throws InterruptedException {

        String wrongLat = "38.220510";
        String wrongLng = "-122.007110";
        String captionText = "caption";

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.keyword_search_text)).perform(scrollTo(), clearText(), typeText(captionText), closeSoftKeyboard());
        onView(withId(R.id.date_from_text)).perform(clearText(), typeText("2020-08-21"), closeSoftKeyboard());
        onView(withId(R.id.date_to_text)).perform(clearText(), typeText(currentDate), closeSoftKeyboard());
        onView(withId(R.id.lat_text)).perform(clearText(), typeText(wrongLat), closeSoftKeyboard());
        onView(withId(R.id.lng_text)).perform(clearText(), typeText(wrongLng), closeSoftKeyboard());
        onView((withId(R.id.btnApplySearch))).perform(click());

        onView(withId(R.id.gridView)).check(matches(hasChildCount(0)));

    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}