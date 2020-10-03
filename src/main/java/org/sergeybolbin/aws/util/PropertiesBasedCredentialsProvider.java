package org.sergeybolbin.aws.util;

import org.sergeybolbin.aws.s3.S3ClientExample;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import static software.amazon.awssdk.profiles.ProfileProperty.AWS_ACCESS_KEY_ID;
import static software.amazon.awssdk.profiles.ProfileProperty.AWS_SECRET_ACCESS_KEY;

public class PropertiesBasedCredentialsProvider implements AwsCredentialsProvider {

    private static final Properties PROPS = loadProperties();
    public static PropertiesBasedCredentialsProvider INSTANCE = new PropertiesBasedCredentialsProvider();

    private PropertiesBasedCredentialsProvider() {
    }

    @Override
    public AwsCredentials resolveCredentials() {
        return AwsBasicCredentials.create(PROPS.getProperty(AWS_ACCESS_KEY_ID), PROPS.getProperty(AWS_SECRET_ACCESS_KEY));
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            properties.load(
                    Objects.requireNonNull(
                            S3ClientExample.class.getClassLoader().getResourceAsStream("aws_permissions.properties")
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }
}
