package fr.inria.phoenix.diasuite.framework.context.getsavedtime;

import java.io.Serializable;

/**
 * An object to store a value published by the context <code>GetSavedTime</code>.
 *
 * <pre>
 * context GetSavedTime as String[] {
 * 	when provided data from Storage
 * 		maybe publish;
 * }
 * </pre>
 */
public final class GetSavedTimeValue implements Serializable {
    private static final long serialVersionUID = 0;
    
    private java.util.List<java.lang.String> value;
    
    /**
     * Get the value of the context <code>GetSavedTime</code>
     * 
     * @return the value of the context <code>GetSavedTime</code>
     */
    public java.util.List<java.lang.String> value() {
        return value;
    }
    
    public GetSavedTimeValue(java.util.List<java.lang.String> value) {
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
        GetSavedTimeValue other = (GetSavedTimeValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
