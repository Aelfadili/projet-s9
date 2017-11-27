package fr.inria.phoenix.diasuite.framework.context.propositiongoout;

import java.io.Serializable;

/**
 * An object to store a value published by the context <code>PropositionGoOut</code>.
 * <p>
 * Boolean ou String ?
 * Sortie context : Si nombre de pas insuffisant
 *
 * <pre>
 * context PropositionGoOut as CriticalNotification {
 * 	when provided tickHour from Clock
 * 	get steps from Fitbit,
 * 		events from Agenda
 * 	maybe publish;
 * 	}
 * </pre>
 */
public final class PropositionGoOutValue implements Serializable {
    private static final long serialVersionUID = 0;
    
    private fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification value;
    
    /**
     * Get the value of the context <code>PropositionGoOut</code>
     * 
     * <p>
     * Boolean ou String ?
     * Sortie context : Si nombre de pas insuffisant
     * 
     * @return the value of the context <code>PropositionGoOut</code>
     */
    public fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification value() {
        return value;
    }
    
    public PropositionGoOutValue(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification value) {
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
        PropositionGoOutValue other = (PropositionGoOutValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
