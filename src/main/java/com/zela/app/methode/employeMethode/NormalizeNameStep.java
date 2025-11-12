package com.zela.app.methode.employeMethode;

import java.util.List;

import com.zela.app.Step;
import com.zela.app.model.Employe;

public class NormalizeNameStep implements Step<Employe> {

    @Override
    public List<Employe> execute(List<Employe> input) {
        for (Employe employe : input) {
            String nomNormalise = employe.getNom().trim().toUpperCase();
            String prenomNormalise = employe.getPrenom().trim().toLowerCase();
            prenomNormalise = Character.toUpperCase(prenomNormalise.charAt(0)) + prenomNormalise.substring(1);
            employe.setNom(nomNormalise);
            employe.setPrenom(prenomNormalise);
        }
        return input;

    }
}
