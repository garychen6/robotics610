/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksiserver;

import java.util.Comparator;

/**
 *
 * @author Warfa
 */
public class TeamComparator implements Comparator<TeamSheet>
{
    
    public int compare(TeamSheet x, TeamSheet y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust
        if (x.getKPR() < y.getKPR())
        {
            return 1;
        }
        if (x.getKPR() > y.getKPR())
        {
            return -1;
        }
        return 0;
    }
}
