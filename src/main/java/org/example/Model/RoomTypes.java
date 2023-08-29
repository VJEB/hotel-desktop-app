package org.example.Model;

public enum RoomTypes {
    NORMAL("Normal"),
    SUITE("Suite"),
    EVENT_ROOM("Event room");

    private final String displayName;

    RoomTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
