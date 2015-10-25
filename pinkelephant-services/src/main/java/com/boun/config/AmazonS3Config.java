package com.boun.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AmazonS3Config {

    /**
     * Bean defining type of credential provider
     * @return currently we use DefaultAWSCredentialsProviderChain
     */
    @Bean
    AWSCredentialsProvider credentialsProvider() {
        return new DefaultAWSCredentialsProviderChain();
    }

    /**
     * Bean creating AmazonS3Client object
     * TODO: Region is currently hardcoded.
     * @param credentialsProvider
     * @return
     */
    @Bean
    public AmazonS3Client s3Client(AWSCredentialsProvider credentialsProvider) {
        return Region.getRegion(Regions.EU_CENTRAL_1).createClient(
                AmazonS3Client.class, credentialsProvider, new ClientConfiguration());
    }

    @Bean
    public AccessControlList accessControlList() {
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        return accessControlList;
    }

}
