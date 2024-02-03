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

public class Payment {

    private final String          collection_name  = "colPayments";
    private       Integer         payment_id;
    private       Integer         reservation;
    private       Integer         payeur;
    private       Double          montant;
    private       String          methode_de_paiement;
    private       String          statut_du_paiement;
    private       String          date_du_paiement;

    Mongo mongo = new Mongo();

    /* ---------------------------------------------------------------------------------------------------- */
    /* Payment()                                                                                            */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une instance de la classe Payment.                                    */
    /* ---------------------------------------------------------------------------------------------------- */

    Payment(Integer reservation,
            Integer payeur,
            Double  montant,
            String  methode_de_paiement,
            String  statut_du_paiement) {

        this.payment_id          = mongo.getLastId(this.collection_name) + 1;           // Nouveau identifiant
        this.reservation         = reservation;
        this.payeur              = payeur;
        this.montant             = montant;
        this.methode_de_paiement = methode_de_paiement;
        this.statut_du_paiement  = statut_du_paiement;
        this.date_du_paiement    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        /* ------------------------------------------------------------------------------------------------ */
        /* Insertion du paiement dans la base de données                                                    */
        /* ------------------------------------------------------------------------------------------------ */

        Document payment = new Document("_id",                  this.payment_id)
                            .append("reservation",              this.reservation)
                            .append("payeur",                   this.payeur)
                            .append("montant",                  this.montant)
                            .append("methode_de_paiement",      this.methode_de_paiement)
                            .append("statut_du_paiement",       this.statut_du_paiement)
                            .append("date_du_paiement",         this.date_du_paiement);

        mongo.insertInstanceCollection(this.collection_name, payment);           // Insert payment in database

        /* ------------------------------------------------------------------------------------------------ */
        /* Update reservation status                                                                        */
        /* ------------------------------------------------------------------------------------------------ */

        mongo.updateInstances(
                "colReservations",
                new Document("_id", this.reservation),
                new Document("$set", new Document("reservation_status", "PAID")),
                new UpdateOptions());

        System.out.println("Payment processed successfully.");

    }
    Payment(){}

    /* ---------------------------------------------------------------------------------------------------- */
    /* getPaymentById()                                                                                     */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de récupérer un paiement par son identifiant.                                  */
    /* ---------------------------------------------------------------------------------------------------- */

    public Payment getPaymentById(Integer id) {

        Payment payment         = new Payment();
        Document payment_doc    = mongo.getInstanceById(this.collection_name, id);

        payment.payment_id          = payment_doc.getInteger("_id");
        payment.reservation         = payment_doc.getInteger("reservation");
        payment.payeur              = payment_doc.getInteger("payeur");
        payment.montant             = payment_doc.getDouble("montant");
        payment.methode_de_paiement = payment_doc.getString("methode_de_paiement");
        payment.statut_du_paiement  = payment_doc.getString("statut_du_paiement");
        payment.date_du_paiement    = payment_doc.getString("date_du_paiement");

        return payment;
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* deletePayment()                                                                                      */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer un payment.                                                       */
    /* ---------------------------------------------------------------------------------------------------- */

    public void deletePayment() {
        mongo.deleteFromCollection(this.collection_name,new Document("_id", this.payment_id));
    }

}
