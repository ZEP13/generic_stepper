package com.zela.app.methode.employeMethode;

import java.sql.SQLException;
import java.util.List;

import com.zela.app.Step;
import com.zela.app.Db.DbConfig;
import com.zela.app.model.Employe;
import com.zela.app.repository.StepRepo;

public class PersistToDatabaseStep implements Step<Employe> {
    @Override
    public List<Employe> execute(List<Employe> input) {
        try {
            DbConfig dbConfig = DbConfig.fromResource("db.properties");
            StepRepo stepRepo = new StepRepo(dbConfig);

            for (Employe employe : input) {
                try {
                    stepRepo.save(employe);
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
