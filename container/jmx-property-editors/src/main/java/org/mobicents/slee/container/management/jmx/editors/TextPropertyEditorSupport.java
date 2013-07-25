package org.mobicents.slee.container.management.jmx.editors;

import java.beans.PropertyEditorSupport;

/**
 * @author Eduardo Martins
 */
public class TextPropertyEditorSupport extends PropertyEditorSupport {

    public TextPropertyEditorSupport() {
        super();
    }

    public TextPropertyEditorSupport(final Object source) {
        super(source);
    }

    /**
     * Sets the property value by parsing a given String.
     *
     * @param text  The string to be parsed.
     */
    public void setAsText(final String text) {
        setValue(text);
    }
}
