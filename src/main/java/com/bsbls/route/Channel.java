package com.bsbls.route;

/**
 * Created by bsbls on 4.11.2015.
 */
public class Channel {

    String name;

    public Channel(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void send(String topic, Object object) {
        // System.out.println(getName() + ":::" + topic);
    }
}
