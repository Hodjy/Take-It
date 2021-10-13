package com.hod.finalapp.model.firebase.enums;

public enum eUserDataTypes
{
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    PASSWORD("password"),
    PICTURE_URL("pictureUrl"),
    TOKEN("token"),
    USER_ID("userId"),
    USERNAME("username");

    public final String name;
    private eUserDataTypes(String name) {
        this.name = name;
    }
}
