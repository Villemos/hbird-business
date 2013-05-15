package org.hbird.exchange.interfaces;

/**
 * IEntityInstance which is applicable to another IEntity or IEntityInstance.
 * 
 * For example start event is applicable to IStartabelEntity, state is applicable to parameter etc.
 */
public interface IApplicableTo {

    /**
     * Method to retrieve the identifier of the IEntityInstance that this IEntityInstace is retrieved from.
     * 
     * @return String in the format '[id]:[timestamp]'
     */
    public String getApplicableTo();
}
