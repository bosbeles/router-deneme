package com.bsbls.route;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bsbls on 4.11.2015.
 */
public class Forwarder {


    private static final String ALL = "*";

    Router router = new Router();

    Map<String, Channel> channelMap = new HashMap<>();

    public Forwarder(String path) {
        try {
            router.loadFromXml(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Router getRouter() {
        return router;
    }

    public void forward(String source, String topic, Object data) {
        Collection<String> routes = router.route(source, topic).getRoutes();
        if(routes.contains(ALL)) {
            Collection<Channel> channels = channelMap.values();
            for (Channel channel : channels) {
                channel.send(topic, data);
            }

        }
        else {
            for (String route : routes) {
                Channel channel = channelMap.get(route);
                if(channel != null) {
                    channel.send(topic, data);
                }
            }
        }

    }

    public void addChannel(Channel channel) {
        channelMap.put(channel.getName(), channel);
    }
}
