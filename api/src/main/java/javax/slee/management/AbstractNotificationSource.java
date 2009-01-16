package javax.slee.management;

abstract class AbstractNotificationSource implements NotificationSource {
    /**
     * Get the class name of this notification source.  More efficient than
     * <code>getClass().getName()</code>.
     * @return the class name of this notification source.
     */
    protected abstract String getClassName();

    protected final int compareTo(String thisClassName, Object that) {
        if (that instanceof AbstractNotificationSource) {
            // a known notification source, so use it's quick-access class name
            return thisClassName.compareTo(((AbstractNotificationSource)that).getClassName());
        }
        else if (that instanceof NotificationSource) {
            // just compare using the other object's class name
            return thisClassName.compareTo(that.getClass().getName());
        }
        else {
            // an unknown object type
            throw new ClassCastException("Not a javax.slee.management.NotificationSource: " + that.getClass().getName());
        }
    }
}
