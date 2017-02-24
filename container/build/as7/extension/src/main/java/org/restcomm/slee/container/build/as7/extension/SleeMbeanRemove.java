package org.restcomm.slee.container.build.as7.extension;

import org.jboss.as.controller.AbstractRemoveStepHandler;

class SleeMbeanRemove extends AbstractRemoveStepHandler {

    static final SleeMbeanRemove INSTANCE = new SleeMbeanRemove();

    private SleeMbeanRemove() {
    }
}