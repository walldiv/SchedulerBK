package com.scheduler.bkend.model;

import java.lang.reflect.Field;
import java.util.List;

import static org.springframework.data.util.CastUtils.cast;

public interface MyClassUtils {

    public default <T> T merge(T inObject) throws IllegalAccessException, InstantiationException {
        Class<?> clazz = inObject.getClass();
        Object merged = clazz.newInstance();
        for(Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object local = field.get(this);
            Object remote = field.get(inObject);
//            System.out.printf("FIELD CLASS TYPE: %s \n", local.getClass().getSimpleName());
            System.out.printf("ANNOTATED TYPE: %s \n", field.getAnnotatedType().toString());
            if(field.getAnnotatedType().toString().equals("int")){
                System.out.printf("INT FIELD FOUND - VAL: %s \n", field.get(this));
                field.set(merged, ((int)remote == 0) ? local : remote);
            }
            else if(field.getAnnotatedType().toString().equals("java.lang.String")){
                System.out.printf("STRING FIELD FOUND - VAL: %s \n", field.get(this));
                field.set(merged, (remote != null) ? remote : local);
            }
            else if(field.getAnnotatedType().toString().equals("java.time.LocalDate")){
                System.out.printf("LOCALDATE FIELD FOUND - VAL: %s \n", field.get(this));
                field.set(merged, (remote != null) ? remote : local);
            }
            else if(field.getAnnotatedType().toString().equals("boolean")){
                System.out.printf("BOOLEAN FIELD FOUND - VAL: %s \n", field.get(this));
                field.set(merged, remote);
            }
            else if(field.getAnnotatedType().toString().equals("java.time.LocalDateTime")){
                System.out.printf("LOCALDATETIME FIELD FOUND - VAL: %s \n", field.get(this));
                field.set(merged, (remote != null) ? remote : local);
            }
            else if(field.getAnnotatedType().toString().equals("java.util.List<java.lang.String>")){
                System.out.printf("LIST<STRING> FIELD FOUND - VAL: %s \n", field.get(this));
                System.out.printf("LIST TO CHANGE TO - VAL: %s", remote);
                List<String> list = cast(remote);
                field.set(merged, (list.size() == 0) ? local : list);
            }
            //This final catch-all handles complex custom object types - simply uses the remote if remote != null
            //this isnt intended for merging of sub-objects to the main object...simply for a use-case of JSON objects
            //being passed into the main object and utilizing that JSON-mapped data to an object.
            else{
                System.out.printf("OTHER TYPE FOUND: \n" + field.get(this) + "\n");
                field.set(merged, (remote != null) ? remote : local);
            }
        }
        System.out.printf("FINAL OBJECT => %s", merged.toString());
        return (T)merged;
    }

}
