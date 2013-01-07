/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Warfa
 */
public class DataBaseClient {

    String path;
    Map<String, TeamSheet> teamSheets;
    Map<String, MatchSheet> matchSheets;
    
    DataBaseClient(String path) {
        this.path = path;
        teamSheets = new HashMap();
        matchSheets = new HashMap();
    }

    public void update(String teamName) throws FileNotFoundException {
        File dir = new File(path + "\\" + teamName);
        for (File child : dir.listFiles()) {
            addSheet(child);
        }
    }
    public void addSheet(File child) throws FileNotFoundException{
        FileReader fl = new FileReader(child);
        
    }
    public TeamSheet lookUp(String teamName){
        return null;
    }
}
