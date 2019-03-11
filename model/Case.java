package pendu.model; /**
 * Created by Ibrahim on 19/04/2017.
 */
public abstract class Case implements Malus {
    protected Character lettre;
    protected boolean trouve = false;
    protected int score = 0;

    public int getNbtentatives() {
        return nbtentatives;
    }

    protected int nbtentatives = 0;

    abstract public boolean entrerLettre (Character lettre);

    public int getScore ()
    {
        return score;
    }

    public int calculerMalus ()
    {
        return 0;
    }

    public Character getLettre ()
    {
        return lettre;
    }
}
