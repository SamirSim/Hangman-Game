package pendu.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ibrahim on 19/04/2017.
 */
public class Mot {
    private String chaine;
    private ArrayList<Case> cases;
    private TypeIndication typeindication;
    private String indication;
    private int coeff;

    public int getScore() {
        return score;
    }

    private int score;

    public Mot(String chaine, TypeIndication typeindication, String indication) {
        this.chaine = chaine;
        // générer le tableau des cases
        int length = chaine.length();
        Random r = new Random();
        int sommezeroprop = r.nextInt(4);
        sommezeroprop = r.nextInt(4);
        sommezeroprop = r.nextInt(4);
        int nbzerochance = r.nextInt(sommezeroprop+1);
        int nbproposition = r.nextInt(sommezeroprop-nbzerochance+1);
        int nbmultichances = length - nbzerochance - nbproposition;
        cases = new ArrayList<Case>();
        int[] pos = new int[length];
        int position;
        while (nbzerochance>0)
        {
            position = r.nextInt(length);
            if (pos[position]==0)
            {
                pos[position] = 1;
                nbzerochance--;
            }
        }
        while (nbproposition>0)
        {
            position = r.nextInt(length);
            if (pos[position]==0)
            {
                pos[position] = 2;
                nbproposition--;
            }
        }
        while (nbmultichances>0)
        {
            position = r.nextInt(length);
            if (pos[position]==0)
            {
                pos[position] = 3;
                nbmultichances--;
            }
        }
        for (int i=0;i<length;i++)
        {
            if (pos[i]==1)
            {
                cases.add(new ZeroChanceCase(chaine.charAt(i)));
            }
            else
            {
                if (pos[i]==2)
                {
                    cases.add(new Proposition(chaine.charAt(i)));
                }
                else
                {
                    cases.add(new MultiChanceCase(chaine.charAt(i)));
                }
            }
        }

        // tableau des cases est généré
        this.typeindication = typeindication;
        this.indication = indication;
        if (typeindication==TypeIndication.DEFINITION) this.coeff = 3;
        if (typeindication==TypeIndication.SYNONYME) this.coeff = 2;
        if (typeindication==TypeIndication.ANTONYME) this.coeff = 1;
    }

    public void calculerScore ()
    {
        int malus = 0;
        int nbrCasesMalus = 0;
        for (Case c: cases)
        {
            this.score += c.getScore();
            malus += c.calculerMalus();
            if ((c instanceof MultiChanceCase) || (c instanceof Proposition))
            {
                nbrCasesMalus++;
            }
        }
        this.score=this.score*coeff;
        if (nbrCasesMalus > 5)
        {
            this.score -= malus;
        }
    }

    public void afficher () // afficher le mot case par case pour le test
    {
        /*
        for (Case c:cases)
        {
            if (c instanceof Proposition)
            {
                System.out.println("Proposition : " + c.getLettre());
            }
            else
            {
                if (c instanceof ZeroChanceCase)
                {
                    System.out.println("Zero chance case : " + c.getLettre());
                }
                else
                {
                    System.out.println("Multi chance case : " + c.getLettre());
                }
            }
        }
        */
        //System.out.println("Mot : "+chaine);
        //System.out.println("Indication : "+indication);
    }

    public ArrayList<Case> getCases () {
        return cases;
    }

    public String getIndication () {
        return indication;
    }

    public String getTypeIndication () {
        return typeindication.toString();
    }
}
