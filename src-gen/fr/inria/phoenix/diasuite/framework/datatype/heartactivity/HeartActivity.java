package fr.inria.phoenix.diasuite.framework.datatype.heartactivity;

import java.io.Serializable;

/**
 * <pre>
structure HeartActivity {
 * 	name as HeartRate;
 * 	minPulsation as Integer;
 * 	maxPulsation as Integer;
 * 	durationInMinutes as Integer;
 * }
</pre>
 */
public class HeartActivity implements Serializable {
    private static final long serialVersionUID = 0;

    // Code for field name
    private fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate name;
    
    /**
     * Returns the value of the name field.
    
    <pre>
    name as HeartRate
    </pre>
    @return the value of name
     */
    public fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate getName() {
        return name;
    }
    
    /**
     * Set the value of the name field.
    
    <pre>
    name as HeartRate
    </pre>
    @param name the new value of name
     */
    public void setName(fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate name) {
        this.name = name;
    }
    // End of code for field name

    // Code for field minPulsation
    private java.lang.Integer minPulsation;
    
    /**
     * Returns the value of the minPulsation field.
    
    <pre>
    minPulsation as Integer
    </pre>
    @return the value of minPulsation
     */
    public java.lang.Integer getMinPulsation() {
        return minPulsation;
    }
    
    /**
     * Set the value of the minPulsation field.
    
    <pre>
    minPulsation as Integer
    </pre>
    @param minPulsation the new value of minPulsation
     */
    public void setMinPulsation(java.lang.Integer minPulsation) {
        this.minPulsation = minPulsation;
    }
    // End of code for field minPulsation

    // Code for field maxPulsation
    private java.lang.Integer maxPulsation;
    
    /**
     * Returns the value of the maxPulsation field.
    
    <pre>
    maxPulsation as Integer
    </pre>
    @return the value of maxPulsation
     */
    public java.lang.Integer getMaxPulsation() {
        return maxPulsation;
    }
    
    /**
     * Set the value of the maxPulsation field.
    
    <pre>
    maxPulsation as Integer
    </pre>
    @param maxPulsation the new value of maxPulsation
     */
    public void setMaxPulsation(java.lang.Integer maxPulsation) {
        this.maxPulsation = maxPulsation;
    }
    // End of code for field maxPulsation

    // Code for field durationInMinutes
    private java.lang.Integer durationInMinutes;
    
    /**
     * Returns the value of the durationInMinutes field.
    
    <pre>
    durationInMinutes as Integer
    </pre>
    @return the value of durationInMinutes
     */
    public java.lang.Integer getDurationInMinutes() {
        return durationInMinutes;
    }
    
    /**
     * Set the value of the durationInMinutes field.
    
    <pre>
    durationInMinutes as Integer
    </pre>
    @param durationInMinutes the new value of durationInMinutes
     */
    public void setDurationInMinutes(java.lang.Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
    // End of code for field durationInMinutes

    public HeartActivity() {
    }

    public HeartActivity(fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate name,
            java.lang.Integer minPulsation,
            java.lang.Integer maxPulsation,
            java.lang.Integer durationInMinutes) {
        this.name = name;
        this.minPulsation = minPulsation;
        this.maxPulsation = maxPulsation;
        this.durationInMinutes = durationInMinutes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((minPulsation == null) ? 0 : minPulsation.hashCode());
        result = prime * result + ((maxPulsation == null) ? 0 : maxPulsation.hashCode());
        result = prime * result + ((durationInMinutes == null) ? 0 : durationInMinutes.hashCode());
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
        HeartActivity other = (HeartActivity) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (minPulsation == null) {
            if (other.minPulsation != null)
                return false;
        } else if (!minPulsation.equals(other.minPulsation))
            return false;
        if (maxPulsation == null) {
            if (other.maxPulsation != null)
                return false;
        } else if (!maxPulsation.equals(other.maxPulsation))
            return false;
        if (durationInMinutes == null) {
            if (other.durationInMinutes != null)
                return false;
        } else if (!durationInMinutes.equals(other.durationInMinutes))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "HeartActivity [" + 
            "name=" + name +", " + 
            "minPulsation=" + minPulsation +", " + 
            "maxPulsation=" + maxPulsation +", " + 
            "durationInMinutes=" + durationInMinutes +
        "]";
    }
}
