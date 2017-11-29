package fr.inria.phoenix.diasuite.framework.device.fitbit;

import java.io.Serializable;

/**
 * Container for the indices of the source <code>heartActivity</code> from device <code>Fitbit</code>.

<pre>
source heartActivity as HeartActivity indexed by period as Period, heartZone as HeartRate;
</pre>
 */
public final class HeartActivityIndices implements Serializable {
    private static final long serialVersionUID = 0;
    
    public HeartActivityIndices(fr.inria.phoenix.diasuite.framework.datatype.period.Period period,
            fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate heartZone) {
        this._period = period;
        this._heartZone = heartZone;
    }
    
    // Code for index period
    private fr.inria.phoenix.diasuite.framework.datatype.period.Period _period;
    
    /**
     * Get the <code>period</code> index
     * 
     * @return the value of the <code>period</code> index
     */
    public fr.inria.phoenix.diasuite.framework.datatype.period.Period period() {
        return _period;
    }
    
    /**
     * Set the <code>period</code> index
     * 
     * @param newPeriod the new value for the <code>period</code> index
     */
    public void setPeriod(fr.inria.phoenix.diasuite.framework.datatype.period.Period newPeriod) {
        this._period = newPeriod;
    }
    // End of code for index period
    
    // Code for index heartZone
    private fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate _heartZone;
    
    /**
     * Get the <code>heartZone</code> index
     * 
     * @return the value of the <code>heartZone</code> index
     */
    public fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate heartZone() {
        return _heartZone;
    }
    
    /**
     * Set the <code>heartZone</code> index
     * 
     * @param newHeartZone the new value for the <code>heartZone</code> index
     */
    public void setHeartZone(fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate newHeartZone) {
        this._heartZone = newHeartZone;
    }
    // End of code for index heartZone
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_period == null) ? 0 : _period.hashCode());
        result = prime * result + ((_heartZone == null) ? 0 : _heartZone.hashCode());
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
        HeartActivityIndices other = (HeartActivityIndices) obj;
        if (_period == null) {
            if (other._period != null)
                return false;
        } else if (!_period.equals(other._period))
            return false;
        if (_heartZone == null) {
            if (other._heartZone != null)
                return false;
        } else if (!_heartZone.equals(other._heartZone))
            return false;
        return true;
    }
}
