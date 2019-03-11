package pendu.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

/**
 * Created by Ibrahim on 19/04/2017.
 */
public class Proposition extends ZeroChanceCase implements Malus {
    private ArrayList<Character> character;
    private String[] propositions;
    private final int nbpropositions = 4;

    public Proposition (Character lettre)
    {
        this.lettre = lettre;
        String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        propositions = new String[nbpropositions];

        int position1;
        int position2;
        Random r = new Random();
        position1 = r.nextInt(nbpropositions);
        propositions[position1] = lettre.toString();
        for (int i=0;i<nbpropositions;i++)
        {
            if (i!=position1)
            {
                position2 = r.nextInt(26);
                while (contain(propositions,alphabet[position2]))
                {
                    position2 = r.nextInt(26);
                }
                propositions[i] = alphabet[position2];
            }
        }
    }

    public int calculerMalus ()
    {
        return nbtentatives*1;
    }

    public boolean entrerLettre (Character lettre)
    {
        if (this.lettre == lettre)
        {
            trouve = true;
            score = 2;
            return true;
        }
        else
        {
            nbtentatives++;
            return false;
        }
    }

    public String[] getPropositions () {
        return propositions;
    }

    public boolean contain (String[] mot, String lettre) {
        boolean trouve = false;
        boolean stop = false;
        int i=0;
        while ((!trouve) && (!stop))
        {
            if (mot[i]!=null)
            {
                if (mot[i].compareTo(lettre)==0)
                {
                    trouve = true;
                }
            }
            i++;
            if (i==nbpropositions)
            {
                stop = true;
            }
        }
        return trouve;
    }
}
