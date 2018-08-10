package me.tobydev;


public class S3UtilConstants {
    public static final SEARCH_OP DEFAULT_SEARCH_OP = SEARCH_OP.AND;
    public static final META_OP DEFAULT_META_OP = META_OP.AND;
    public static final TAG_OP DEFAULT_TAG_OP = TAG_OP.AND;

    public enum META_OP {OR, AND};
    public enum TAG_OP {OR, AND};
    public enum SEARCH_OP {OR, AND};
}