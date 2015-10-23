package de.coins2015.oscar1.bettingoddscrawler;

import java.util.Arrays;

import org.json.simple.JSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

public class MongoDBHandler {

    private MongoClient mongoClient;
    private MongoCredential mongoCredential;
    private DB db;

    public MongoDBHandler(String host, String port, String userName,
	    String database, String password) {
	this.mongoCredential = MongoCredential.createCredential(userName,
		database, password.toCharArray());

	this.mongoClient = new MongoClient(new ServerAddress(Controller.host),
		Arrays.asList(mongoCredential));
	this.db = mongoClient.getDB(database);
    }

    public void storeData(JSONObject data, String collection) {
	DBObject dbObject = (DBObject) JSON.parse(data.toJSONString());
	DBCollection dbCollection = db.getCollection(collection);
	dbCollection.insert(dbObject);
    }
}
