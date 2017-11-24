package fr.inria.phoenix.diasuite.framework.context.coupling;

import java.io.Serializable;

/**
 * An object to store a value published by the context <code>Coupling</code>.
 * <p>
 * when provided GetSavedTime 
 * 		maybe publish;
 *
 * <pre>
 * context Coupling as SleepPeriod[] indexed by period as Period {
 * 	when provided GetFitbitInfos 
 * 		maybe publish;
 * }
 * </pre>
 */
public final class CouplingValue implements Serializable {
    private static final long serialVersionUID = 0;
    
    private java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value;
    
    /**
     * Get the value of the context <code>Coupling</code>
     * 
     * <p>
     * when provided GetSavedTime 
     * 		maybe publish;
     * 
     * @return the value of the context <code>Coupling</code>
     */
    public java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value() {
        return value;
    }
    
    private CouplingIndices indices;
    
    /**
     * Get the value of the indices of the context <code>Coupling</code>
     * 
     * @return the value of the indices
     */
    public CouplingIndices indices() {
        return indices;
    }
    
    public CouplingValue(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value, CouplingIndices indices) {
        this.value = value;
        this.indices = indices;
    }
    
    public CouplingValue(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        this.value = value;
        this.indices = new CouplingIndices(period);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + ((indices == null) ? 0 : indices.hashCode());
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
        CouplingValue other = (CouplingValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        if (indices == null) {
            if (other.indices != null)
                return false;
        } else if (!indices.equals(other.indices))
            return false;
        return true;
    }
}
