package dev.forcetower.cubicrectangle.view

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.ViewPagerActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.TestApplication
import dev.forcetower.cubicrectangle.dagger.DaggerTestComponent
import dev.forcetower.cubicrectangle.testutils.MatcherUtils.firstChildOf
import dev.forcetower.cubicrectangle.testutils.RecyclerItems
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {
    @get:Rule
    val testRule = CountingTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule(HomeActivity::class.java, true, false)

    private lateinit var app: TestApplication

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        app = instrumentation.targetContext.applicationContext as TestApplication
        DaggerTestComponent.builder().application(app).build().inject(app)
    }

    @Test
    fun navigateTheWholeApp() {
        activityRule.launchActivity(null)

        onView(withId(R.id.pager_genres))
            .check(matches(isDisplayed()))
            .perform(swipeLeft())
            .perform(swipeLeft())
            .perform(swipeLeft())
            .perform(ViewPagerActions.scrollToPage(1))

        val action = firstChildOf(withId(R.id.pager_genres))
        val recycler = allOf(isDescendantOfA(action), withId(R.id.recycler_movies))

        onView(recycler)
            .check(RecyclerItems.hasItemCount(31))
            .check(matches(hasDescendant(withText("movie"))))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))

        onView(withId(R.id.details))
            .check(matches(isDisplayed()))

        onView(withId(R.id.title_name))
            .check(matches(withText("Homem-Aranha: De Volta ao Lar")))

        onView(withId(R.id.btn_back))
            .perform(click())

        onView(withId(R.id.btn_search))
            .perform(click())

        onView(withId(R.id.edit_query))
            .perform(click())
            .perform(typeTextIntoFocusedView("Marvel"))
            .check(matches(withText("Marvel")))
            .check(matches(not(withText("DC"))))

        onView(withId(R.id.btn_clear))
            .perform(click())

        onView(withId(R.id.edit_query))
            .check(matches(withText("")))
            .perform(click())
            .perform(typeTextIntoFocusedView("beta"))

        onView(withId(R.id.recycler_movies))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.btn_back))
            .perform(click())

        onView(withId(R.id.edit_query))
            .check(matches(not(withText(""))))
            .check(matches(withText("beta")))

        onView(withId(R.id.btn_back))
            .perform(click())
    }
}
