package com.candibell.dal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.candibell.common.Constant;

@DynamoDBTable(tableName = "PLACEHOLDER_DEVICES_TABLE_NAME")
public class M_Device {

	// get the table name from env. var. set in serverless.yml
    private static final String DEVICES_TABLE_NAME = System.getenv("DEVICES_TABLE_NAME");
    
    private static DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    private Logger logger = Logger.getLogger(this.getClass());

    private String id;
    private String userId;
    private String deviceType;
    private String deviceName;
    private String createdTime;
    private String lastUpdatedTime;
    
    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return this.id;
    }
    public M_Device setId(String id) {
    	if (id != null && !id.isEmpty()) {
    		this.id = id.toUpperCase(Constant.DEFAULT_LOCATE);
    	}
        return this;
    }
    
    @DynamoDBRangeKey(attributeName = "userId")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "userIdIndex")
    public String getUserId() {
    	return this.userId;
    }
    public M_Device setUserId(String userId) {
    	this.userId = userId;
    	return this;
    }
    
    @DynamoDBAttribute(attributeName = "deviceType")
    public String getDeviceType() {
    	return this.deviceType;
    }
    public M_Device setDeviceType(String deviceType) {
    	this.deviceType = deviceType;
    	return this;
    }
    
    @DynamoDBAttribute(attributeName = "deviceName")
    public String getDeviceName() {
    	return this.deviceName;
    }
    public M_Device setDeviceName(String deviceName) {
    	this.deviceName = deviceName;
    	return this;
    }
    
    @DynamoDBAttribute(attributeName = "createdTime")
    @DynamoDBAutoGeneratedTimestamp(strategy=DynamoDBAutoGenerateStrategy.CREATE)
    public String getCreatedTime() {
    	return this.createdTime;
    }
    public M_Device setCreatedTime(String createdTime) {
    	this.createdTime = createdTime;
    	return this;
    }
    
    @DynamoDBAttribute(attributeName = "lastUpdatedTime")
    @DynamoDBAutoGeneratedTimestamp(strategy=DynamoDBAutoGenerateStrategy.ALWAYS)
    public String getLastUpdatedTime() {
    	return this.lastUpdatedTime;
    }
    public M_Device setLastUpdatedTime(String lastUpdatedTime) {
    	this.lastUpdatedTime = lastUpdatedTime;
    	return this;
    }
    
    @SuppressWarnings("static-access")
	public M_Device() {
        // build the mapper config
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
            .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(DEVICES_TABLE_NAME))
            .build();
        // get the db adapter
        this.db_adapter = DynamoDBAdapter.getInstance();
        this.client = this.db_adapter.getDbClient();
        // create the mapper with config
        this.mapper = this.db_adapter.createDbMapper(mapperConfig);
    }
    
	@Override
	public String toString() {
		return "Device [client=" + client + ", mapper=" + mapper + ", logger=" + logger + ", id=" + id + ", userId="
				+ userId + ", deviceType=" + deviceType + ", deviceName=" + deviceName + ", createdTime=" + createdTime
				+ ", lastUpdatedTime=" + lastUpdatedTime + "]";
	}
    
	// methods
    public Boolean ifTableExists() {
        return this.client.describeTable(DEVICES_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
    }
    
    public M_Device getDeviceById(String id) throws IOException {
    	M_Device device = null;
    	
    	Map<String, AttributeValue> av = new HashMap<>(2);
    	av.put(":v1", new AttributeValue().withS(id));
    	
    	DynamoDBQueryExpression<M_Device> queryExp = new DynamoDBQueryExpression<M_Device>()
    	.withKeyConditionExpression("id = :v1")
    	.withExpressionAttributeValues(av);
    	PaginatedQueryList<M_Device> result = this.mapper.query(M_Device.class, queryExp);
    	if (result.size() == 1) {
    		device = result.get(0);
    		logger.info("Devices - getDeviceById(" + id + "): device - " + device.toString());
    	} else if (result.size() > 1) {
    		logger.warn("Devices - getDeviceById(" + id + "): Unexpexted size - " + result.size());
    	} else {
    		logger.info("Devices - getDeviceById(" + id + "): device - Not Found.");
    	}
    	
    	return device;
    }
    
    public List<M_Device> getDevicesByUserId(String userId) throws IOException {
    	List<M_Device> devices = new ArrayList<>(0);

    	this.setUserId(userId);
    	final DynamoDBQueryExpression<M_Device> queryExp = new DynamoDBQueryExpression<>();
    	queryExp.setHashKeyValues(this);
    	queryExp.setIndexName("userIdIndex");
    	queryExp.setConsistentRead(false);
    	final PaginatedQueryList<M_Device> result = this.mapper.query(M_Device.class, queryExp);
    	devices.addAll(result);
    	logger.info("Devices - getDevicesByUserId(" + userId + "): device size - " + result.size());
    	
    	return devices;
    }
    
     public void addRegisterDevice() throws IOException {
    	 logger.info("Devices - addRegisterDevice(): " + this.toString());
    	 this.mapper.save(this);
     }
    
     public void deRegisterDevice() throws IOException {
    	 logger.info("Devices - deRegisterDevice(): " + this.toString());
    	 this.mapper.delete(this);
     }
    
}
