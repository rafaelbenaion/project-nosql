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

public class Main {
    public static void main(String[] args) {

        /* ------------------------------------------------------------------------------------------------ */
        /* Exemple d'utilisation de l'application.                                                          */
        /* ------------------------------------------------------------------------------------------------ */

        /* ------------------------------------------------------------------------------------------------ */
        /* Création d'utilisateurs.                                                                         */
        /* ------------------------------------------------------------------------------------------------ */

        User client1 = new User("rafaelbaptista",
                "rafael@icloud.com",
                "laranja123",
                true,
                "Rafael",
                "Baptista",
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>());

        User client2 = new User("yusong",
                "song@icloud.com",
                "tofu123",
                true,
                "Yu",
                "Song",
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>());

        User vendeur1 = new User("marouanboulli",
                "marouan@icloud.com",
                "couscous123",
                true,
                "Marouan",
                "Boulli",
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>());

        User vendeur2 = new User("missaouioumayma",
                "missaoui@icloud.com",
                "fullStack123",
                true,
                "Oumayma",
                "Missaoui",
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>(),
                new ArrayList<Document>());


        /* ------------------------------------------------------------------------------------------------ */
        /* Get user by id.                                                                                  */
        /* ------------------------------------------------------------------------------------------------ */

        //User client1  = new User().getUserById(1);
        //User client2  = new User().getUserById(2);
        //User vendeur1 = new User().getUserById(3);
        //User vendeur2 = new User().getUserById(4);

        /* ------------------------------------------------------------------------------------------------ */
        /* Delete user by id.                                                                               */
        /* ------------------------------------------------------------------------------------------------ */

        vendeur2.deleteUsers(new Document("_id", vendeur2.getId()));

        /* ------------------------------------------------------------------------------------------------ */
        /* User interactions.                                                                               */
        /* ------------------------------------------------------------------------------------------------ */

        client1.addRating(client1.getId(), 12, "It works.");
        client1.addSearchHistory(client1.getId(), "Un four.");
        client1.addFavorite(client1.getId(), 451);
        client1.addStatistics(client1.getId(), "Nb_reservations", 52);
        client1.addReservation(client1.getId(), 316, 5, (float) 24.12);

        /* ------------------------------------------------------------------------------------------------ */
        /* Création de categories.                                                                          */
        /* ------------------------------------------------------------------------------------------------ */


        Category category1 = new Category("Camping");
        Category category2 = new Category("Informatique");
        Category category3 = new Category("Jardinage");


        /* ------------------------------------------------------------------------------------------------ */
        /* Get category by Id.                                                                              */
        /* ------------------------------------------------------------------------------------------------ */

        //Category category1 = new Category().getCategoryById(1);
        //Category category2 = new Category().getCategoryById(2);
        //Category category3 = new Category().getCategoryById(3);

        /* ------------------------------------------------------------------------------------------------ */
        /* Création de goods.                                                                               */
        /* ------------------------------------------------------------------------------------------------ */


        vendeur1.newGoods("Tente",
                "Tente 4 places, idéale pour le camping.",
                10.0,
                "https://www.tente.com/tente4places.jpg",
                "43.615, 7.071",
                true,
                category1.getId());



        vendeur1.newGoods("Macbook",
                "Macbook air M1.",
                50.0,
                "https://www.apple.com/macbook.jpg",
                "43.615, 7.071",
                true,
                category2.getId());



        /* ------------------------------------------------------------------------------------------------ */
        /* Get good by Id.                                                                                  */
        /* ------------------------------------------------------------------------------------------------ */

        Good tente   = new Good().getGoodById(1);
        Good macbook = new Good().getGoodById(2);

        /* ------------------------------------------------------------------------------------------------ */
        /* Creating conversations.                                                                          */
        /* ------------------------------------------------------------------------------------------------ */

        macbook.startConversation(client1.getId());

        Conversation my_conversation = new Conversation().getConversationById(1);

        my_conversation.insertMessage(client1.getId(),"I would like to rent the Macbook for 10 days.");
        my_conversation.insertMessage(vendeur1.getId(),"Sure, you can make the reservation.");
        my_conversation.insertMessage(client1.getId(),"Thank you, I will make the reservation now.");

        /* ------------------------------------------------------------------------------------------------ */
        /* Showing the conversation.                                                                        */
        /* ------------------------------------------------------------------------------------------------ */

        client1.myConversations();

        /* ------------------------------------------------------------------------------------------------ */
        /* Creating reservations.                                                                           */
        /* ------------------------------------------------------------------------------------------------ */

        client1.newReservation(macbook, 10);

        /* ------------------------------------------------------------------------------------------------ */
        /* Get reservation by Id.                                                                           */
        /* ------------------------------------------------------------------------------------------------ */

        Reservation my_macbook_reservation = new Reservation().getReservationById(1);

        /* ------------------------------------------------------------------------------------------------ */
        /* Paying the reservation.                                                                          */
        /* ------------------------------------------------------------------------------------------------ */

        my_macbook_reservation.payReservation();

        /* ------------------------------------------------------------------------------------------------ */
        /* Showing the reservations of a user.                                                              */
        /* ------------------------------------------------------------------------------------------------ */

        client1.myReservations();

        /* ------------------------------------------------------------------------------------------------ */
        /* Get all active non paid reservations.                                                            */
        /* ------------------------------------------------------------------------------------------------ */

        new Reservation().getAllActiveReservations();

        /* ------------------------------------------------------------------------------------------------ */
        /* Get the sum of cost of all reservations for each user.                                           */
        /* ------------------------------------------------------------------------------------------------ */

        new Reservation().sumReservationsCostByUser();

        /* ------------------------------------------------------------------------------------------------ */
        /* Get all goods for each user.                                                                     */
        /* ------------------------------------------------------------------------------------------------ */

        new Good().printAllGoodsForEachUser();

        /* ------------------------------------------------------------------------------------------------ */
        /* Get all goods with active reservation not payed.                                                 */
        /* ------------------------------------------------------------------------------------------------ */

        new Good().printAllGoodsWithActiveReservationStatus();

    }
}