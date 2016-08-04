public class EventBusObservable
{
    public static Observable<BusEvent> mediaUploadEvents()
	{
        return Observable.create(new MediaUploadOnSubscribe()).onBackpressureDrop();
    }
}

abstract class EventBusSubscriber
{
    EventBusSubscriber()
	{
        EventBus.getDefault().register(this);
    }
}

class MediaUploadOnSubscribe implements Observable.OnSubscribe<BusEvent>
{
    @Override
    public void call(final Subscriber<? super BusEvent> subscriber)
	{
        new EventBusSubscriber()
		{
            public void onEventMainThread(MediaEvents.MediaUploadSucceeded event)
			{
                if(!subscriber.isUnsubscribed())
				{
                    subscriber.onNext(new MediaUploadSuccessEvent());
                }
            }

            public void onEventMainThread(MediaEvents.MediaUploadFailed event)
			{
                if(!subscriber.isUnsubscribed())
				{
                    subscriber.onNext(new MediaUploadFailureEvent());
                }
            }

            public void onEventMainThread(MediaEvents.MediaUploadProgress event)
			{
                if(!subscriber.isUnsubscribed())
				{
                    subscriber.onNext(new MediaUploadProgressEvent(event.mProgress));
                }
            }
        };
    }
}