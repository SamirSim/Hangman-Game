package pendu.model; /**
 * Created by Ibrahim on 19/04/2017.
 */
public class ZeroChanceCase extends Case {

    public ZeroChanceCase (){}

    public ZeroChanceCase (Character lettre)
    {
        this.lettre = lettre;
    }

    public boolean entrerLettre (Character lettre)
    {
        if (this.lettre == lettre) {
            trouve = true;
            score = 3;
            return true;
        } else {
            nbtentatives++;
            return false;
        }
    }

}
