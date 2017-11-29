package fr.inria.phoenix.diasuite.framework.datatype.physiologicalactivity;

import java.io.Serializable;

/**
 * <pre>
structure PhysiologicalActivity {
 * 	name as String; 
 * 	period as Period;
 * 	physiologicalData as PhysiologicalData;
 * }
</pre>
 */
public class PhysiologicalActivity implements Serializable {
    private static final long serialVersionUID = 0;

    // Code for field name
    private java.lang.String name;
    
    /**
     * Returns the value of the name field.
    
    <pre>
    name as String
    </pre>
    @return the value of name
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Set the value of the name field.
    
    <pre>
    name as String
    </pre>
    @param name the new value of name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    // End of code for field name

    // Code for field period
    private fr.inria.phoenix.diasuite.framework.datatype.period.Period period;
    
    /**
     * Returns the value of the period field.
    <p>
    DailyActivityName or PeriodActivityName
    
    <pre>
    period as Period
    </pre>
    @return the value of period
     */
    public fr.inria.phoenix.diasuite.framework.datatype.period.Period getPeriod() {
        return period;
    }
    
    /**
     * Set the value of the period field.
    <p>
    DailyActivityName or PeriodActivityName
    
    <pre>
    period as Period
    </pre>
    @param period the new value of period
     */
    public void setPeriod(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        this.period = period;
    }
    // End of code for field period

    // Code for field physiologicalData
    private fr.inria.phoenix.diasuite.framework.datatype.physiologicaldata.PhysiologicalData physiologicalData;
    
    /**
     * Returns the value of the physiologicalData field.
    
    <pre>
    physiologicalData as PhysiologicalData
    </pre>
    @return the value of physiologicalData
     */
    public fr.inria.phoenix.diasuite.framework.datatype.physiologicaldata.PhysiologicalData getPhysiologicalData() {
        return physiologicalData;
    }
    
    /**
     * Set the value of the physiologicalData field.
    
    <pre>
    physiologicalData as PhysiologicalData
    </pre>
    @param physiologicalData the new value of physiologicalData
     */
    public void setPhysiologicalData(fr.inria.phoenix.diasuite.framework.datatype.physiologicaldata.PhysiologicalData physiologicalData) {
        this.physiologicalData = physiologicalData;
    }
    // End of code for field physiologicalData

    public PhysiologicalActivity() {
    }

    public PhysiologicalActivity(java.lang.String name,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period,
            fr.inria.phoenix.diasuite.framework.datatype.physiologicaldata.PhysiologicalData physiologicalData) {
        this.name = name;
        this.period = period;
        this.physiologicalData = physiologicalData;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((period == null) ? 0 : period.hashCode());
        result = prime * result + ((physiologicalData == null) ? 0 : physiologicalData.hashCode());
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
        PhysiologicalActivity other = (PhysiologicalActivity) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (period == null) {
            if (other.period != null)
                return false;
        } else if (!period.equals(other.period))
            return false;
        if (physiologicalData == null) {
            if (other.physiologicalData != null)
                return false;
        } else if (!physiologicalData.equals(other.physiologicalData))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PhysiologicalActivity [" + 
            "name=" + name +", " + 
            "period=" + period +", " + 
            "physiologicalData=" + physiologicalData +
        "]";
    }
}
