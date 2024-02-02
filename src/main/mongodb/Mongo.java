/* -------------------------------------------------------------------------------------------------------- */
/* Projet d’Approfondissement - BD vers Big Data                                                            */
/* -------------------------------------------------------------------------------------------------------- */
/* 12 janvier 2023, Université Côte d'Azur.                                                                 */
/* BAPTISTA Rafael, BOULLI Marouan, MISSAOUI Oumayma & SONG Yu.                                             */
/* -------------------------------------------------------------------------------------------------------- */

package main.mongodb;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import java.util.Iterator;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.model.UpdateOptions;

import static com.mongodb.client.model.Sorts.descending;

public class Mongo {

    private MongoDatabase database;
    private String        dbName      = "rentalPlatform";
    private String        hostName    = "localhost";
    private int           port        = 27017;
    private String        userName    = "admin";
    private String        passWord    = "admin";

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
    /* createCollection()                                                                                   */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une collection dans la base de donnees.                               */
    /* ---------------------------------------------------------------------------------------------------- */

    public void createCollection(String nomCollection){

        database.createCollection(nomCollection);
        System.out.println("Collection " + nomCollection + " created successfully");

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
    /* insertInstanceCollection()                                                                           */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Insérer une instance dans une collection.                                                            */
    /* ---------------------------------------------------------------------------------------------------- */

    public void insertInstanceCollection(String nomCollection, Document instance){
        MongoCollection<Document> collection = database.getCollection(nomCollection);
        collection.insertOne(instance);
        System.out.println("Document inserted successfully");
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getInstanceById()                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Get an instance from a collection by its id.                                                         */
    /* ---------------------------------------------------------------------------------------------------- */

    public Document getInstanceById(String nomCollection, Integer id){

        System.out.println("\n\n\n*********** dans getInstanceById *****************");

        MongoCollection<Document> collection = database.getCollection(nomCollection);
        Document whereQuery                  = new Document();

        whereQuery.put("_id", id );

        FindIterable<Document> listDept=collection.find(whereQuery);

        Iterator it = listDept.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
        return whereQuery;
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getInstances()                                                                                       */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Get list of instances.                                                                               */
    /* ---------------------------------------------------------------------------------------------------- */

    public void getInstances(String  nomCollection,
                            Document whereQuery,
                            Document projectionFields,
                            Document sortFields){

        System.out.println("\n\n\n*********** dans getInstances *****************");

        MongoCollection<Document>  collection    = database.getCollection(nomCollection);
        FindIterable<Document>     listInstances = collection.find(whereQuery)
                                                             .sort(sortFields)
                                                             .projection(projectionFields);

        // Getting the iterator
        Iterator it = listInstances.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getLastId()                                                                                          */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Get the last id of a collection.                                                                     */
    /* ---------------------------------------------------------------------------------------------------- */

    public int getLastId(String nomCollection){

        System.out.println("\n\n\n*********** dans getLastId *****************");

        MongoCollection<Document> collection    = database.getCollection(nomCollection);
        FindIterable<Document>    listInstances = collection.find().sort(descending("_id")).limit(1);

        Document lastInsertedDocument = listInstances.first();

        //  ObjectId id = listInstances.first().getObjectId("_id");

        if (listInstances.first() != null) {
            return (int) lastInsertedDocument.get("_id");
        } else {
            return 0;
        }
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* updateInstances()                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de mettre a jour des instances dans une collection.                            */
    /* ---------------------------------------------------------------------------------------------------- */

    public void updateInstances(String nomCollection,
                               Document whereQuery,
                               Document updateExpressions, UpdateOptions updateOptions){

        MongoCollection<Document>   collection   = database.getCollection(nomCollection);
        UpdateResult                updateResult = collection.updateMany(whereQuery, updateExpressions);

        System.out.println("\n" +
                    "Resultat update : "
                +   "getUpdate id: "       + updateResult
                +   " getMatchedCount : "  + updateResult.getMatchedCount()
                +   " getModifiedCount : " + updateResult.getModifiedCount()
        );
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* deleteInstances()                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer des instances dans une collection.                                */
    /* ---------------------------------------------------------------------------------------------------- */

    public void deleteInstances(String nomCollection, Document filters){

        System.out.println("\n\n\n*********** dans deleteInstances *****************");

        FindIterable<Document>    listInstances;
        Iterator                  it;
        MongoCollection<Document> collection = database.getCollection(nomCollection);

        listInstances = collection.find(filters).sort(new Document("_id", 1));
        it            = listInstances.iterator(); // Getting the iterator
        this.displayIterator(it, "Dans deleteInstances: avant suppression");

        collection.deleteMany(filters);

        listInstances = collection.find(filters).sort(new Document("_id", 1));
        it            = listInstances.iterator(); // Getting the iterator
        this.displayIterator(it, "Dans deleteInstances: Apres suppression");
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
