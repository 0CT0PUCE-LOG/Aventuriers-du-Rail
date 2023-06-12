package fr.umontpellier.iut.rails.mecanique.etatsJoueur.prisecartevisible;

import fr.umontpellier.iut.rails.mecanique.etatsJoueur.EtatJoueur;
import fr.umontpellier.iut.rails.mecanique.Joueur;

public class ReveleNouvelleCarteTransportVisible2 extends EtatJoueur {
    public ReveleNouvelleCarteTransportVisible2(Joueur joueurCourant) {
        super(joueurCourant);
        getJeu().instructionProperty().setValue("Choisissez une pioche pour révéler une carte");
    }

    public boolean piocherWagon() {
        if (!getJeu().piocheWagonEstVide()) {
            joueurCourant.revelerCarteTransport("WAGON");
            finDuTour();
            return true;
        } else {
            if (!getJeu().piocheBateauEstVide())
                getJeu().instructionProperty().setValue("Cette pioche est vide - Choisissez l'autre pioche");
            else
                finDuTour();
            return false;
        }
    }

    public boolean piocherBateau() {
        if (!getJeu().piocheBateauEstVide()) {
            joueurCourant.revelerCarteTransport("BATEAU");
            finDuTour();
            return true;
        } else{
            if (!getJeu().piocheWagonEstVide())
                getJeu().instructionProperty().setValue("Cette pioche est vide - Choisissez l'autre pioche");
            else
                finDuTour();
            return false;
        }
    }
}
