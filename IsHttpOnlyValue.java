package com.kramer.smauthenticator.utility;

public enum IsHttpOnlyValue {

    httponly(true), notsecure(false);
    public static IsHttpOnlyValue isHttpOnlyValue;
    private final boolean isSecure;

    IsHttpOnlyValue(final boolean _secure) {
        this.isSecure = _secure;
    }

    public boolean isSecure() {
        return isSecure;
    }
    public static void setIsHttpOnlyValue(IsHttpOnlyValue _isHttpOnlyValue){
        isHttpOnlyValue= _isHttpOnlyValue;
    }

    public static IsHttpOnlyValue getIsHttpOnlyValue() {
        return isHttpOnlyValue;
    }
}