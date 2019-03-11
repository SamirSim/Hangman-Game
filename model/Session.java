package pendu.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import java.io.*;
import java.util.*;

/**
 * Created by microbox on 19/04/2017.
 */
public class Session
{
    private final int echecMax = 6;
    private ArrayList<Mot> mots;
    private Joueur joueurActuel;

    public void setNbEssaisRestants(int nbEssaisRestants) {
        this.nbEssaisRestants = nbEssaisRestants;
    }

    private int nbEssaisRestants = echecMax;
    private final int nbmot = 10;

    public void setScoreActuel(int scoreActuel) {
        this.scoreActuel = scoreActuel;
    }

    private int scoreActuel;
    private int numMotActuel=1;

    public int getNumMotActuel() {
        return numMotActuel;
    }

    public void setNumMotActuel(int numMotActuel) {
        this.numMotActuel = numMotActuel;
    }

    public Session()
    {
        //this.joueurActuel = joueurActuel;
        this.scoreActuel = 0;
        Random random = new Random();
        FastScanner fastScanner = newInput();
        importerMots(random.nextInt(fastScanner.nbLignes()-nbmot-1));
    }

    private void importerMots(int pos) {
        mots = new ArrayList<Mot>();
        FastScanner fastScanner = newInput();
        Mot mot;
        String m;
        String type;
        TypeIndication typeIndication;
        String ind;

        for (int i=0; i<nbmot+pos+1; i++)
        {
            //System.out.println("ici");
            if (i<pos)
            {
                fastScanner.next();
                fastScanner.next();
                fastScanner.next();
            }
            else
            {
                type =  fastScanner.next();
                if (type.contains("\uFEFF")) {
                    type = type.substring(1);
                }
                ind = fastScanner.next();
                m = fastScanner.next();
                if (type.compareTo("SYNONYME")==0)
                {
                    typeIndication = TypeIndication.SYNONYME;
                }
                else
                {
                    if (type.compareTo("DEFINITION")==0)
                    {
                        typeIndication = TypeIndication.DEFINITION;
                    }
                    else
                    {
                        typeIndication = TypeIndication.ANTONYME;
                    }
                }
                mot = new Mot(m,typeIndication,ind);
                if (m.length()>6)
                {
                    mots.add(mot);
                }
            }
        }
    }

    public void miseAjourScore()
    {

    }

    public Joueur getJoueurActuel()
    {
        return this.joueurActuel;
    }

    public ArrayList<Mot> getMots () {
        return mots;
    }

    static class FastScanner {
        static BufferedReader br;
        static StringTokenizer st;

        FastScanner(File f) {
            try {
                br = new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        private FastScanner(InputStream f) {
            br = new BufferedReader(new InputStreamReader(f));
        }
        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine(),";");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        public int nbLignes () {
            return Integer.parseInt(br.lines().count()+"");
        }
    }

    private FastScanner newInput() {
        try {
            return new FastScanner(new File("src/mots.poo"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getNbEssaisRestants () {
        return nbEssaisRestants;
    }

    public int getScoreActuel () {
        return scoreActuel;
    }
}
