package be.deezify.listeners;

/**
 * Generic interface for observable/listenable objects.
 *
 * @param <T> The type of listener.
 */
public interface Listenable<T> {

    /**
     * Adds a listener to the object.
     *
     * @param listener The listener to add.
     */
    void addListener(T listener);

    /**
     * Removes a listener from the object.
     *
     * @param listener The listener to remove.
     */
    void removeListener(T listener);

}
