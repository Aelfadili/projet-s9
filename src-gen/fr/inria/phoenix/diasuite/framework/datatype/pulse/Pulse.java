package fr.inria.phoenix.diasuite.framework.datatype.pulse;

import java.io.Serializable;

/**
 * <pre>
 * structure Pulse {
 *         period as Period;
 *         pulsations as Integer [];
 * }
 * </pre>
 */
public class Pulse implements Serializable {
    private static final long serialVersionUID = 0;

    // Code for field period
    private fr.inria.phoenix.diasuite.framework.datatype.period.Period period;
    
    /**
     * Returns the value of the period field.
     * 
     * <pre>
     * period as Period
     * </pre>
     * @return the value of period
     */
    public fr.inria.phoenix.diasuite.framework.datatype.period.Period getPeriod() {
        return period;
    }
    
    /**
     * Set the value of the period field.
     * 
     * <pre>
     * period as Period
     * </pre>
     * @param period the new value of period
     */
    public void setPeriod(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        this.period = period;
    }
    // End of code for field period

    // Code for field pulsations
    private java.util.List<java.lang.Integer> pulsations;
    
    /**
     * Returns the value of the pulsations field.
     * 
     * <pre>
     * pulsations as Integer []
     * </pre>
     * @return the value of pulsations
     */
    public java.util.List<java.lang.Integer> getPulsations() {
        return pulsations;
    }
    
    /**
     * Set the value of the pulsations field.
     * 
     * <pre>
     * pulsations as Integer []
     * </pre>
     * @param pulsations the new value of pulsations
     */
    public void setPulsations(java.util.List<java.lang.Integer> pulsations) {
        this.pulsations = pulsations;
    }
    // End of code for field pulsations

    public Pulse() {
    }

    public Pulse(fr.inria.phoenix.diasuite.framework.datatype.period.Period period,
            java.util.List<java.lang.Integer> pulsations) {
        this.period = period;
        this.pulsations = pulsations;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((period == null) ? 0 : period.hashCode());
        result = prime * result + ((pulsations == null) ? 0 : pulsations.hashCode());
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
        Pulse other = (Pulse) obj;
        if (period == null) {
            if (other.period != null)
                return false;
        } else if (!period.equals(other.period))
            return false;
        if (pulsations == null) {
            if (other.pulsations != null)
                return false;
        } else if (!pulsations.equals(other.pulsations))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Pulse [" + 
            "period=" + period +", " + 
            "pulsations=" + pulsations +
        "]";
    }
}
