package com.fwisniewski.pokemonfinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fwisniewski.pokemonfinder.repository.model.Pokemon
import com.fwisniewski.pokemonfinder.repository.model.PokemonType
import com.fwisniewski.pokemonfinder.repository.model.User
import com.fwisniewski.pokemonfinder.repository.room.PokemonDao
import com.fwisniewski.pokemonfinder.repository.room.PokemonDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PokemonDatabaseTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao: PokemonDao
    private lateinit var db: PokemonDatabase

    private val anUser = User(userId = 1, name = "Felipe", type = "normal")

    private val aTypeTest = PokemonType(name = "normal", thumbnailImage = "https://...")

    private val aPokemonTest = Pokemon(id = 1L, name = "Pikachu", detailPageURL = "https://",
        weight = 1f, height = 1f, number = "1", collectibles_slug = "a", featured = "a", slug = "a",
        thumbnailAltText = "a", thumbnailImage = "a", abilities = listOf("a", "b"),
        weakness = listOf("c", "d"), type = listOf("normal"))

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, PokemonDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = db.pokemonDao
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun crud_user() {
        insertUserDb()
        loadUserDb()
        deleteUserDb()
    }

    private fun insertUserDb() {
        dao.saveUser(anUser)
    }

    private fun loadUserDb() {
        val userDb = LiveDataTestUtil.getValue(dao.loadUser(1))
        assertEquals(anUser.name, userDb.name)
    }

    private fun deleteUserDb() {
        dao.deleteUser(anUser)
        val userNull = LiveDataTestUtil.getValue(dao.loadUser(1))
        assertEquals(null, userNull)
    }

    @Test
    @Throws(Exception::class)
    fun insert_and_get_types() {
        val typeOne = aTypeTest.copy(name = "Test1")
        val typeTwo = aTypeTest.copy(name = "Test2")
        val typeTree = aTypeTest.copy(name = "Test3")
        val aTypes = listOf(typeOne, typeTwo, typeTree)

        dao.saveTypes(aTypes)

        val typesDb = LiveDataTestUtil.getValue(dao.loadTypes())

        assertEquals(aTypes.size, typesDb.size)
    }

    @Test
    @Throws(Exception::class)
    fun insert_replace_repeated_types() {
        val typeOne = aTypeTest.copy(name = "Test1")
        val typeTwo = aTypeTest.copy(name = "Test1")
        val typeTree = aTypeTest.copy(name = "Test1")
        val aTypes = listOf(typeOne, typeTwo, typeTree)

        dao.saveTypes(aTypes)

        val typesDb = LiveDataTestUtil.getValue(dao.loadTypes())

        assertEquals(1, typesDb.size)
    }

    @Test
    @Throws(Exception::class)
    fun insert_and_search_pokemons() {
        val pokeOne = aPokemonTest.copy(id = 101, type = listOf("normal","fighting"))
        val pokeTwo = aPokemonTest.copy(id = 102, type = listOf("poison"))
        val pokeTree = aPokemonTest.copy(id = 103, type = listOf("normal","rock"))
        val aPokemons = listOf(pokeOne, pokeTwo, pokeTree)

        dao.savePokemons(aPokemons)

        val pokemonsTypeNormal = LiveDataTestUtil.getValue(dao.searchPokemons("%normal%", "%%", true))
        assertEquals(2, pokemonsTypeNormal.size)

        val pokemonsTypeFighting = LiveDataTestUtil.getValue(dao.searchPokemons("%fighting%", "%%", true))
        assertEquals(1, pokemonsTypeFighting.size)

        val pokemonsTypePoison = LiveDataTestUtil.getValue(dao.searchPokemons("%poison%", "%%", true))
        assertEquals(1, pokemonsTypePoison.size)

        val pokemonsTypeRock = LiveDataTestUtil.getValue(dao.searchPokemons("%rock%", "%%", true))
        assertEquals(1, pokemonsTypeRock.size)
    }
}