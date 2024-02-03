/* -------------------------------------------------------------------------------------------------------- */
/* Projet d’Approfondissement - BD vers Big Data                                                            */
/* -------------------------------------------------------------------------------------------------------- */
/* 12 janvier 2023, Université Côte d'Azur.                                                                 */
/* BAPTISTA Rafael, BOULLI Marouan, MISSAOUI Oumayma & SONG Yu.                                             */
/* -------------------------------------------------------------------------------------------------------- */


package main.mongodb;

import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Conversation {

    private final String          collection_name  = "colConversations";
    private       Integer         conversation_id;
    private       Integer         vendeur;
    private       Integer         client;
    private       List<Document>  messages;
    private       Integer         good_related;
    private       String          last_update;

    Mongo mongo = new Mongo();

    /* ---------------------------------------------------------------------------------------------------- */
    /* Conversation()                                                                                       */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une instance de la classe Conversation.                               */
    /* ---------------------------------------------------------------------------------------------------- */

    Conversation(Integer        vendeur,
                 Integer        client,
                 Integer        good_related) {

        this.conversation_id = mongo.getLastId(this.collection_name) + 1;               // Nouveau identifiant
        this.vendeur         = vendeur;
        this.client          = client;
        this.messages        = new ArrayList<Document>();
        this.good_related    = good_related;
        this.last_update     = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        /* ------------------------------------------------------------------------------------------------ */
        /* Insertion de la conversation dans la base de données                                             */
        /* ------------------------------------------------------------------------------------------------ */

        Document conversation = new Document("_id",this.conversation_id)
                .append("vendeur",        this.vendeur)
                .append("client",         this.client)
                .append("messages",       this.messages)
                .append("good_related",   this.good_related)
                .append("last_update",    this.last_update);

        mongo.insertInstanceCollection(this.collection_name, conversation); // Insert conversation in database
    }
    Conversation() {}

    /* ---------------------------------------------------------------------------------------------------- */
    /* getConversationById()                                                                                */
    /* ---------------------------------------------------------------------------------------------------- */

    public Conversation getConversationById(Integer id) {

        Conversation conversation     = new Conversation();
        Document conversation_doc     = mongo.getInstanceById(this.collection_name, id);

        conversation.conversation_id  = conversation_doc.getInteger("_id");
        conversation.vendeur          = conversation_doc.getInteger("vendeur");
        conversation.client           = conversation_doc.getInteger("client");
        conversation.messages         = (List<Document>) conversation_doc.get("messages");
        conversation.good_related     = conversation_doc.getInteger("good_related");
        conversation.last_update      = conversation_doc.getString("last_update");

        return conversation;
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* insertMessage()                                                                                      */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'insérer un message dans une conversation.                                    */
    /* ---------------------------------------------------------------------------------------------------- */

    public void insertMessage(Integer user_id, String texte) {

        String date       = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Document messsage = new Document()
                .append("user",     user_id)
                .append("message",  texte)
                .append("date",     date);

        mongo.updateInstances(
                this.collection_name,
                new Document("_id", this.conversation_id),
                new Document("$push", new Document("messages", messsage)),
                new UpdateOptions());

        mongo.updateInstances(
                this.collection_name,
                new Document("_id", this.conversation_id),
                new Document("$set", new Document("last_update", date)),
                new UpdateOptions());
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* printAllConversations()                                                                              */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'afficher toutes les conversations.                                           */
    /* ---------------------------------------------------------------------------------------------------- */

    public void printAllConversations(Integer user_id) {

        System.out.println("-------------------Conversations où l'utilisateur " + user_id + " est vendeur :");
        mongo.getInstances(this.collection_name,
                new Document("vendeur", user_id),
                new Document(),
                new Document());

        System.out.println("--------------------Conversations où l'utilisateur " + user_id + " est client :");
        mongo.getInstances(this.collection_name,
                new Document("client", user_id),
                new Document(),
                new Document());
    }


}
