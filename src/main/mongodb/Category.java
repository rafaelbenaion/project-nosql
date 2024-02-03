/* -------------------------------------------------------------------------------------------------------- */
/* Projet d’Approfondissement - BD vers Big Data                                                            */
/* -------------------------------------------------------------------------------------------------------- */
/* 12 janvier 2023, Université Côte d'Azur.                                                                 */
/* BAPTISTA Rafael, BOULLI Marouan, MISSAOUI Oumayma & SONG Yu.                                             */
/* -------------------------------------------------------------------------------------------------------- */

package main.mongodb;
import  org.bson.Document;

public class Category extends Document {

    private final String  collection_name  = "colCategories";
    public        Integer category_id;
    public        String  category_name;

    Mongo mongo = new Mongo();

    /* ---------------------------------------------------------------------------------------------------- */
    /* Category()                                                                                           */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de creer une instance de la classe Category et de l'insérer dans la base.      */
    /* ---------------------------------------------------------------------------------------------------- */

    public Category(String name) {

        this.category_id   = mongo.getLastId(this.collection_name) + 1;    // Création d'un nouvel identifiant
        this.category_name = name;

        Document category = new Document("_id",   this.category_id)
                                .append("category_name", this.category_name);

        mongo.insertInstanceCollection(this.collection_name, category);         // Insert category in database

    }

    public Category() {
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getCategoryById()                                                                                    */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de récupérer une catégorie par son identifiant.                                */
    /* ---------------------------------------------------------------------------------------------------- */

    public Category getCategoryById(Integer id) {

        Category category      = new Category();
        Document category_doc  = mongo.getInstanceById(this.collection_name, id);
        category.category_id   = category_doc.getInteger("_id");
        category.category_name = category_doc.getString("category_name");

        return category;
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* deleteCategories()                                                                                   */
    /* ---------------------------------------------------------------------------------------------------- */
    /* Cette fonction permet de supprimer une ou plusieurs categories.                                      */
    /* ---------------------------------------------------------------------------------------------------- */

    public void deleteCategories(Document whereQuery) {
        mongo.deleteFromCollection(this.collection_name, whereQuery);
    }

    /* ---------------------------------------------------------------------------------------------------- */
    /* getId()                                                                                              */
    /* ---------------------------------------------------------------------------------------------------- */

    public Integer getId() {
        return this.category_id;
    }


}
