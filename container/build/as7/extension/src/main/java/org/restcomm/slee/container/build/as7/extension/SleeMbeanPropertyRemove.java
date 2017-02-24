package org.restcomm.slee.container.build.as7.extension;

import org.jboss.as.controller.AbstractRemoveStepHandler;

class SleeMbeanPropertyRemove extends AbstractRemoveStepHandler {

    public static final SleeMbeanPropertyRemove INSTANCE = new SleeMbeanPropertyRemove();

    private SleeMbeanPropertyRemove() {
    }
}