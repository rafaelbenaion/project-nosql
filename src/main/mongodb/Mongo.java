/* -------------------------------------------------------------------------------------------------------- */
/* Projet d’Approfondissement - BD vers Big Data                                                            */
/* -------------------------------------------------------------------------------------------------------- */
/* 12 janvier 2023, Université Côte d'Azur.                                                                 */
/* BAPTISTA Rafael, BOULLI Marouan, MISSAOUI Oumayma & SONG Yu.                                             */
/* -------------------------------------------------------------------------------------------------------- */

package main.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BsonField;
import org.bson.Document;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.IndexOptions;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.model.UpdateOptions;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Sorts.descending;

public class Mongo {

    private MongoDatabase database;
    private String dbName = "rentalPlatform";
    private String hostName = "localhost";
    private int port = 27017;
    private String userName = "admin";
    private String passWord = "admin";

    /* ---------------------------------------------------------------------------------------------------- */
    /* Mongo()                                                                                              */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une instance de la classe Mongo.                                      */
    /* ---------------------------------------------------------------------------------------------------- */
    Mongo() {

        // Creating a Mongo client
        MongoClient mongoClient = new MongoClient(hostName, port);

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential(userName, dbName, passWord.toCharArray());
        System.out.println("Connected to the database successfully");
        System.out.println("Credentials ::" + credential);

        // Accessing the database
        database = mongoClient.getDatabase(dbName);

    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* createCollection()                                                                                   */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une collection dans la base de donnees.                               */
    /* ---------------------------------------------------------------------------------------------------- */

    public void createCollection(String nomCollection) {

        database.createCollection(nomCollection);
        System.out.println("Collection " + nomCollection + " created successfully");

    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* deleteFromCollection()                                                                               */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer des instances dans une collection.                                */
    /* Le parametre filters : permet de passer des conditions de recherche des instances a supprimer.       */
    /* ---------------------------------------------------------------------------------------------------- */

    public void deleteFromCollection(String nomCollection, Document filters) {

        System.out.println("\n*********** dans deleteFromCollection *****************");

        FindIterable<Document> listInstances;
        Iterator it;
        MongoCollection<Document> colInstances = database.getCollection(nomCollection);

        listInstances = colInstances.find(filters).sort(new Document("_id", 1));
        it = listInstances.iterator(); // Getting the iterator
        this.displayIterator(it, "Dans deleteFromCollection: avant suppression");

        colInstances.deleteMany(filters);

        listInstances = colInstances.find(filters).sort(new Document("_id", 1));
        it = listInstances.iterator(); // Getting the iterator
        this.displayIterator(it, "Dans deleteFromCollection: Apres suppression");
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* dropCollection()                                                                                     */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer une collection dans la base de donnees.                           */
    /* ---------------------------------------------------------------------------------------------------- */

    public void dropCollection(String nomCollection) {
        //Drop a collection
        MongoCollection<Document> colDepts = null;
        System.out.println("\n*********** dans dropCollectionDept *****************");

        System.out.println("!!!! Collection Dept : " + colDepts);

        colDepts = database.getCollection(nomCollection);
        System.out.println("!!!! Collection Dept : " + colDepts);
        // Visiblement jamais !!!
        if (colDepts == null)
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

    public void insertInstanceCollection(String nomCollection, Document instance) {
        MongoCollection<Document> collection = database.getCollection(nomCollection);
        collection.insertOne(instance);
        System.out.println("Document inserted successfully");
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getInstanceById()                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Get an instance from a collection by its id.                                                         */
    /* ---------------------------------------------------------------------------------------------------- */

    public Document getInstanceById(String nomCollection, Integer id) {

        System.out.println("\n*********** dans getInstanceById *****************");

        MongoCollection<Document> collection = database.getCollection(nomCollection);
        Document whereQuery = new Document();

        whereQuery.put("_id", id);

        FindIterable<Document> listDept = collection.find(whereQuery);

        Iterator it = listDept.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            System.out.println(next);
            return (Document) next;
        }
        return whereQuery;
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getInstances()                                                                                       */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Get list of instances.                                                                               */
    /* ---------------------------------------------------------------------------------------------------- */

    public void getInstances(String nomCollection,
                             Document whereQuery,
                             Document projectionFields,
                             Document sortFields) {

        System.out.println("\n*********** dans getInstances *****************");

        MongoCollection<Document> collection = database.getCollection(nomCollection);
        FindIterable<Document> listInstances = collection.find(whereQuery)
                .sort(sortFields)
                .projection(projectionFields);

        // Getting the iterator
        Iterator it = listInstances.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getLastId()                                                                                          */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Get the last id of a collection.                                                                     */
    /* ---------------------------------------------------------------------------------------------------- */

    public int getLastId(String nomCollection) {

        System.out.println("\n*********** dans getLastId *****************");

        MongoCollection<Document> collection = database.getCollection(nomCollection);
        FindIterable<Document> listInstances = collection.find().sort(descending("_id")).limit(1);

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
                                Document updateExpressions, UpdateOptions updateOptions) {

        MongoCollection<Document> collection = database.getCollection(nomCollection);
        UpdateResult updateResult = collection.updateMany(whereQuery, updateExpressions);

        System.out.println("\n" +
                "Resultat update : "
                + "getUpdate id: " + updateResult
                + " getMatchedCount : " + updateResult.getMatchedCount()
                + " getModifiedCount : " + updateResult.getModifiedCount()
        );
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* deleteInstances()                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer des instances dans une collection.                                */
    /* ---------------------------------------------------------------------------------------------------- */

    public void deleteInstances(String nomCollection, Document filters) {

        System.out.println("\n*********** dans deleteInstances *****************");

        FindIterable<Document> listInstances;
        Iterator it;
        MongoCollection<Document> collection = database.getCollection(nomCollection);

        listInstances = collection.find(filters).sort(new Document("_id", 1));
        it = listInstances.iterator(); // Getting the iterator
        this.displayIterator(it, "Dans deleteInstances: avant suppression");

        collection.deleteMany(filters);

        listInstances = collection.find(filters).sort(new Document("_id", 1));
        it = listInstances.iterator(); // Getting the iterator
        this.displayIterator(it, "Dans deleteInstances: Apres suppression");
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* displayIterator()                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Parcours un iterateur et affiche les documents qui s'y trouvent.                                     */
    /* ---------------------------------------------------------------------------------------------------- */

    public void displayIterator(Iterator it, String message) {
        System.out.println(" \n #### " + message + " ################################");
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* groupBy()                                                                                            */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de grouper des instances dans une collection.                                  */
    /* ---------------------------------------------------------------------------------------------------- */

    public void groupBy(
            String collectionName,
            String groupOperator,
            Document groupFields) {

        MongoCollection<Document> collection = database.getCollection(collectionName);

        // Vérifier que l'opérateur est "$group"
        if (!"$group".equals(groupOperator)) {
            System.out.println("Opérateur non supporté : " + groupOperator);
            return;
        }

        // Créer l'agrégation
        Bson group = Aggregates.group(groupFields);

        // Exécuter la requête d'agrégation
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(group));

        // Afficher les résultats
        Iterator<Document> it = result.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* groupByTotalCost()                                                                                   */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de grouper les reservations par utilisateur et avoir le coût total.            */
    /* ---------------------------------------------------------------------------------------------------- */

    public void groupByTotalCost(String collectionName, String groupOperator, Document groupFields) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        // Assuming $group is the correct operator to use
        Bson group = Aggregates.group(groupFields.get("_id"),
                Accumulators.sum("totalCost", "$total_cost"));

        // Execute the aggregation
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(group));

        // Print the results correctly
        Iterator<Document> it = result.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            // Adjust the printing if necessary based on your actual result structure
            System.out.println(doc.toJson());
        }
    }



    /* ---------------------------------------------------------------------------------------------------- */
    /* aggregate()                                                                                          */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'aggréger des instances dans une collection.                                  */
    /* ---------------------------------------------------------------------------------------------------- */

    public void aggregate(String collectionName, Bson match, Bson group, List<Bson> additionalStages) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        // Build the aggregation pipeline
        List<Bson> pipeline = new ArrayList<>();
        if (match != null) {
            pipeline.add(Aggregates.match(match));
        }
        if (group != null) {
            pipeline.add(Aggregates.group(group));
        }
        if (additionalStages != null && !additionalStages.isEmpty()) {
            pipeline.addAll(additionalStages);
        }

        // Execute the aggregation
        collection.aggregate(pipeline).forEach(printBlock);
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* printBlock()                                                                                         */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'afficher un document.                                                        */
    /* ---------------------------------------------------------------------------------------------------- */
    java.util.function.Consumer<Document> printBlock = new java.util.function.Consumer<Document>() {
        @Override
        public void accept(Document document) {
            System.out.println(document.toJson());
        }
    };

    /* ---------------------------------------------------------------------------------------------------- */
    /* printAllGoodsForEachUser()                                                                           */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'afficher tous les biens pour chaque utilisateur.                             */
    /* ---------------------------------------------------------------------------------------------------- */

    public void printAllGoodsForEachUser() {

        MongoCollection<Document> collection = database.getCollection("colGoods");

        List<Bson> pipeline = Arrays.asList(
                Aggregates.group("$owner",
                        Accumulators.push("goods", new Document("title", "$title")
                                .append("description", "$description")
                                .append("price_per_day", "$price_per_day")
                                .append("images_url", "$images_url")
                                .append("coordonees_gps", "$coordonees_gps")
                                .append("availability_status", "$availability_status")
                                .append("category", "$category")
                        )
                ),
                Aggregates.project(new Document("_id", 0)
                        .append("owner", "$_id")
                        .append("goods", 1)
                )
        );

        AggregateIterable<Document> results = collection.aggregate(pipeline);
        for (Document doc : results) {
            System.out.println(doc.toJson());
        }

    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* printActiveReservedGoods()                                                                           */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'afficher les biens réservés en attente de payment.                           */
    /* ---------------------------------------------------------------------------------------------------- */

    public void printActiveReservedGoods() {
        MongoCollection<Document> reservationsCollection = database.getCollection("colReservations");

        List<Bson> pipeline = Arrays.asList(

                Aggregates.match(Filters.eq("reservation_status", "Active")),
                Aggregates.lookup("colGoods", "good_reserved", "_id", "goodsDetails"),
                Aggregates.unwind("$goodsDetails"),
                Aggregates.group("$goodsDetails._id",
                        Accumulators.first("goodsDetails", "$goodsDetails")
                ),

                Aggregates.project(new Document("_id", 0)
                        .append("title", "$goodsDetails.title")
                        .append("description", "$goodsDetails.description")
                        .append("price_per_day", "$goodsDetails.price_per_day")
                        .append("images_url", "$goodsDetails.images_url")
                        .append("coordonees_gps", "$goodsDetails.coordonees_gps")
                        .append("availability_status", "$goodsDetails.availability_status")
                        .append("category", "$goodsDetails.category")
                        .append("owner", "$goodsDetails.owner")
                )
        );

        AggregateIterable<Document> result = reservationsCollection.aggregate(pipeline);

        for (Document doc : result) {
            System.out.println(doc.toJson());
        }
    }

}
