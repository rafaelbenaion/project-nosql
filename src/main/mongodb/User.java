/* -------------------------------------------------------------------------------------------------------- */
/* Projet d’Approfondissement - BD vers Big Data                                                            */
/* -------------------------------------------------------------------------------------------------------- */
/* 12 janvier 2023, Université Côte d'Azur.                                                                 */
/* BAPTISTA Rafael, BOULLI Marouan, MISSAOUI Oumayma & SONG Yu.                                             */
/* -------------------------------------------------------------------------------------------------------- */

package main.mongodb;

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

public class User extends Document{

    private       String          collection_name  = "colUser";
    private final Integer         user_id;
    private       String          username;
    private       String          email;
    private       String          password_hash;
    private       Boolean         identity_verified;
    private       String          first_name;
    private       String          last_name;
    private       List<Document>  user_ratings;
    private       List<Document>  search_history;
    private       List<Document>  favorites;
    private       List<Document>  reservations;
    private       List<Document>  user_statistics;
    Mongo mongo = new Mongo();

    /* ---------------------------------------------------------------------------------------------------- */
    /* User()                                                                                               */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une instance de la classe User.                                       */
    /* ---------------------------------------------------------------------------------------------------- */

    User(String  username,
         String  email,
         String  password_hash,
         Boolean identity_verified,
         String first_name,
         String last_name,
         List<Document> user_ratings,
         List<Document> search_history,
         List<Document> favorites,
         List<Document> reservations,
         List<Document> user_statistics) {

        this.username           = username;
        this.email              = email;
        this.password_hash      = password_hash;
        this.identity_verified  = identity_verified;
        this.first_name         = first_name;
        this.last_name          = last_name;
        this.user_ratings       = user_ratings;
        this.search_history     = search_history;
        this.favorites          = favorites;
        this.reservations       = reservations;
        this.user_statistics    = user_statistics;

        /* ------------------------------------------------------------------------------------------------ */
        /* Defining User ID                                                                                 */
        /* ------------------------------------------------------------------------------------------------ */

        this.user_id = mongo.getLastId(this.collection_name) + 1;

    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* insertUser()                                                                                         */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'inserer un User dans la base         .                                       */
    /* ---------------------------------------------------------------------------------------------------- */

    public void insertUser() {

        Document newuser = new Document(
                         "_id",                 this.user_id)
                .append ("nom",                 this.username)
                .append("username",             this.username)
                .append("email",                this.email)
                .append("password_hash",        this.password_hash)
                .append("identity_verified",    this.identity_verified)
                .append("first_name",           this.first_name)
                .append("last_name",            this.last_name)
                .append("user_ratings",         "")
                .append("search_history",       "")
                .append("favorites",            "")
                .append("reservations",         "")
                .append("user_statistics",      "");

        newuser.append("user_ratings", this.user_ratings);

        mongo.insertInstanceCollection(this.collection_name, newuser);
        System.out.println("Document inserted successfully");
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getAllUsers()                                                                                        */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de recuperer tous les utilisateurs.                                            */
    /* ---------------------------------------------------------------------------------------------------- */

    public void getAllUsers(Document whereQuery,
                            Document projectionFields,
                            Document sortFields) {

        mongo.getInstances(this.collection_name, whereQuery, projectionFields, sortFields);

    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getUserById()                                                                                        */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de recuperer tous les utilisateurs.                                            */
    /* ---------------------------------------------------------------------------------------------------- */

    public void getUserById(Integer id) {
        mongo.getInstanceById(this.collection_name, id);
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* Testing functions                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */

    public static void main(String[] args) {

        System.out.println("Testing User classes!");

        Mongo mongo = new Mongo();

        // Create a new user only for testing
        User user = new User("marouan",
                             "marouan@icloud.com",
                             "marouan123",
                             true,
                             "Rafael",
                             "Baptista",
                             new ArrayList<Document>(),
                             new ArrayList<Document>(),
                             new ArrayList<Document>(),
                             new ArrayList<Document>(),
                             new ArrayList<Document>());

        //mongo.dropCollection(user.collection_name);
        //mongo.createCollection(user.collection_name);

        //user.insertUser();
        //mongo.deleteFromCollection(user.collection_name, user);

        mongo.getInstanceById(user.collection_name, user.user_id);

        user.getAllUsers(new Document(), new Document(), new Document());
    }

}
