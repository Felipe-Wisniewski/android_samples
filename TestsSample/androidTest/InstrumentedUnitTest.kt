@RunWith(AndroidJUnit4.class)
public class InstrumentedUnitTest {
    
 private final String prefName = "TEST_PREF";
    
 @Test
 public void test_sharedPref(){
     //No AndroidX podemos utilizar o ApplicationProvider no lugar do InstrumentationRegistry
     //val context = ApplicationProvider.getApplicationContext<Context>()
     Context mContext = InstrumentationRegistry.getContext();
     SharedPreferences mSharedPreferences = mContext.getSharedPreferences(prefName,Context.MODE_PRIVATE);
     
     SharedPreferences.Editor editor = mSharedPreferences.edit();
     editor.putString("key", "value");
     editor.apply();
     
     SharedPreferences mSharedPreferences2 = mContext.getSharedPreferences(prefName, Context.MODE_PRIVATE);
     assertThat("value", is(mSharedPreferences2.getString("key", null)));
}