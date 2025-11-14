package com.zela.app;

import java.util.ArrayList;
import java.util.List;

import com.zela.app.model.Employe;
import com.zela.app.pipeline.ProcessingPipeline;
import com.zela.app.repository.StepRepo;
import com.zela.app.Db.DbConfig;
import com.zela.app.methode.employeMethode.NormalizeNameStep;
import com.zela.app.methode.employeMethode.PersistEmployeToDatabaseStep;
import com.zela.app.methode.employeMethode.RemoveInactiveEmplyeesStep;

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
