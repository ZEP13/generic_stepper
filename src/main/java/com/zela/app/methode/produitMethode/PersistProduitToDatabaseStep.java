package com.zela.app.methode.produitMethode;

import java.sql.SQLException;
import java.util.List;

import com.zela.app.Step;
import com.zela.app.Db.DbConfig;
import com.zela.app.model.Produit;
import com.zela.app.repository.StepRepo;

public class PersistProduitToDatabaseStep implements Step<Produit> {
    @Override
    public List<Produit> execute(List<Produit> input) {
        try {
            DbConfig dbConfig = DbConfig.fromResource("db.properties");
            StepRepo stepRepo = new StepRepo(dbConfig);

            for (Produit produit : input) {
                try {
                    stepRepo.save(produit);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
}
