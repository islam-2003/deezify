package be.deezify.services;

import be.deezify.listeners.Listenable;

import java.util.HashSet;
import java.util.Set;

public abstract class Service<L> implements Listenable<L> {

    protected final Set<L> listeners = new HashSet<>();

    public void addListener(L listener) {
        listeners.add(listener);
    }

    public void removeListener(L listener) {
        listeners.remove(listener);
    }

}
