package grabber.workers;

/**
 * Created by nikita on 26.03.14.
 */
public interface Pushable<T> {
    public void push(T object);
}
