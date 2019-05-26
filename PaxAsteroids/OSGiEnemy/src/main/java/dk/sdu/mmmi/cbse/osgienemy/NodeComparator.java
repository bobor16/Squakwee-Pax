/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgienemy;

import java.util.Comparator;

/**
 *
 * @author rasmu
 */
public class NodeComparator implements Comparator<Node>{
    public int compare(Node n1, Node n2){
        if (n1.getF() < n2.getF()) {
            return -1;
        } else if (n1.getF() > n2.getF()) {
            return 1;
        }
        return 0;
    }
}
