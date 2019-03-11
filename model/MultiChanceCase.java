package pendu.model; /**
 * Created by Ibrahim on 19/04/2017.
 */
public class MultiChanceCase extends Case implements Malus {

    public MultiChanceCase (Character lettre)
    {
        this.lettre = lettre;
    }

    public int calculerMalus ()
    {
        return nbtentatives*2;
    }

    public boolean entrerLettre (Character lettre)
    {
        if (this.lettre == lettre)
        {
            trouve = true;
            score = 1;
            return true;
        }
        else
        {
            nbtentatives++;
            return false;
        }
    }
}
