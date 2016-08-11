@RunWith(AndroidJUnit4.class)
@MediumTest
public class EditPostActivityTest
{
    private SourceViewEditText editorTitleView;
    private SourceViewEditText editorContentView;

    @Rule
    public IntentsTestRule<EditPostActivity> activityTestRule =
            new IntentsTestRule<EditPostActivity>(
						EditPostActivity.class,
						false,
						false)
            {
                @Override
                public void beforeActivityLaunched()
                {
                    // Initialize the monitor before each test
                    EventMonitor.getInstance().initialize();
                }

                @Override
                public void afterActivityLaunched()
                {
                    // Add observables used by all tests
                    EventMonitor.getInstance()
							.observe(EventBusObservable.mediaUploadEvents());
                    getEditorViews();
                    EventMonitor.getInstance()
							.observe(EventUtils.postChanges(editorTitleView,
														editorContentView));

                    // Add checks valid for all tests
                    EventMonitor.getInstance().checkThat(
                            "Switched to HTML even if a media item is uploading!",
                            anEventThat(isHtmlToggle())
                            .cannotHappenBetween(
                                anEventThat(isMediaUploadProgress()),
                                anEventThat(isMediaUploadOutcome())));

                    EventMonitor.getInstance().checkThat(
                            "Post content changed after the upload started!",
                            providedThat(
                                existsAnEventThat(isPostUploadStart()))
                            .then(
                                anEventThat(isPostChange())
                                .canHappenOnlyBefore(
                                    anEventThat(isPostUploadStart()))));

                    EventMonitor.getInstance().checkThat(
                            "Clicking on 'publish' didn't perform the expected actions!",
                            atLeast(1).eventsWhereEach(
                                anyEvent(
                                    isPostUploadStart(),
                                    isToastDisplay()))
                            .mustHappenAfter(
                                anEventThat(
											isMenuClick(R.id.menu_save_post))));

                    EventMonitor.getInstance().checkThat(
                            "Media upload wasn't cancelled when editor was removed!",
                            providedThat(
                                exist(
                                    between(
                                        anEventThat(
														isMediaUploadProgress()),
                                        anEventThat(
														isMediaUploadOutcome())),
                                    exactly(1))
                                .eventsWhereEach(
												isFragmentLifecycleEvent(ON_DETACH)))
                            .then(
                                atLeast(1).eventsWhereEach(
												isMediaUploadCancel())
                                .mustHappenAfter(
                                    anEventThat(
													isFragmentLifecycleEvent(
															ON_DETACH)))));

                    EventMonitor.getInstance().checkThat(
                            "Race condition between publish and upload media!",
                            isNotSatisfied(
                                exist(
                                    between(
                                        anEventThat(
														isMediaUploadProgress()),
                                        anEventThat(
                                            anyEvent(
                                                isMediaUploadOutcome(),
                                                isMediaUploadProgress()))),
                                    atLeast(1))
                                .eventsWhereEach(isPostUploadStart())));

                    EventMonitor.getInstance().checkThat(
                            "Media upload progress updates are not sent correctly!",
                            allEventsWhereEach(isMediaUploadProgress())
                            .areOrdered(new Comparator<MediaUploadProgressEvent>()
                            {
                                @Override
                                public int compare(MediaUploadProgressEvent e1, MediaUploadProgressEvent e2)
                                {
                                    return Float.compare(e1.getProgress(), e2.getProgress());
                                }
                            }));
                }

                @Override
                public void afterActivityFinished()
                {
                    // At the end of each test, stop the verification
                    EventMonitor.getInstance().stopVerification();
                }
            };

    @Test
    public void testUploadImage()
    {
        // Start activity and verification, and mock the image selection
        launchActivity();
        startVerification();
        setupCameraResult();

        // Test actions
        onView(withId(R.id.format_bar_button_html))
                .perform(click());

        onView(withId(R.id.sourceview_title))
                .perform(replaceText("My Title"));

        onView(withId(R.id.sourceview_content))
                .perform(replaceText("My Content."));

        onView(withId(R.id.format_bar_button_html))
                .perform(click());

        onView(withId(R.id.format_bar_button_media))
                .perform(click());

        onView(withText(R.string.select_photo))
                .perform(click());

        onView(withContentDescription(R.string.publish_post))
            .perform(click());
    }

    @Test
    public void testPublishError()
    {
        // Start activity
        launchActivity();
        Context context = InstrumentationRegistry.getTargetContext();

        // Add checks specific for this test
        EventMonitor.getInstance().checkThat(
                "The error toast wasn't displayed!",
                exactly(1).eventsWhereEach(
                    isToastDisplay(equalTo(
								context.getString(
										R.string.error_publish_empty_post))))
                .mustHappenAfter(
                    anEventThat(isMenuClick(R.id.menu_save_post))));

        // Start verification
        startVerification();

        // Test actions
        onView(withId(R.id.format_bar_button_html))
                .perform(click());

        onView(withContentDescription(R.string.publish_post))
                .perform(click());
    }


    /*** OTHER TEST CASES ***/





    /************** HELPER METHODS **************/

    private void startVerification()
    {
		// Log events and throw AssertionError if a result fails
        EventMonitor.getInstance().startVerification(
                EventMonitor.getLoggerEventsSubscriber(),
                EventMonitor.getAssertionErrorResultsSubscriber());
    }

    private void launchActivity()
    {
        /* Omitted for simplicity. Launches Activity with correct Intent parameters. */
    }

    private void setupCameraResult()
    {
        /* Omitted for simplicity. Mocks the "select image" operation using Espresso Intents library. */
    }

    private void getEditorViews()
    {
        /* Omitted for simplicity. Retrieves title and content views from the Editor Fragment contained in the Activity. */
    }
}