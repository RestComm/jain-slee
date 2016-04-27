package org.mobicents.slee.container.management.jmx;

/**
 * Created by sergeypovarnin on 27.04.16.
 */
public class ConcreteSbbGeneratorConfiguration implements
        ConcreteSbbGeneratorConfigurationMBean {

    private Boolean initializedWithNull;

    public boolean isInitializedWithNull() { return initializedWithNull; }

    public void setInitializedWithNull(boolean initializedWithNull) {
        if (this.initializedWithNull != null) {
            //logger.warn("Setting initializedWithNull property to "
            //        + initializedWithNull
            //        + ". If called with server running a stop and start is need to apply changes.");
        }
        System.out.println("**************************************************");
        System.out.println("initializedWithNull: " + initializedWithNull);
        System.out.println("**************************************************");
        this.initializedWithNull = initializedWithNull;
    }
}
