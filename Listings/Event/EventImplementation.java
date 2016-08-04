public class HtmlToggleEvent extends Event
{
    private boolean active;

    public HtmlToggleEvent(boolean active)
	{
        this.active = active;
    }

    public static Matcher<HtmlToggleEvent> isHtmlToggle()
	{
        return new FeatureMatcher<HtmlToggleEvent, Void>(anything(""), "is any HTML toggle event", "")
		{
            @Override
            protected Void featureValueOf(final HtmlToggleEvent actual)
			{
                return null;
            }
        };
    }

    public static Matcher<HtmlToggleEvent> isHtmlToggle(final boolean active)
	{
        return new FeatureMatcher<HtmlToggleEvent, Boolean>(equalTo(active), active ? "is an HTML mode activation" : "is an HTML mode deactivation", "")
		{
            @Override
            protected Boolean featureValueOf(final HtmlToggleEvent actual)
			{
                return actual.active;
            }
        };
    }

    @Override
    public String toString()
	{
        return active ? "{HTML mode activated}" : "{HTML mode deactivated}";
    }
}