package com.zela.app.methode.employeMethode;

import java.sql.SQLException;
import java.util.List;

import com.zela.app.Step;
import com.zela.app.Db.DbConfig;
import com.zela.app.model.Employe;
import com.zela.app.repository.StepRepo;

public class RemoveInactiveEmplyeesStep implements Step<Employe> {

    @Override
    public List<Employe> execute(List<Employe> input) {
        try {
            DbConfig dbConfig = DbConfig.fromResource("db.properties");
            StepRepo repo = new StepRepo(dbConfig);

            for (Employe employe : input) {
                if (employe.getActif() == 0) {
                    try {
                        repo.deleteEntityByFieldValue(employe);
                    } catch (SQLException e) {
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
