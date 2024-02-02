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

public class Reservation {

    private final String      collection_name       = "colReservations";
    private       Integer     reservation_id;
    private       Integer     renter;
    private       Integer     good_reserved;
    private       Integer     rental_period;
    private       Double      total_cost;
    private       String      reservation_status;
    private       Instant     created_at;

    Mongo mongo = new Mongo();

    /* ---------------------------------------------------------------------------------------------------- */
    /* Reservation()                                                                                        */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une instance de la classe Reservation.                                */
    /* ---------------------------------------------------------------------------------------------------- */

    public Reservation(Integer    renter,
                       Integer     good_reserved,
                       Integer     rental_period,
                       String      reservation_status) {

        this.reservation_id     = mongo.getLastId(this.collection_name) + 1;            // Nouveau identifiant
        this.renter             = renter;
        this.good_reserved      = good_reserved;
        this.rental_period      = rental_period;
        this.reservation_status = reservation_status;
        this.created_at         = Instant.now();

        /* ------------------------------------------------------------------------------------------------ */
        /* Calcul du prix total de la réservation                                                           */
        /* ------------------------------------------------------------------------------------------------ */

        Good good       = new Good();
             good       = good.getGoodById(this.good_reserved);
        this.total_cost = good.getPricePerDay(this.good_reserved) * this.rental_period;

        /* ------------------------------------------------------------------------------------------------ */
        /* Insertion de la réservation dans la base de données                                              */
        /* ------------------------------------------------------------------------------------------------ */

        Document reservation = new Document("_id",                  this.reservation_id)
                                    .append("renter",               this.renter)
                                    .append("good_reserved",        this.good_reserved)
                                    .append("rental_period",        this.rental_period)
                                    .append("total_cost",           this.total_cost)
                                    .append("reservation_status",   this.reservation_status)
                                    .append("created_at",           this.created_at);

        mongo.insertInstanceCollection(this.collection_name, reservation);           // Sauvegarde reservation
    }

    public Reservation() {
    }

}
