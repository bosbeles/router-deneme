package com.bsbls.route;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import javax.swing.*;
import java.util.*;

/**
 * Created by bsbls on 4.11.2015.
 */
public class Main {

    public static void main(String[] args) throws Exception{

        Thread.sleep(1000);

        // test();

        Forwarder forwarder = new Forwarder("routing.xml");


        int CHANNEL_SIZE = 5;
        for (int i = 0; i < CHANNEL_SIZE; i++) {
            forwarder.addChannel(new Channel("C" + i));
        }


        Random rand = new Random();

        long time = System.currentTimeMillis();
        for (int i = 0; i < 50000 ; i++) {
            int r = rand.nextInt(CHANNEL_SIZE);
            forwarder.forward("C" + r, "A" + rand.nextInt(25), new Object());
        }
        time = System.currentTimeMillis() - time;
        System.out.println(time + " ms.");
        System.out.println(forwarder.getRouter().getStats());

    }

    private static void test() throws Exception {
        Router router = new Router();
        router.loadFromXml("routing.xml");

        Collection<String> routes = router.route("C1", "A").getRoutes();
        System.out.println(routes);

        routes = router.route("C1", "B").getRoutes();
        System.out.println(routes);

        routes = router.route("C4", "A").getRoutes();
        System.out.println(routes);

        routes = router.route("C1", "C").getRoutes();
        System.out.println(routes);

        routes = router.route("C2", "C").getRoutes();
        System.out.println(routes);
        routes = router.route("C4", "C").getRoutes();
        System.out.println(routes);
    }
}
