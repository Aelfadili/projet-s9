package fr.inria.phoenix.scenario.areyouhome.context;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.phoenix.diasuite.framework.context.ishome.AbstractIsHome;
import fr.inria.phoenix.diasuite.framework.device.contactsensor.ContactFromContactSensor;
import fr.inria.phoenix.diasuite.framework.device.indoorlocationdetector.CurrentRoomFromIndoorLocationDetector;

public abstract class IsHome extends AbstractIsHome {

	public IsHome(ServiceConfiguration serviceConfiguration) {
		super(serviceConfiguration);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IsHomeValuePublishable onContactFromContactSensor(ContactFromContactSensor contactFromContactSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IsHomeValuePublishable onCurrentRoomFromIndoorLocationDetector(
			CurrentRoomFromIndoorLocationDetector currentRoomFromIndoorLocationDetector) {
		// TODO Auto-generated method stub
		return null;
	}

}
