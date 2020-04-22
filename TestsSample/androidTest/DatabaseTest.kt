

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var database: MainDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MainDatabase::class.java).build()
    }
}