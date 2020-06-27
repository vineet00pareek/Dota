package gg.bayes.challenge.utils;

/**
 * Holds all events of the match
 * 
 * @author VineetPareek
 *
 */
public enum Event {

    /**
     * This is casts events for hero to spells.
     */
    CASTS("casts"),
    /**
     * This is buys event for hero to purchase items.
     */
    BUYS("buys"),
    /**
     * This is hits event for hero to damage other hero.
     */
    HITS("hits"),
    /**
     * This is killed event for hero to kill other hero.
     */
    KILLED("killed"),

    /**
     * This is uses event for hero items.
     */
    USES("uses");

    /**
     * This variable holds events;
     */
    private String event;

    /**
     * To initialization events.
     * 
     * @param event the event has done by Heroes
     */
    private Event(String event) {
        this.event = event;
    }

    /**
     * Used to get events
     * 
     * @return {@link String}
     */
    public String getEvent() {
        return event;
    }
}
