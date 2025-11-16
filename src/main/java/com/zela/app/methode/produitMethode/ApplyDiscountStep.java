package com.zela.app.methode.produitMethode;

import java.util.List;

import com.zela.app.Step;
import com.zela.app.Db.DbConfig;
import com.zela.app.model.Produit;
import com.zela.app.repository.StepRepo;

public class ApplyDiscountStep implements Step<Produit> {

    double discount;

    public ApplyDiscountStep(double discount) {
        this.discount = discount;
    }

    @Override
    public List<Produit> execute(List<Produit> input) {
        try {
            DbConfig dbConfig = DbConfig.fromResource("db.properties");
            StepRepo repo = new StepRepo(dbConfig);
            for (Produit produit : input) {
                try {
                    repo.findEntityByField("prix");

                    Double newPrice = produit.getPrix() - (produit.getPrix() * discount / 100);
                    String fieldName = "prix";
                    if (newPrice >= 0) {
                        repo.updateFieldValue(fieldName, String.valueOf(newPrice), produit.getEntityId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("erreur de la recuperation des id des produits");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
}
