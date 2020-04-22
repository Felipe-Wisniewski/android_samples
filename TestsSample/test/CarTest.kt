class CarTest {

    private lateinit var car: Car

    @Before
    fun setUp() {
        car = Car()
    }

    @Test
    fun `Should increase speed given an acceleration and time`() {
        car.accelerate(newAcceleration = 5, timeWindow = 3)

        val currentSpeed = car.getCurrentSpeed()

        assertEquals(15, currentSpeed)
    }
}