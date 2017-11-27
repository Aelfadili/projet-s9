package fr.inria.phoenix.scenario.coach_activity.context;


import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.phoenix.diasuite.framework.context.congratulation.AbstractCongratulation;
import fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification;
import fr.inria.phoenix.diasuite.framework.datatype.date.Date;
import fr.inria.phoenix.diasuite.framework.datatype.period.Period;
import fr.inria.phoenix.diasuite.framework.device.clock.TickHourFromClock;

public class Congratulation extends AbstractCongratulation {
	
	public int time = 20;
	public int step = 2000;
	public Date soir;
	public Congratulation(ServiceConfiguration serviceConfiguration) {
		super(serviceConfiguration);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	protected CongratulationValuePublishable onTickHourFromClock(TickHourFromClock tickHourFromClock,
			DiscoverForTickHourFromClock discover) {
		if ( tickHourFromClock.equals(time)) {// besoin ou pas? 
		FitbitProxyForTickHourFromClock steps = discover.fitbits().anyOne();
		Period period = null;
		period.getBeginning();   // get beginnig represente quoi ? 
		period.setEnd(soir);
		if (step < steps.getSteps(period)) {
			  CriticalNotification notification = new CriticalNotification("Goalreached", "Congratulation", "Félicitation !!!  Vous avez atteint votre objectif journalier",null);
			// Return quelque chose??? 
		}
		}
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}