package com.didlink.rest.controllers;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.Application;
import java.util.LinkedHashSet;
import java.util.Set;

public class ControllerFactory extends Application {

    @Inject
    private Instance<Controller> controllers;

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resourceList = new LinkedHashSet<Class<?>>();
        controllers.forEach(resource -> resourceList.add(resource.getClass()));
        return resourceList;
    }
}
