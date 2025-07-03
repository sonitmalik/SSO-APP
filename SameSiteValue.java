package com.kramer.smauthenticator.utility;
public enum SameSiteValue {
    none(false), notsecure(false);
    public static SameSiteValue sameSiteValue;
    private final boolean isSecure;

    SameSiteValue(final boolean _secure) {
        this.isSecure = _secure;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public static void setSameSiteValue(SameSiteValue sameSiteValue){
        sameSiteValue= sameSiteValue;
    }

    public static SameSiteValue getSameSiteValue() {
        return sameSiteValue;
    }
}

//public enum SameSiteValue {
//    NONE("0"), Strict("1"),Lax("2");
//
//    private final String text;
//
//    SameSiteValue(final String text) {
//        this.text = text;
//    }
//
//    @Override
//    public String toString() {
//        return text;
//    }
//}