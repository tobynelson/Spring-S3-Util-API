package me.tobydev;

import java.util.Map;
import java.util.ArrayList;

public class S3SearchRequest {
    private Map<String,String> metaData;
    private ArrayList<String> tags;
    private S3UtilConstants.META_OP metaOp = S3UtilConstants.DEFAULT_META_OP;
    private S3UtilConstants.TAG_OP tagOp = S3UtilConstants.DEFAULT_TAG_OP;
    private S3UtilConstants.SEARCH_OP searchOp = S3UtilConstants.DEFAULT_SEARCH_OP;

    public Map<String,String> getMetaData() { return this.metaData; }
    public ArrayList<String> getTags() { return this.tags; }

    public S3UtilConstants.META_OP getMetaOp() { return this.metaOp; }
    public S3UtilConstants.TAG_OP getTagOp() { return this.tagOp; }
    public S3UtilConstants.SEARCH_OP getSearchOp() { return this.searchOp; }

}