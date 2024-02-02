/* -------------------------------------------------------------------------------------------------------- */
/* Projet d’Approfondissement - BD vers Big Data                                                            */
/* -------------------------------------------------------------------------------------------------------- */
/* 12 janvier 2023, Université Côte d'Azur.                                                                 */
/* BAPTISTA Rafael, BOULLI Marouan, MISSAOUI Oumayma & SONG Yu.                                             */
/* -------------------------------------------------------------------------------------------------------- */

package main.mongodb;

import jdk.jfr.Category;
import org.bson.Document;

public class Good extends Document{

    private final String      collection_name  = "colGoods";
    private       Integer     goods_id;
    private       String      title;
    private       String      description;
    private       Double      price_per_day;
    private       String      images_url;
    private       String      coordonees_gps;
    private       Boolean     availability_status;
    private       Integer     category;
    private       Integer     owner;

    Mongo mongo = new Mongo();

    /* ---------------------------------------------------------------------------------------------------- */
    /* Good()                                                                                               */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une instance de la classe Good.                                       */
    /* ---------------------------------------------------------------------------------------------------- */

    public Good(String      title,
                String      description,
                Double      price_per_day,
                String      images_url,
                String      coordonees_gps,
                Boolean     availability_status,
                Integer     category,
                Integer     owner) {

        this.title               = title;
        this.description         = description;
        this.price_per_day       = price_per_day;
        this.images_url          = images_url;
        this.coordonees_gps      = coordonees_gps;
        this.availability_status = availability_status;
        this.category            = category;
        this.owner               = owner;
    }

    public Good() {
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* insertGood()                                                                                         */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet d'insérer un objet Good dans la base de données.                               */
    /* ---------------------------------------------------------------------------------------------------- */

    public void insertGood() {

        /* ------------------------------------------------------------------------------------------------ */
        /* Defining Goods ID                                                                                */
        /* ------------------------------------------------------------------------------------------------ */

        this.goods_id = mongo.getLastId(this.collection_name) + 1;

        Document good = new Document("_id",              this.goods_id)
                          .append("title",               this.title)
                          .append("description",         this.description)
                          .append("price_per_day",       this.price_per_day)
                          .append("images_url",          this.images_url)
                          .append("coordonees_gps",      this.coordonees_gps)
                          .append("availability_status", this.availability_status)
                          .append("category",            this.category)
                          .append("owner",               this.owner);

        mongo.insertInstanceCollection(this.collection_name, good);
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* deleteGoods()                                                                                        */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer un ou plusieurs goods.                                            */
    /* ---------------------------------------------------------------------------------------------------- */

    public void deleteGoods(Document whereQuery) {
        mongo.deleteFromCollection(this.collection_name, whereQuery);
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getGoodById()                                                                                        */
    /* ---------------------------------------------------------------------------------------------------- */

    public Good getGoodById(Integer id) {

        Good good                   = new Good();
        Document good_doc           = mongo.getInstanceById(this.collection_name, id);
        good.goods_id               = good_doc.getInteger("_id");
        good.title                  = good_doc.getString("title");
        good.description            = good_doc.getString("description");
        good.price_per_day          = good_doc.getDouble("price_per_day");
        good.images_url             = good_doc.getString("images_url");
        good.coordonees_gps         = good_doc.getString("coordonees_gps");
        good.availability_status    = good_doc.getBoolean("availability_status");
        good.category               = good_doc.getInteger("category");
        good.owner                  = good_doc.getInteger("owner");

        return good;
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getPricePerDay()                                                                                     */
    /* ---------------------------------------------------------------------------------------------------- */

    public Double getPricePerDay(Integer id) {

        Document good_doc = mongo.getInstanceById(this.collection_name, id);
        return good_doc.getDouble("price_per_day");
    }

}
