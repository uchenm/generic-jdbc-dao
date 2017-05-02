package com.cffex.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Ming on 2017/4/28.
 */
public class GridFsAppStore {

    public static void main(String[] args) {

        ApplicationContext ctx =
            new AnnotationConfigApplicationContext(SpringMongoGridFsConfig.class);
        GridFsOperations gridOperations =
            (GridFsOperations) ctx.getBean("gridFsTemplate");

        DBObject metaData = new BasicDBObject();
        metaData.put("extra1", "anything 1");
        metaData.put("extra2", "anything 2");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("c:/Users/Ming/Downloads/docker-deploy.png");
            gridOperations.store(inputStream, "docker-deploy.png", "image/png", metaData);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Done");

        List<GridFSDBFile> result = gridOperations.find(
            new Query().addCriteria(Criteria.where("filename").is("docker-deploy.png")));

        for (GridFSDBFile file : result) {
            try {
                System.out.println(file.getFilename());
                System.out.println(file.getContentType());

                //save as another image
                file.writeTo("c:/Users/Ming/Downloads/docker-deploy.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Done");

    }

}
