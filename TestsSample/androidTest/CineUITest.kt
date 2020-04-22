package com.kobe.cinephiles

import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.kobe.cinephiles.repository.MovieRepositoryImpl
import com.kobe.cinephiles.repository.retrofit.HttpServiceImpl
import com.kobe.cinephiles.repository.room.CineDatabase
import com.kobe.cinephiles.view.MainActivity
import com.kobe.cinephiles.view.upcoming.UpcomingAdapterWeb
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class CineUITest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private val tagRecyclerWeb = "web"
    private val tagRecyclerFav = "fav"
    private val movieTitleToBeClicked = "My Spy"

    init {
        loadKoinModules(module {

            single(override = true) {
                val retrofit = HttpServiceImpl.getService()

                val db = Room.inMemoryDatabaseBuilder(
                        InstrumentationRegistry.getInstrumentation().context,
                        CineDatabase::class.java)
                    .allowMainThreadQueries()
                    .build()

                MovieRepositoryImpl(retrofit, db.cineDao)
            }
        })
    }

    /*@Before
    fun setUp() {
        activityRule.activity.deleteDatabase(DATABASE_NAME)
    }*/

    @Test
    fun add_favorite_and_remove_favorite() {
        test_add_favorite()
        remove_favorite()
    }

    private fun test_add_favorite() {
        onView(withTagValue(`is`(tagRecyclerWeb as Any)))
            .perform(scrollTo<UpcomingAdapterWeb.VH>(hasDescendant(withText(movieTitleToBeClicked))))

        onView(withTagValue(`is`(tagRecyclerWeb as Any)))
            .perform(actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText(movieTitleToBeClicked)), click()))

        onView(withId(R.id.txtTitleMovie))
            .check(matches(withText(movieTitleToBeClicked)))

        onView(withId(R.id.fabFavorite))
            .perform(click())

        pressBack()

        onView(withId(R.id.viewPager))
            .perform(swipeLeft())

        onView(withTagValue(`is`(tagRecyclerFav as Any)))
            .check(matches(hasDescendant(withText(movieTitleToBeClicked))))

        pressBack()
        pressBack()
    }

    private fun remove_favorite() {
        onView(withId(R.id.viewPager))
            .perform(swipeLeft())

        onView(withTagValue(`is`(tagRecyclerFav as Any)))
            .check(matches(hasDescendant(withText(movieTitleToBeClicked))))

        onView(withTagValue(`is`(tagRecyclerFav as Any)))
            .perform(actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText(movieTitleToBeClicked)), click()))

        onView(withId(R.id.txtTitleMovie))
            .check(matches(withText(movieTitleToBeClicked)))

        onView(withId(R.id.fabFavorite))
            .perform(click())

        onView(withTagValue(`is`(tagRecyclerFav as Any)))
            .check(matches(hasDescendant(withText(movieTitleToBeClicked))))
    }
}