package fr.inria.phoenix.diasuite.framework.context.propositiondactivity;

import java.io.Serializable;

/**
 * An object to store a value published by the context <code>PropositionDActivity</code>.
 *
 * <pre>
 * context PropositionDActivity as Boolean {
 * 	when provided dailyActivity from ActivityNotifier
 * 	get dailyActivity from ActivityNotifier
 * 	maybe publish;
 * }
 * </pre>
 */
public final class PropositionDActivityValue implements Serializable {
    private static final long serialVersionUID = 0;
    
    private java.lang.Boolean value;
    
    /**
     * Get the value of the context <code>PropositionDActivity</code>
     * 
     * @return the value of the context <code>PropositionDActivity</code>
     */
    public java.lang.Boolean value() {
        return value;
    }
    
    public PropositionDActivityValue(java.lang.Boolean value) {
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
        PropositionDActivityValue other = (PropositionDActivityValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
