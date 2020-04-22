package com.kobe.cinephiles

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kobe.cinephiles.model.Genre
import com.kobe.cinephiles.model.UpcomingMovie
import com.kobe.cinephiles.repository.room.CineDao
import com.kobe.cinephiles.repository.room.CineDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CineDatabaseTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cineDao: CineDao
    private lateinit var db: CineDatabase

    private val aMovie = UpcomingMovie(id = 101, title = "The Kobe", poster_path = "https://",
        backdrop_path = "", overview = "Test Movie", genre_ids = emptyList(), vote_average = 7.1f,
        vote_count = 1, release_date = "2020/03/18")

    private val aListGenre = listOf(Genre(id=12, name="Adventure"),
        Genre(id=16, name="Animation"), Genre(id=28, name="Action"))

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, CineDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        cineDao = db.cineDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insert_and_get_movie_to_favorite() {
        cineDao.saveFavorite(aMovie)
        val movie: UpcomingMovie? = cineDao.movieById(aMovie.id)
        assertEquals(aMovie.id, movie?.id)
    }

    @Test
    @Throws(Exception::class)
    fun insert_and_delete_movie() {
        cineDao.saveFavorite(aMovie)
        cineDao.deleteFavorite(aMovie)

        val movie: UpcomingMovie? = cineDao.movieById(aMovie.id)

        assertEquals(null, movie)
    }

    @Test
    @Throws(Exception::class)
    fun insert_a_list_of_genre_and_get_the_list() {
        cineDao.saveGenre(aListGenre)

        val list = LiveDataTestUtil.getValue(cineDao.loadGenre())

        assertEquals(aListGenre, list)
    }
}