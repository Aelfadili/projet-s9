package fr.inria.phoenix.diasuite.framework.datatype.physiologicaldata;

import java.io.Serializable;

/**
 * <pre>
structure PhysiologicalData {
 * 	distanceInMeters as Integer;
 * 	calories as Integer;
 * 	pulsations as Integer [];
 * 	steps as Integer;
 * }
</pre>
 */
public class PhysiologicalData implements Serializable {
    private static final long serialVersionUID = 0;

    // Code for field distanceInMeters
    private java.lang.Integer distanceInMeters;
    
    /**
     * Returns the value of the distanceInMeters field.
    
    <pre>
    distanceInMeters as Integer
    </pre>
    @return the value of distanceInMeters
     */
    public java.lang.Integer getDistanceInMeters() {
        return distanceInMeters;
    }
    
    /**
     * Set the value of the distanceInMeters field.
    
    <pre>
    distanceInMeters as Integer
    </pre>
    @param distanceInMeters the new value of distanceInMeters
     */
    public void setDistanceInMeters(java.lang.Integer distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }
    // End of code for field distanceInMeters

    // Code for field calories
    private java.lang.Integer calories;
    
    /**
     * Returns the value of the calories field.
    
    <pre>
    calories as Integer
    </pre>
    @return the value of calories
     */
    public java.lang.Integer getCalories() {
        return calories;
    }
    
    /**
     * Set the value of the calories field.
    
    <pre>
    calories as Integer
    </pre>
    @param calories the new value of calories
     */
    public void setCalories(java.lang.Integer calories) {
        this.calories = calories;
    }
    // End of code for field calories

    // Code for field pulsations
    private java.util.List<java.lang.Integer> pulsations;
    
    /**
     * Returns the value of the pulsations field.
    
    <pre>
    pulsations as Integer []
    </pre>
    @return the value of pulsations
     */
    public java.util.List<java.lang.Integer> getPulsations() {
        return pulsations;
    }
    
    /**
     * Set the value of the pulsations field.
    
    <pre>
    pulsations as Integer []
    </pre>
    @param pulsations the new value of pulsations
     */
    public void setPulsations(java.util.List<java.lang.Integer> pulsations) {
        this.pulsations = pulsations;
    }
    // End of code for field pulsations

    // Code for field steps
    private java.lang.Integer steps;
    
    /**
     * Returns the value of the steps field.
    
    <pre>
    steps as Integer
    </pre>
    @return the value of steps
     */
    public java.lang.Integer getSteps() {
        return steps;
    }
    
    /**
     * Set the value of the steps field.
    
    <pre>
    steps as Integer
    </pre>
    @param steps the new value of steps
     */
    public void setSteps(java.lang.Integer steps) {
        this.steps = steps;
    }
    // End of code for field steps

    public PhysiologicalData() {
    }

    public PhysiologicalData(java.lang.Integer distanceInMeters,
            java.lang.Integer calories,
            java.util.List<java.lang.Integer> pulsations,
            java.lang.Integer steps) {
        this.distanceInMeters = distanceInMeters;
        this.calories = calories;
        this.pulsations = pulsations;
        this.steps = steps;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((distanceInMeters == null) ? 0 : distanceInMeters.hashCode());
        result = prime * result + ((calories == null) ? 0 : calories.hashCode());
        result = prime * result + ((pulsations == null) ? 0 : pulsations.hashCode());
        result = prime * result + ((steps == null) ? 0 : steps.hashCode());
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
        PhysiologicalData other = (PhysiologicalData) obj;
        if (distanceInMeters == null) {
            if (other.distanceInMeters != null)
                return false;
        } else if (!distanceInMeters.equals(other.distanceInMeters))
            return false;
        if (calories == null) {
            if (other.calories != null)
                return false;
        } else if (!calories.equals(other.calories))
            return false;
        if (pulsations == null) {
            if (other.pulsations != null)
                return false;
        } else if (!pulsations.equals(other.pulsations))
            return false;
        if (steps == null) {
            if (other.steps != null)
                return false;
        } else if (!steps.equals(other.steps))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PhysiologicalData [" + 
            "distanceInMeters=" + distanceInMeters +", " + 
            "calories=" + calories +", " + 
            "pulsations=" + pulsations +", " + 
            "steps=" + steps +
        "]";
    }
}
