package com.timecat.component.data.model.habit;

import java.util.LinkedList;
import java.util.List;

/**
 * A ModelObservable allows objects to subscribe themselves to it and receive
 * notifications whenever the model is changed.
 */
public class ModelObservable {
    private List<Listener> listeners;

    /**
     * Creates a new ModelObservable with no listeners.
     */
    public ModelObservable() {
        super();
        listeners = new LinkedList<>();
    }

    /**
     * Adds the given listener to the observable.
     *
     * @param l the listener to be added.
     */
    public void addListener(Listener l) {
        listeners.add(l);
    }

    /**
     * Notifies every listener that the model has changed.
     * <p>
     * Only models should call this method.
     */
    public void notifyListeners() {
        for (Listener l : listeners) l.onModelChange();
    }

    /**
     * Removes the given listener.
     * <p>
     * The listener will no longer be notified when the model changes. If the
     * given listener is not subscribed to this observable, does nothing.
     *
     * @param l the listener to be removed
     */
    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    /**
     * Interface implemented by objects that want to be notified when the model
     * changes.
     */
    public interface Listener {
        /**
         * Called whenever the model associated to this observable has been
         * modified.
         */
        void onModelChange();
    }
}
