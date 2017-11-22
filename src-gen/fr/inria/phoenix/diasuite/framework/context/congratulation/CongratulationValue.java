package fr.inria.phoenix.diasuite.framework.context.congratulation;

import java.io.Serializable;

/**
 * An object to store a value published by the context <code>Congratulation</code>.
 * <p>
 * Sortie context : Si nombre de pas suffisant alors féliciter
 *
 * <pre>
 * context Congratulation as Boolean {
 * 	when provided steps from Fitbit
 * 	get steps from Fitbit
 * 	maybe publish;
 * 	}
 * </pre>
 */
public final class CongratulationValue implements Serializable {
    private static final long serialVersionUID = 0;
    
    private java.lang.Boolean value;
    
    /**
     * Get the value of the context <code>Congratulation</code>
     * 
     * <p>
     * Sortie context : Si nombre de pas suffisant alors féliciter
     * 
     * @return the value of the context <code>Congratulation</code>
     */
    public java.lang.Boolean value() {
        return value;
    }
    
    public CongratulationValue(java.lang.Boolean value) {
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
        CongratulationValue other = (CongratulationValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
