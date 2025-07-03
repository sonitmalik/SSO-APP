package com.kramer.smauthenticator.utility;

public enum IsSecureValue {
    secure(true), notsecure(false);
    public static IsSecureValue isSecureValue;
    private final boolean isSecure;

    IsSecureValue(final boolean _secure) {
        this.isSecure = _secure;
    }

    public static void setIsSecureValue(IsSecureValue _isSecureValue) {
        isSecureValue = _isSecureValue;
    }

    public static IsSecureValue getIsSecureValue() {
        return isSecureValue;
    }

    public boolean isSecure() {
        return isSecure;
    }

}