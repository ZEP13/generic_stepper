package com.zela.app;

import java.util.ArrayList;
import java.util.List;

import com.zela.app.model.Employe;
import com.zela.app.model.Produit;
import com.zela.app.pipeline.ProcessingPipeline;
import com.zela.app.repository.StepRepo;
import com.zela.app.Db.DbConfig;
import com.zela.app.methode.employeMethode.NormalizeNameStep;
import com.zela.app.methode.employeMethode.PersistEmployeToDatabaseStep;
import com.zela.app.methode.employeMethode.RemoveInactiveEmplyeesStep;
import com.zela.app.methode.produitMethode.PersistProduitToDatabaseStep;
import com.zela.app.methode.produitMethode.RemoveOutOfStockStep;

public class App {
    public static void main(String[] args) {
        try {

            DbConfig dbConfig = DbConfig.fromResource("db.properties");
            StepRepo repo = new StepRepo(dbConfig);

            Employe employe = new Employe("Doe", "John", 30, 1);
            repo.save(employe);
            List<Employe> employes = new ArrayList<>();
            employes.add(new Employe("  doe  ", "JOHN", 30, 1));
            employes.add(new Employe("  smith  ", "jANE", 25, 1));
            employes.add(new Employe("brown ", "ALICE ", 40, 0));

            List<Produit> produit = new ArrayList<>();
            produit.add(new Produit("Ordinateur", 1200.0, 10));
            produit.add(new Produit("Souris", 25.0, 100));
            produit.add(new Produit("Clavier", 45.0, 50));
            produit.add(new Produit("telephone", 123.3, 0));

            ProcessingPipeline<Produit> produitPipeline = new ProcessingPipeline<>();
            produitPipeline.addStep(new PersistProduitToDatabaseStep());
            // produitPipeline.addStep(new ApplyDiscountStep(10.0));
            produitPipeline.addStep(new RemoveOutOfStockStep());

            ProcessingPipeline<Employe> pipeline = new ProcessingPipeline<>();

            // pipeline.addStep(new NormalizeNameStep());
            // pipeline.addStep(new PersistEmployeToDatabaseStep());
            pipeline.addStep(new RemoveInactiveEmplyeesStep());
            List<Employe> result = pipeline.execute(employes);

            System.out.println("=== Employés après traitement ===");
            for (Employe e : result) {
                System.out.println(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
