/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Warfa
 */
public class DataBaseClient {

    String path;
    Map<String, TeamSheet> dataBase;

    DataBaseClient(String path) {
        this.path = path;
        dataBase = new HashMap();
    }

    public void update(String teamName) throws FileNotFoundException {
        File dir = new File(path + "\\" + teamName);
        TeamSheet team = new TeamSheet(teamName);
        for (File child : dir.listFiles()) {
            team.addSheet(child);
        }
        dataBase.put(teamName, team);
    }
    
    public TeamSheet lookUp(String teamName){
        return dataBase.get(teamName);
    }
}
