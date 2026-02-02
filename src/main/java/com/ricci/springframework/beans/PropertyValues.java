package com.ricci.springframework.beans;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {

    private final List<PropertyValue> propertyValueList=new ArrayList<>();

    public void addPropertyValue(PropertyValue pv){
        propertyValueList.add(pv);
    }

    public List<PropertyValue> getPropertyValues(){
        return propertyValueList;
    }

    public PropertyValue getPropertyValue(String propertyName){
        for(PropertyValue pv:propertyValueList){
            if(pv.getName()==propertyName){
                return pv;
            }
        }
        return null;
    }
}
