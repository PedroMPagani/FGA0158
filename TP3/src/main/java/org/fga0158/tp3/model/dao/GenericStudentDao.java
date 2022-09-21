package org.fga0158.tp3.model.dao;

public abstract class GenericStudentDao<T> {

    /**
     * Returns a LONGTEXT in which we will serialize to DB, we could use a binary serialization too but most libraries that are fast are not
     * stable for production env, even tho they're way faster than some sort of JSON serializer.
     * @param user instance of class that will serialize.
     * @return string json format
     */
    public abstract String serialize(T user);

}