package com.cffex;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

/**
 * Created by Ming on 2017/4/25.
 */
public class MongoApp {
    public static void main(String[] args) throws Exception{
        Mongo mongo = new Mongo("localhost", 27017);
        DB db = mongo.getDB("yourdb");
        DBCollection collection = db.getCollection("dummyColl");

        // convert JSON to DBObject directly
        DBObject dbObject = (DBObject) JSON
            .parse("{'name':'mkyong', 'age':30}");

        collection.insert(dbObject);

        DBCursor cursorDoc = collection.find();
        while (cursorDoc.hasNext()) {
            System.out.println(cursorDoc.next());
        }

        System.out.println("Done");

    }
}
