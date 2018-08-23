package com.candibell.dal;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "PLACEHOLDER_USERDEVICESETAG_TABLE_NAME")
public class M_UserDevicesEtag {
	
	private static final String USERDEVICESETAG_TABLE_NAME = System.getenv("USER_DEVICES_ETAG_TABLE_NAME");
	
	private static DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    @SuppressWarnings("static-access")
    public M_UserDevicesEtag() {
    	DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(USERDEVICESETAG_TABLE_NAME))
                .build();
            // get the db adapter
            this.db_adapter = DynamoDBAdapter.getInstance();
            this.client = this.db_adapter.getDbClient();
            // create the mapper with config
            this.mapper = this.db_adapter.createDbMapper(mapperConfig);
    }
    
    // methods
    public Boolean ifTableExists() {
        return this.client.describeTable(USERDEVICESETAG_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
    }
    
    private String userId;
    private String etag; 
    
    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
    	return this.userId;
    }
    public M_UserDevicesEtag setUserId(String userId) {
    	this.userId = userId;
    	return this;
    }
    
    @DynamoDBAttribute(attributeName = "etag")
    public String getEtag() {
    	return this.etag;
    }
    public M_UserDevicesEtag setEtag(String etag) {
    	this.etag = etag;
    	return this;
    }
    
    public void update() throws IOException {
   	 logger.info("User - addRegisterDevice(): " + this.toString());
   	 this.mapper.save(this);
    }
}
