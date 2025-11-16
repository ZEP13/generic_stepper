package com.zela.app.methode.produitMethode;

import java.util.List;

import com.zela.app.Step;
import com.zela.app.Db.DbConfig;
import com.zela.app.model.Produit;
import com.zela.app.repository.StepRepo;

public class RemoveOutOfStockStep implements Step<Produit> {

    @Override
    public List<Produit> execute(List<Produit> input) {
        try {
            DbConfig dbConfig = DbConfig.fromResource("db.properties");
            StepRepo repo = new StepRepo(dbConfig);

            for (Produit produit : input) {
                if (produit.getQuantite() < 1) {
                    try {
                        repo.deleteEntityByFieldValue(produit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
}
