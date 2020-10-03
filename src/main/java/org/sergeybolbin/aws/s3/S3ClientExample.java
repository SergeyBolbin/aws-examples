package org.sergeybolbin.aws.s3;

import org.sergeybolbin.aws.util.PropertiesBasedCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

public class S3ClientExample {
    private static final Region REGION = Region.EU_CENTRAL_1;
    private static final String JAVA_BUCKET_NAME = "sergeybolbin-java-bucket";

    public static void main(String[] args) {

        try(S3Client client = buildClient()) {
            ListBucketsResponse listBucketsResponse = client.listBuckets(
                    ListBucketsRequest.builder().build()
            );

            boolean javaBucketExists = false;
            for (Bucket bucket : listBucketsResponse.buckets()) {
                if (JAVA_BUCKET_NAME.equals(bucket.name())) {
                    System.out.println(JAVA_BUCKET_NAME + " already exists!");
                    javaBucketExists = true;
                    break;
                }
            }

            if (!javaBucketExists) {
                System.out.println(JAVA_BUCKET_NAME + " does not exist. Creating new one");
                CreateBucketResponse createBucketResponse = client.createBucket(CreateBucketRequest.builder()
                        .bucket(JAVA_BUCKET_NAME)
                        .createBucketConfiguration(CreateBucketConfiguration.builder()
                                .locationConstraint(REGION.id())
                                .build())
                        .build());

                if (createBucketResponse.sdkHttpResponse().isSuccessful()) {
                    System.out.println("Bucket " + JAVA_BUCKET_NAME + " successfully created!");
                } else {
                    System.err.println("Error during bucket creation. Http Code: " +
                            createBucketResponse.sdkHttpResponse().statusCode());
                }

            }
        }
    }

    private static S3Client buildClient() {
        return S3Client.builder()
                .credentialsProvider(PropertiesBasedCredentialsProvider.INSTANCE)
                .region(REGION)
                .build();
    }


}
