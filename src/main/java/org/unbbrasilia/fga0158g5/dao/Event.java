package org.unbbrasilia.fga0158g5.dao;

import lombok.Getter;

@Getter
public class Event {

    private Long start;
    private Long end;
    private String eventName;

    public Event(Long entry, Long out, String name){
        this.start = entry;
        this.end = out;
        this.eventName = name;
    }

    public boolean underEvent(Long start, Long end){
        return (start - this.start >= 0 && this.end - end >= 0);
    }

}