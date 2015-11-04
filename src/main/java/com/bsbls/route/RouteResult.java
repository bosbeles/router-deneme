package com.bsbls.route;

import java.util.Collection;
import java.util.List;

/**
 * Created by bsbls on 4.11.2015.
 */
public class RouteResult {
    private boolean exclude;
    private Collection<String> routes;


    public Collection<String> getRoutes() {
        return routes;
    }

    public void setRoutes(Collection<String> routes) {
        this.routes = routes;
    }

    public boolean isExclude() {
        return exclude;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }
}
