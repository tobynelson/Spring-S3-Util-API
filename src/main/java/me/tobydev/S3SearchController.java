package me.tobydev;

import com.amazonaws.services.s3.model.*;
import org.springframework.web.bind.annotation.*;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class S3SearchController {

    private final int OBJLIMIT = 10;
    private AmazonS3 s3;

    /*
        Find objects whose meta data match a series of k=v pairs
     */
    @PostMapping("/meta/{bucketName}")
    public List<S3UtilObj> metaSearchMulti(@PathVariable String bucketName, @RequestBody S3SearchRequest req) {
        List<S3UtilObj> res = new ArrayList<S3UtilObj>();

        try {
            s3 = AmazonS3ClientBuilder.defaultClient();
            ObjectListing objectListing = s3.listObjects(bucketName);
            res = this.searchBucket(bucketName, objectListing, req);

        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        return res;
    }

    private List<S3UtilObj> searchBucket(String bucketName, ObjectListing objectListing, S3SearchRequest req) {
        List<S3UtilObj> res = new ArrayList<S3UtilObj>();
        int objCount = 0;

        for (Iterator<?> iterator =
             objectListing.getObjectSummaries().iterator();
             iterator.hasNext(); )
        {
            S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
            ObjectMetadata meta = null;
            GetObjectTaggingResult tags = null;
            Map<String, String> searchMeta = req.getMetaData();
            ArrayList<String> searchTags = req.getTags();
            boolean metaValid = false;
            boolean tagValid = false;
            int metaMatches = 0;
            int tagMatches = 0;

            try {
                meta = s3.getObjectMetadata(bucketName, summary.getKey());
                tags = s3.getObjectTagging(new GetObjectTaggingRequest(bucketName, summary.getKey()));
            } catch (AmazonServiceException e){
                System.err.println(e.getErrorMessage());
            }

            if (meta != null && searchMeta != null) {
                Iterator it = searchMeta.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    String val = meta.getUserMetaDataOf((String) pair.getKey());

                    if (val != null && val.equalsIgnoreCase((String) pair.getValue())) {
                        metaMatches++;
                    } else {
                        if (req.getMetaOp() == S3UtilConstants.META_OP.AND) {
                            break;
                        }
                    }
                }
            }

            if (tags != null && searchTags != null) {
                for (int i = 0; i < searchTags.size(); i++) {
                    if (tags.getTagSet().contains(searchTags.get(i))) {
                        tagMatches++;
                    }
                }
            }

            if (searchMeta == null || metaMatches == searchMeta.size() ||
                    (req.getMetaOp() == S3UtilConstants.META_OP.OR && metaMatches > 0))
            {
                metaValid = true;
            }

            if (searchTags == null || tagMatches == searchTags.size() ||
                    (req.getTagOp() == S3UtilConstants.TAG_OP.OR && tagMatches > 0))
            {
                tagValid = true;
            }

            if ((req.getSearchOp() == S3UtilConstants.SEARCH_OP.AND && tagValid && metaValid) ||
                    (req.getSearchOp() == S3UtilConstants.SEARCH_OP.OR && (tagValid || metaValid))
            )
            {
                S3UtilObj s3Obj = new S3UtilObj();
                s3Obj.setTitle(bucketName + " - " + summary.getKey());
                s3Obj.setSummary(summary);
                s3Obj.setDataUrl(s3.getUrl(bucketName, summary.getKey()));
                res.add(s3Obj);
            }

            objCount++;
            if (objCount >= OBJLIMIT) {
                break;
            }
        }

        return res;
    }
}