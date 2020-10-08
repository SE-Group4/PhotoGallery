package com.example.photogallery;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
<<<<<<< Updated upstream
import android.graphics.drawable.BitmapDrawable;
=======
>>>>>>> Stashed changes
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;

<<<<<<< Updated upstream
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;
=======
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
>>>>>>> Stashed changes

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
<<<<<<< Updated upstream
import static androidx.test.espresso.intent.Intents.intended;
=======
>>>>>>> Stashed changes
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.Intents.intended;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class ImageSearchEspressoTest {
<<<<<<< Updated upstream
    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);
=======

    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    @Rule public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Rule public GrantPermissionRule mRuntimeWritePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//    @Rule public GrantPermissionRule mRuntimeReadPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE);
//    @Rule public GrantPermissionRule mRuntimeLocationPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "_caption_" + timeStamp + "_";
        File storageDir = activityRule.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }
    @Before
    public void createImage() {
        String directoryString = activityRule.getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        OutputStream fOutputStream;

        try {
            Drawable drawable = activityRule.getActivity().getDrawable(R.drawable.ic_launcher_background);
            drawable = (DrawableCompat.wrap(drawable)).mutate();

            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            File directory = new File(directoryString);
            if(!directory.exists()){
                directory.mkdirs();
            }

            File file = createImageFile();
            if (file.exists()) {
                file.delete();
            }

            fOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
>>>>>>> Stashed changes
    @Test
    public void captionTest() throws InterruptedException {
        String captionText = "caption";
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
<<<<<<< Updated upstream
        onView(withId(R.id.btnSearch)).perform(click());
=======
>>>>>>> Stashed changes
        onView(withId(R.id.keyword_search_text)).perform(scrollTo(), clearText(), typeText(captionText), closeSoftKeyboard());
        onView(withId(R.id.date_from_text)).perform(clearText(), typeText("2020-08-21"), closeSoftKeyboard());
        onView(withId(R.id.date_to_text)).perform(clearText(), typeText(currentDate), closeSoftKeyboard());
        onView((withId(R.id.btnApplySearch))).perform(click());
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        onView(withText(captionText)).check(matches(isDisplayed()));
    }

    @Test
    public void cameraTest() {
        Drawable testPic = activityRule.getActivity().getDrawable(R.drawable.ic_launcher_background);
//        Bitmap bitmap = Bitmap.createBitmap(testPic.getIntrinsicWidth(), testPic.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bitmap = BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().getResources(),
                R.mipmap.ic_launcher);
        Intent resultData = new Intent();
        resultData.putExtra("data", bitmap);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Stub out the Camera. When an intent is sent to the Camera, this tells Espresso to respond
        // with the ActivityResult we just created
        intending(toPackage("com.example.photogallery.MainActivity")).respondWith(result);

        // Now that we have the stub in place, click on the button in our app that launches into the Camera
        onView(withId(R.id.btnSnap)).perform(click());

        // We can also validate that an intent resolving to the "camera" activity has been sent out by our app
        intended(toPackage("com.android.camera2"));
    }


}