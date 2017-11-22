package fr.inria.phoenix.diasuite.framework.context.alertmove;

import java.io.Serializable;

/**
 * An object to store a value published by the context <code>AlertMove</code>.
 *
 * <pre>
 * context AlertMove as Boolean{
 * 	when provided dailyActivity from ActivityNotifier
 * 	get steps from Fitbit,
 * 		dailyActivity from ActivityNotifier
 * 	maybe publish;
 * }
 * </pre>
 */
public final class AlertMoveValue implements Serializable {
    private static final long serialVersionUID = 0;
    
    private java.lang.Boolean value;
    
    /**
     * Get the value of the context <code>AlertMove</code>
     * 
     * @return the value of the context <code>AlertMove</code>
     */
    public java.lang.Boolean value() {
        return value;
    }
    
    public AlertMoveValue(java.lang.Boolean value) {
        this.value = value;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AlertMoveValue other = (AlertMoveValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
