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
import java.text.SimpleDateFormat;
import java.util.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.model.UpdateOptions;

public class User extends Document{

    private final String          collection_name  = "colUser";
    private       Integer         user_id;
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
        /* Insertion de l'utilisateur dans la base de données                                               */
        /* ------------------------------------------------------------------------------------------------ */

        this.user_id = mongo.getLastId(this.collection_name) + 1;

        Document newuser = new Document(
                "_id",                 this.user_id)
                .append ("nom",                 this.username)
                .append("username",             this.username)
                .append("email",                this.email)
                .append("password_hash",        this.password_hash)
                .append("identity_verified",    this.identity_verified)
                .append("first_name",           this.first_name)
                .append("last_name",            this.last_name)
                .append("user_ratings",         this.user_ratings)
                .append("search_history",       this.search_history)
                .append("favorites",            this.favorites)
                .append("reservations",         this.reservations)
                .append("user_statistics",      this.user_statistics);

        mongo.insertInstanceCollection(this.collection_name, newuser);
        System.out.println("User created successfully!");

    }
    User() {
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

    public User getUserById(Integer id) {

        Document user           = mongo.getInstanceById(this.collection_name, id);

        this.user_id            = user.getInteger("_id");
        this.username           = user.getString("username");
        this.email              = user.getString("email");
        this.password_hash      = user.getString("password_hash");
        this.identity_verified  = user.getBoolean("identity_verified");
        this.first_name         = user.getString("first_name");
        this.last_name          = user.getString("last_name");
        this.user_ratings       = (List<Document>) user.get("user_ratings");
        this.search_history     = (List<Document>) user.get("search_history");
        this.favorites          = (List<Document>) user.get("favorites");
        this.reservations       = (List<Document>) user.get("reservations");
        this.user_statistics    = (List<Document>) user.get("user_statistics");

        return this;
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* updateUsers()                                                                                        */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de mettre a jour un ou plusieurs utilisateurs.                                 */
    /* ---------------------------------------------------------------------------------------------------- */

    public void updateUsers(Document whereQuery,
                            Document updateExpressions,
                            UpdateOptions updateOptions) {

        mongo.updateInstances(this.collection_name, whereQuery, updateExpressions, updateOptions);
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* deleteUsers()                                                                                        */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer un ou plusieurs utilisateurs.                                     */
    /* ---------------------------------------------------------------------------------------------------- */

    public void deleteUsers(Document whereQuery) {
        mongo.deleteFromCollection(this.collection_name, whereQuery);
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* addRating()                                                                                          */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'ajouter un nouveau rating pour un utilisateur.                               */
    /* ---------------------------------------------------------------------------------------------------- */
    /*    "user_ratings": [                                                                                 */
    /*     {                                                                                                */
    /*       "rater_id": 12,                                                                                */
    /*       "rating": 5,                                                                                   */
    /*       "comment": "It works."                                                                         */
    /*     }                                                                                                */
    /* ---------------------------------------------------------------------------------------------------- */

    public void addRating(Integer user_id, Integer grade, String comment) {

        Document rating = new Document()
                .append("rater_id", user_id)
                .append("rating",   grade)
                .append("comment",  comment);

        this.updateUsers(
                new Document("_id", user_id),
                new Document("$push", new Document("user_ratings", rating)),
                new UpdateOptions());
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* addSearchHistory()                                                                                   */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'ajouter une nouvelle recherche pour un utilisateur.                          */
    /* ---------------------------------------------------------------------------------------------------- */
    /*    "search_history": [                                                                               */
    /*     {                                                                                                */
    /*       "query": "Un four.",                                                                           */
    /*       "timestamp": "2015-11-10T12:15:01Z"                                                            */
    /*     }                                                                                                */
    /* ---------------------------------------------------------------------------------------------------- */

    public void addSearchHistory(Integer user_id, String query) {

        // Recuperer la date actuelle
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Document search_history = new Document()
                .append("query", query)
                .append("timestamp",  date);

        this.updateUsers(
                new Document("_id", user_id),
                new Document("$push", new Document("search_history", search_history)),
                new UpdateOptions());
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* addFavorite()                                                                                        */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'ajouter un nouveau favori pour un utilisateur.                               */
    /* ---------------------------------------------------------------------------------------------------- */
    /*    "favorites": [                                                                                    */
    /*     {                                                                                                */
    /*       "goods_id": 451,                                                                               */
    /*       "favorited_at": "2015-11-10T12:15:01Z"                                                         */
    /*     }                                                                                                */
    /* ---------------------------------------------------------------------------------------------------- */

    public void addFavorite(Integer user_id, Integer goods_id) {

        // Recuperer la date actuelle
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Document favorite = new Document()
                .append("goods_id", goods_id)
                .append("favorited_at",  date);

        this.updateUsers(
                new Document("_id", user_id),
                new Document("$push", new Document("favorites", favorite)),
                new UpdateOptions());
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* addStatistic()                                                                                       */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'ajouter une nouvelle statistique pour un utilisateur.                        */
    /* ---------------------------------------------------------------------------------------------------- */
    /*    "user_statistics": [                                                                              */
    /*      {                                                                                               */
    /*        "stat_name": "Nb_reservations",                                                               */
    /*        "stat_value": 5,                                                                              */
    /*        "total_cost": 24                                                                              */
    /*      }                                                                                               */
    /* ---------------------------------------------------------------------------------------------------- */

    public void addStatistics(Integer user_id, String stat_name, Integer stat_value) {

        Document statistic = new Document()
                .append("stat_name", stat_name)
                .append("stat_value",  stat_value);

        this.updateUsers(
                new Document("_id", user_id),
                new Document("$push", new Document("user_statistics", statistic)),
                new UpdateOptions());
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* addReservation()                                                                                     */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'ajouter une nouvelle reservation pour un utilisateur.                        */
    /* ---------------------------------------------------------------------------------------------------- */
    /*    "reservations": [                                                                                 */
    /*      {                                                                                               */
    /*        "goods_id": 316,                                                                              */
    /*        "period": 5,                                                                                  */
    /*        "total_cost": 24.12                                                                           */
    /*      }                                                                                               */
    /* ---------------------------------------------------------------------------------------------------- */

    public void addReservation(Integer user_id, Integer goods_id, Integer period, Float total_cost) {

        Document reservation = new Document()
                .append("goods_id", goods_id)
                .append("period",  period)
                .append("total_cost",  total_cost);

        this.updateUsers(
                new Document("_id", user_id),
                new Document("$push", new Document("reservations", reservation)),
                new UpdateOptions());
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getId()                                                                                              */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de recuperer l'id d'un utilisateur.                                            */
    /* ---------------------------------------------------------------------------------------------------- */

    public Integer getId(){
        return this.user_id;
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* newGoods()                                                                                           */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer un nouveau bien. Et de l'ajouter a la base de donnees.                */
    /* ---------------------------------------------------------------------------------------------------- */

    public void newGoods(String title,
                         String description,
                         Double price_per_day,
                         String images_url,
                         String coordonees_gps,
                         Boolean availability_status,
                         Integer category) {

        Good good = new Good(title,
                             description,
                             price_per_day,
                             images_url,
                             coordonees_gps,
                             availability_status,
                             category,
                             this.user_id);

        good.insertGood();                                        // Insertion du bien dans la base de donnees
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* newReservation()                                                                                     */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une nouvelle reservation pour un utilisateur.                         */
    /* ---------------------------------------------------------------------------------------------------- */

    public void newReservation(Good    good,
                               Integer rental_period) {

        Reservation reservation = new Reservation(this.user_id,
                                                  good,
                                                  rental_period,
                                                  "Active");
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* deleteAllReservations()                                                                              */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer toutes les reservations de l'utilisateur.                         */
    /* ---------------------------------------------------------------------------------------------------- */

    public void deleteAllReservations() {
        Reservation reservation = new Reservation();
        reservation.deleteReservationsFromUser(this.user_id);
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* myConversations()                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'afficher toutes les conversations d'un utilisateur.                          */
    /* ---------------------------------------------------------------------------------------------------- */

    public void myConversations() {
        Conversation conversation = new Conversation();
        conversation.printAllConversations(this.user_id);
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* myReservations()                                                                                     */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'afficher toutes les reservations d'un utilisateur.                           */
    /* ---------------------------------------------------------------------------------------------------- */

    public void myReservations() {

        System.out.println("----------------------------------------User " + this.user_id + " reservations:");

        mongo.getInstances(
                "colReservations",
                new Document("renter", this.user_id),
                new Document(),
                new Document());
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* Testing functions                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */

    public static void main(String[] args) {

    }

}
