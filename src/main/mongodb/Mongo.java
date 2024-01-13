/* -------------------------------------------------------------------------------------------------------- */
/* Projet d’Approfondissement - BD vers Big Data                                                            */
/* -------------------------------------------------------------------------------------------------------- */
/* 12 janvier 2023, Université Côte d'Azur.                                                                 */
/* BAPTISTA Rafael, BOULLI Marouan, MISSAOUI Oumayma & SONG Yu.                                             */
/* -------------------------------------------------------------------------------------------------------- */

package main.mongodb;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DB;
import org.bson.Document;
import java.util.Arrays;
import java.util.List;
import com.mongodb.client.FindIterable;
import java.util.Iterator;
import java.util.ArrayList;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.model.UpdateOptions;



public class Mongo {

    private MongoDatabase database;
    private String      dbName              = "rentalPlatform";
    private String      hostName            = "localhost";
    private int         port                = 27017;
    private String      userName            = "admin";
    private String      passWord            = "admin";

    /* -------------------------------------------------------------------------------------------------------- */
    /* dropCollection()                                                                                         */
    /* -------------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer une collection dans la base de donnees.                               */
    /* -------------------------------------------------------------------------------------------------------- */

    public void dropCollection(String nomCollection){
        //Drop a collection
        MongoCollection<Document> colDepts=null;
        System.out.println("\n\n\n*********** dans dropCollectionDept *****************");

        System.out.println("!!!! Collection Dept : "+colDepts);

        colDepts=database.getCollection(nomCollection);
        System.out.println("!!!! Collection Dept : "+colDepts);
        // Visiblement jamais !!!
        if (colDepts==null)
            System.out.println("Collection inexistante");
        else {
            colDepts.drop();
            System.out.println("Collection colDepts removed successfully !!!");

        }
    }

    /* -------------------------------------------------------------------------------------------------------- */
    /* createCollection()                                                                                       */
    /* -------------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une collection dans la base de donnees.                                   */
    /* -------------------------------------------------------------------------------------------------------- */

    public void createCollection(String nomCollection){

        database.createCollection(nomCollection);
        System.out.println("Collection " + nomCollection + " created successfully");

    }

    Mongo(){

        // Creating a Mongo client

        MongoClient mongoClient = new MongoClient( hostName , port );

        // Creating Credentials
        // RH : Ressources Humaines
        MongoCredential credential;
        credential = MongoCredential.createCredential(userName, dbName,
                passWord.toCharArray());
        System.out.println("Connected to the database successfully");
        System.out.println("Credentials ::"+ credential);
        // Accessing the database
        database = mongoClient.getDatabase(dbName);

    }

}
