public Check canHappenOnlyBetween(final AnEventThat eventBefore, final AnEventThat eventAfter)
{
	return new Check(
			"Every event that "+getMatcher()+" happens only between a pair of events where the first "+eventBefore.getMatcher()+" and the second "+eventAfter.getMatcher(),

			new CheckSubscriber()
			{
				private final static int OUTSIDE_PAIR = 0;
				private final static int INSIDE_PAIR = 1;
				private final static int FOUND_E1_OUTSIDE = 2;

				private final State state = new State(OUTSIDE_PAIR);

				private boolean foundAtLeastOneE1 = false;
				private int eventsInCurrentPair = 0;

				@Override
				public void onNext(Event event)
				{
					switch(state.getState())
					{
						case OUTSIDE_PAIR:

							if(getMatcher().matches(event))
							{
								state.setState(FOUND_E1_OUTSIDE);
								state.setEvents(event);
								endCheck();
							}

							else if(eventBefore.getMatcher().matches(event))
							{
								state.setState(INSIDE_PAIR);
							}

							break;

						case INSIDE_PAIR:

							if(eventAfter.getMatcher().matches(event))
							{
								state.setState(OUTSIDE_PAIR);
								eventsInCurrentPair = 0;
							}

							else if(getMatcher().matches(event))
							{
								foundAtLeastOneE1 = true;
								eventsInCurrentPair++;
							}

							break;
					}
				}

				@NonNull
				@Override
				public Result getFinalResult()
				{
					Outcome outcome = null;
					String report = null;

					if(state.getState()==INSIDE_PAIR && eventsInCurrentPair<=0)
					{
						state.setState(OUTSIDE_PAIR);
					}

					switch(state.getState())
					{
						case OUTSIDE_PAIR:

							if(foundAtLeastOneE1)
							{
								outcome = Outcome.SUCCESS;
								report = "Every event that "+getMatcher()+" was found inside a pair";
							}

							else
							{
								outcome = Outcome.WARNING;
								report = "No event that "+getMatcher()+" was found in the sequence";
							}

							break;

						case INSIDE_PAIR:

							outcome = Outcome.FAILURE;
							report = "At the end of the stream, "+eventsInCurrentPair+" events that "+getMatcher()+" were found but no event that "+eventAfter.getMatcher()+" was there to close the pair";

							break;

						case FOUND_E1_OUTSIDE:

							outcome = Outcome.FAILURE;
							report = "Event "+state.getEvent(0)+" was found outside a pair";

							break;
					}

					return new Result(outcome, report);
				}
			}
	);
}