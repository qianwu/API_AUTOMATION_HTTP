package com.api.wq.helper;

import java.util.ResourceBundle;

public enum  PropertiesRepository {
    /**
     *
     */
    DATAREPOSITORY("data"),

    ACCOUNTREPOSITORY("accounts"),

    TOUDAREPOSITORY("touda"),

    CONTEXTREPOSITORY("context"),
    APPLICATIONREPOSITORY("application");

    String fileName;

    PropertiesRepository(String fileName){
        this.fileName = "selectedEnv/" + fileName;
        readValue();
    }

    ResourceBundle resourceBundle;

    private void readValue(){
        try{
            resourceBundle = ResourceBundle.getBundle(fileName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return resourceBundle.getString(key);
    }
}
