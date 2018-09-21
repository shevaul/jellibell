package com.candibell.dal;

import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "PLACEHOLDER_PASTSESSIONS_TABLE_NAME")
public class M_PastSession {

	// get the table name from env. var. set in serverless.yml
    private static final String PASTSESSIONS_TABLE_NAME = System.getenv("PAST_SESSIONS_TABLE_NAME");
    
    private static DynamoDBAdapter db_adapter;
    private static AmazonDynamoDB client;
    private static DynamoDBMapper mapper;
    
    private static Logger logger = Logger.getLogger(M_PastSession.class);
    
    static {
    	DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
    			.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(PASTSESSIONS_TABLE_NAME))
    			.build();
    	// get the db adapter
    	db_adapter = DynamoDBAdapter.getInstance();
    	client = db_adapter.getDbClient();
    	// create the mapper with config
    	mapper = db_adapter.createDbMapper(mapperConfig);
    }
    
    public M_PastSession() {
    }
    
    // methods
    public Boolean ifTableExists() {
        return client.describeTable(PASTSESSIONS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
    }
}
