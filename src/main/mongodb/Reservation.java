/* -------------------------------------------------------------------------------------------------------- */
/* Projet d’Approfondissement - BD vers Big Data                                                            */
/* -------------------------------------------------------------------------------------------------------- */
/* 12 janvier 2023, Université Côte d'Azur.                                                                 */
/* BAPTISTA Rafael, BOULLI Marouan, MISSAOUI Oumayma & SONG Yu.                                             */
/* -------------------------------------------------------------------------------------------------------- */

package main.mongodb;


import com.mongodb.internal.connection.Time;
import org.bson.Document;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Reservation {

    private final String      collection_name       = "colReservations";
    private       Integer     reservation_id;
    private       Integer     renter;
    private       Good        good_reserved;
    private       Integer     rental_period;
    private       Double      total_cost;
    private       String      reservation_status;
    private       String      created_at;

    Mongo mongo                 = new Mongo();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));;

    /* ---------------------------------------------------------------------------------------------------- */
    /* Reservation()                                                                                        */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une instance de la classe Reservation.                                */
    /* ---------------------------------------------------------------------------------------------------- */

    public Reservation(Integer     renter,
                       Good        good_reserved,
                       Integer     rental_period,
                       String      reservation_status) {

        this.reservation_id     = mongo.getLastId(this.collection_name) + 1;            // Nouveau identifiant
        this.renter             = renter;
        this.good_reserved      = good_reserved;
        this.rental_period      = rental_period;
        this.reservation_status = reservation_status;


        this.created_at         = formatter.format(Instant.now());

        /* ------------------------------------------------------------------------------------------------ */
        /* Calcul du prix total de la réservation                                                           */
        /* ------------------------------------------------------------------------------------------------ */

        this.total_cost = (good_reserved.getPricePerDay() * (double) this.rental_period);

        /* ------------------------------------------------------------------------------------------------ */
        /* Insertion de la réservation dans la base de données                                              */
        /* ------------------------------------------------------------------------------------------------ */

        Document reservation = new Document("_id",                  this.reservation_id)
                                    .append("renter",               this.renter)
                                    .append("good_reserved",        this.good_reserved.getId())
                                    .append("rental_period",        this.rental_period)
                                    .append("total_cost",           this.total_cost)
                                    .append("reservation_status",   this.reservation_status)
                                    .append("created_at",           this.created_at);

        mongo.insertInstanceCollection(this.collection_name, reservation);           // Sauvegarde reservation

        System.out.println("Reservation created successfully.");
    }

    public Reservation() {
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* deleteReservationsFromUser()                                                                         */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer une ou plusieurs réservations d'un utilisateur.                   */
    /* ---------------------------------------------------------------------------------------------------- */

    public void deleteReservationsFromUser(Integer renter_id) {
        Document whereQuery = new Document("renter", renter_id);
        mongo.deleteFromCollection(this.collection_name, whereQuery);
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* payReservation()                                                                                     */
    /* ---------------------------------------------------------------------------------------------------- */

    public void payReservation(){
        Payment payment = new Payment(
                this.reservation_id,
                this.renter,
                this.total_cost,
                "CB",
                "PAID");
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getReservationById()                                                                                 */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de récupérer une réservation par son identifiant.                              */
    /* ---------------------------------------------------------------------------------------------------- */

    public Reservation getReservationById(Integer id) {

        Reservation reservation         = new Reservation();
        Document reservation_doc        = mongo.getInstanceById(this.collection_name, id);

        reservation.reservation_id      = reservation_doc.getInteger("_id");
        reservation.renter              = reservation_doc.getInteger("renter");
        reservation.good_reserved       = new Good().getGoodById(reservation_doc.getInteger("good_reserved"));
        reservation.rental_period       = reservation_doc.getInteger("rental_period");
        reservation.total_cost          = reservation_doc.getDouble("total_cost");
        reservation.reservation_status  = reservation_doc.getString("reservation_status");
        reservation.created_at          = reservation_doc.getString("created_at");

        return reservation;
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getAllActiveReservations()                                                                           */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de récupérer toutes les réservations actives.                                  */
    /* ---------------------------------------------------------------------------------------------------- */

    public void getAllActiveReservations() {

        mongo.getInstances(this.collection_name,
                new Document("reservation_status", "Active"),
                new Document(),
                new Document());

    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* nbOfReservationsForEachCategory()                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */

    /* ---------------------------------------------------------------------------------------------------- */
    /* sumReservationsCostByUser()                                                                          */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de récupérer le montant total des réservations des utilisateurs.               */
    /* ---------------------------------------------------------------------------------------------------- */

    public void sumReservationsCostByUser() {

        System.out.println("\n\nTotal cost of reservations by user :");

        String groupOperator = "$group";
        Document groupFields = new Document("_id", "$renter")
                .append("totalCost", new Document("$sum", "$total_cost"));

        mongo.groupByTotalCost(this.collection_name, groupOperator, groupFields);
    }

}
