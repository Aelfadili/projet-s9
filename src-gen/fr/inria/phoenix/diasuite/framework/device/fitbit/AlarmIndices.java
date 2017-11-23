package fr.inria.phoenix.diasuite.framework.device.fitbit;

import java.io.Serializable;

/**
 * Container for the indices of the source <code>alarm</code> from device <code>Fitbit</code>.
 * <p>
 * alarm
 * 
 * <pre>
 * source alarm as Alarm indexed by name as String;
 * </pre>
 */
public final class AlarmIndices implements Serializable {
    private static final long serialVersionUID = 0;
    
    public AlarmIndices(java.lang.String name) {
        this._name = name;
    }
    
    // Code for index name
    private java.lang.String _name;
    
    /**
     * Get the <code>name</code> index
     * 
     * @return the value of the <code>name</code> index
     */
    public java.lang.String name() {
        return _name;
    }
    
    /**
     * Set the <code>name</code> index
     * 
     * @param newName the new value for the <code>name</code> index
     */
    public void setName(java.lang.String newName) {
        this._name = newName;
    }
    // End of code for index name
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
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
        AlarmIndices other = (AlarmIndices) obj;
        if (_name == null) {
            if (other._name != null)
                return false;
        } else if (!_name.equals(other._name))
            return false;
        return true;
    }
}
