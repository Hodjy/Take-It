package com.hod.finalapp.model.enums;

public enum eStorageFolders {
    USERS_PROFILE_PICTURES("users_profile_pictures"),
    ITEMS_PICTURES("items_pictures");

    public final String name;
    private eStorageFolders(String name) {
        this.name = name;
    }
}
