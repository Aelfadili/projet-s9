package fr.inria.phoenix.diasuite.framework.context.sleep;

import java.io.Serializable;

/**
 * An object to store a value published by the context <code>Sleep</code>.
 *
 * <pre>
 * context Sleep as String {
 * 	when provided inactivityLevel from InactivitySensor 
 * 		get currentTime from RoutineScheduler,
 * 		lastInteraction from InactivitySensor
 * 	maybe publish;
 * }
 * </pre>
 */
public final class SleepValue implements Serializable {
    private static final long serialVersionUID = 0;
    
    private java.lang.String value;
    
    /**
     * Get the value of the context <code>Sleep</code>
     * 
     * @return the value of the context <code>Sleep</code>
     */
    public java.lang.String value() {
        return value;
    }
    
    public SleepValue(java.lang.String value) {
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
        SleepValue other = (SleepValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
