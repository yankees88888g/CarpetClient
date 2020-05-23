package carpetclient.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class BiObservable<T, U> implements IObservable<T, BiConsumer<T, U>>
{
    private List<BiConsumer<T, U>> subscribers;

    protected BiObservable()
    {
        subscribers = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public void notifySubscribers(U value)
    {
        if (subscribers != null)
            subscribers.forEach((r) -> r.accept((T)this, value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T subscribe(BiConsumer<T, U> subscriber)
    {
        if ( subscribers == null)
            subscribers = new ArrayList<>();

        subscribers.add(subscriber);
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T unsubscribe(BiConsumer<T, U> subscriber)
    {
        if (subscribers.contains(subscriber))
            subscribers.remove(subscriber);

        return (T)this;
    }
}
