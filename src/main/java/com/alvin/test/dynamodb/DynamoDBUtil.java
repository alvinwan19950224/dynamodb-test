package com.alvin.test.dynamodb;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class DynamoDBUtil {

    public static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("REGION")
            .withCredentials(new AWSStaticCredentialsProvider(new AWSCredentials() {
        @Override
        public String getAWSAccessKeyId() {
            return "ACCESS KEY ID";
        }

        @Override
        public String getAWSSecretKey() {
            return "SECRET KEY";
        }
    })).build();
    public static final DynamoDB dynamoDB = new DynamoDB(client);
    public static final String TABLE_NAME = "TABLE NAME";
}
