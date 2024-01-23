package com.example.kotlinpractice

import android.os.Handler
import android.os.Looper
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingResourceTimeoutException
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.kotlinpractice.*
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import org.junit.Assert.*
import kotlin.coroutines.*




/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.kotlinpractice", appContext.packageName)



    }
    @Test
    fun testLogin() {

        onView(withId(R.id.editTextTextEmailAddressForLogin)).perform(typeText("user@email.dk"))
        onView(withId(R.id.editTextTextPasswordForLogin)).perform(typeText("123456"))
        onView(withId(R.id.buttonLogin)).perform(click())
        //Thread.sleep(50) Thread.sleep sleeps the app, not the test.

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            // Perform the next action or assertions after a delay
            onView(withId(R.id.button_sort)).check(matches(withText("sort")))
        }, 3000)
        //onView(withId(R.id.button_sort)).check(matches(withText(("sort"))))



    }
}