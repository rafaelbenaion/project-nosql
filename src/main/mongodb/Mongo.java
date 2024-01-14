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
    private String        dbName      = "rentalPlatform";
    private String        hostName    = "localhost";
    private int           port        = 27017;
    private String        userName    = "admin";
    private String        passWord    = "admin";

    /* ---------------------------------------------------------------------------------------------------- */
    /* dropCollection()                                                                                     */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer une collection dans la base de donnees.                           */
    /* ---------------------------------------------------------------------------------------------------- */

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

    /* ---------------------------------------------------------------------------------------------------- */
    /* createCollection()                                                                                   */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une collection dans la base de donnees.                               */
    /* ---------------------------------------------------------------------------------------------------- */

    public void createCollection(String nomCollection){

        database.createCollection(nomCollection);
        System.out.println("Collection " + nomCollection + " created successfully");

    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* Mongo()                                                                                              */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une instance de la classe Mongo.                                      */
    /* ---------------------------------------------------------------------------------------------------- */
    Mongo(){

        // Creating a Mongo client
        MongoClient mongoClient = new MongoClient( hostName , port );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential(userName, dbName, passWord.toCharArray());
        System.out.println("Connected to the database successfully");
        System.out.println("Credentials ::"+ credential);

        // Accessing the database
        database = mongoClient.getDatabase(dbName);

    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* deleteFromCollection()                                                                               */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer des instances dans une collection.                                */
    /* Le parametre filters : permet de passer des conditions de recherche des instances a supprimer.       */
    /* ---------------------------------------------------------------------------------------------------- */

    public void deleteFromCollection(String nomCollection, Document filters){

        System.out.println("\n\n\n*********** dans deleteFromCollection *****************");

        FindIterable<Document> listInstances;
        Iterator it;
        MongoCollection<Document> colInstances=database.getCollection(nomCollection);

        listInstances   = colInstances.find(filters).sort(new Document("_id", 1));
        it              = listInstances.iterator(); // Getting the iterator
        this.displayIterator(it, "Dans deleteFromCollection: avant suppression");

        colInstances.deleteMany(filters);

        listInstances   = colInstances.find(filters).sort(new Document("_id", 1));
        it              = listInstances.iterator(); // Getting the iterator
        this.displayIterator(it, "Dans deleteFromCollection: Apres suppression");
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getInstanceById()                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Get an instance from a collection by its id.                                                         */
    /* ---------------------------------------------------------------------------------------------------- */

    public void getInstanceById(String nomCollection, Integer id){

        System.out.println("\n\n\n*********** dans getInstanceById *****************");

        MongoCollection<Document> collection = database.getCollection(nomCollection);
        Document whereQuery                  = new Document();

        whereQuery.put("_id", id );

        FindIterable<Document> listDept=collection.find(whereQuery);

        Iterator it = listDept.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* insertInstanceCollection()                                                                           */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Insérer une instance dans une collection.                                                            */
    /* ---------------------------------------------------------------------------------------------------- */

    // Document instance = new Document("_id", 50).append("dname", "FORMATION").append("loc", "Nice");

    public void insertInstanceCollection(String nomCollection, Document instance){
        MongoCollection<Document> collection = database.getCollection(nomCollection);
        collection.insertOne(instance);
        System.out.println("Document inserted successfully");
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* displayIterator()                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Parcours un iterateur et affiche les documents qui s'y trouvent.                                     */
    /* ---------------------------------------------------------------------------------------------------- */

    public void displayIterator(Iterator it, String message){
        System.out.println(" \n #### "+ message + " ################################");
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }


}
