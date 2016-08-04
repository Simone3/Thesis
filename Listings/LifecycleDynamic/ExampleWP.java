@RunWith(AndroidJUnit4.class)
@MediumTest
public class MyProfileActivityTest extends ActivityRuleLifecycleTest<MyProfileActivity>
{
    @Override
    protected ActivityTestRule<MyProfileActivity> getActivityTestRule()
    {
        return new ActivityTestRule<>(MyProfileActivity.class);
    }

    @Nullable
    @Override
    public RotationCallback testRotation()
    {
        return new RotationCallback()
        {
            private String name;

            @Override
            public void beforeRotation()
            {
                // Open first name dialog
                onView(withId(R.id.first_name_row))
                        .check(matches(isDisplayed()))
                        .perform(click());

                // Type name
                name = "MyFirstName"+(new Random().nextInt(100));
                onView(withId(R.id.my_profile_dialog_input))
                        .check(matches(isDisplayed()))
                        .perform(replaceText(name));

                // Confirm
                onView(withText("OK"))
                        .perform(click());

                // Check name in the button
                onView(withId(R.id.first_name))
                        .check(matches(allOf(isDisplayed(), withText(name))));
            }

            @Override
            public void afterRotation()
            {
                // Check name in the button
                onView(withId(R.id.first_name))
                        .check(matches(allOf(isDisplayed(), withText(name))));
            }
        };
    }
}