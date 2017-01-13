package com.example.entity;

import java.util.HashMap;

import javax.lang.model.element.VariableElement;

/**
 * Created by WZG on 2017/1/11.
 */

public class VariMsg {
    /*包名*/
    private String pk;
    /*类名*/
    private String clsName;
    /*注解对象*/
    private HashMap<Integer,VariableElement> varMap;

    public VariMsg(String pk) {
        setPk(pk);
        varMap=new HashMap<>();
    }

    public VariMsg(String pk, String clsName) {
        setPk(pk);
        setClsName(clsName);
        varMap=new HashMap<>();
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public HashMap<Integer, VariableElement> getVarMap() {
        return varMap;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }
}
