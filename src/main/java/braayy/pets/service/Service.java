package braayy.pets.service;

import braayy.pets.Pets;

public abstract class Service {

    protected final Pets plugin;

    public Service(Pets plugin) {
        this.plugin = plugin;
    }

    public abstract void enable();

    public abstract void disable();

}