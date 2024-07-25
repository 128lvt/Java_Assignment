package com.project.assignment.systems;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not find " + objectName + " with Id " + id + " :(");
    }

}
