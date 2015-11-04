package com.bsbls.route;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import java.util.*;

/**
 * Created by bsbls on 4.11.2015.
 */
public class Router {


    public static final String ALL = "*";
    Map<String, RouteResult> cache;
    Map<String, HashMap<String, Set<String>>> routeMap = new HashMap<>();
    int stats = 0;


    public Router() {
        cache = new LinkedHashMap<String, RouteResult>(10, 0.75f, true) {
            int cacheSize = 50;

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, RouteResult> eldest) {
                return size() > cacheSize;
            }
        };

    }

    public void loadFromXml(String path) throws Exception {
        cache.clear();
        routeMap.clear();

        HierarchicalConfiguration configuration = new XMLConfiguration(path);
        List<HierarchicalConfiguration> topics = configuration.configurationsAt("topic");
        for (HierarchicalConfiguration topic : topics) {
            String[] topicList = topic.getStringArray("[@id]");
            List<HierarchicalConfiguration> routes = topic.configurationsAt("route");
            for (HierarchicalConfiguration route : routes) {
                String[] srcList = route.getStringArray("[@src]");
                String[] dstList = route.getStringArray("[@dst]");
                for (String topicId : topicList) {
                    for (String src : srcList) {
                        addRoute(topicId, src, Arrays.asList(dstList));
                    }
                }
            }
        }
    }

    public void addRoute(String topic, String source, Collection<String> destination) {
        HashMap<String, Set<String>> topicMap = routeMap.get(topic);
        if (topicMap == null) {
            topicMap = new HashMap<>();
            routeMap.put(topic, topicMap);
        }
        Set<String> destinations = topicMap.get(source);
        if (destinations == null) {
            destinations = new HashSet<>();
            topicMap.put(source, destinations);
        }
        if (destination == null || destination.isEmpty()) {
            destinations.clear();
        } else {
            destinations.addAll(destination);
        }

    }

    public RouteResult route(String source, String topic) {

        String key = source + "," + topic;
        RouteResult routeResult = cache.get(key);
        if (routeResult == null) {
            Collection<String> result = getDestinations(source, topic);

            routeResult = new RouteResult();
            routeResult.setRoutes(result);

            cache.put(key, routeResult);
            stats++;
        }

        return routeResult;
    }

    public int getStats() {
        return stats;
    }

    private Collection<String> getDestinations(String source, String topic) {
        Collection<String> result = null;
        HashMap<String, Set<String>> ownTopicMap = routeMap.get(topic);
        HashMap<String, Set<String>> allTopicMap = routeMap.get(ALL);

        if(ownTopicMap != null) {
            result = ownTopicMap.get(source);
            if(result == null) {
                result = ownTopicMap.get(ALL);
            }
        }

        if(result == null && allTopicMap != null) {
            result = allTopicMap.get(source);
            if(result == null) {
                result = allTopicMap.get(ALL);
            }
        }
        if(result == null) {
            result = Collections.<String>emptySet();
        }
        return result;
    }

}
