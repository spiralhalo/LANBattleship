/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbs.net;

/**
 *
 * @author lenovo z40
 */
public class CommsResult {

    private int type;
    private String result;
            
    public CommsResult(){ }
    
    public void set(int type, String result){
        this.type = type;
        this.result = result;
    }
    
    public int consumeType(){
        int temp = type;
        type = 0;
        return temp;
    }
    
    public String consumeResult(){
        String temp = ""+result;
        result = null;
        return temp;
    }
}
