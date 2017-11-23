package fr.inria.phoenix.scenario.areyouhome.context;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.log.DiaLog;
import fr.inria.phoenix.diasuite.framework.context.isidentified.AbstractIsIdentified;
import fr.inria.phoenix.diasuite.framework.device.notifier.ReplyFromNotifier;

public abstract class IsIdentified extends AbstractIsIdentified {

	public IsIdentified(ServiceConfiguration serviceConfiguration) {
		super(serviceConfiguration);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Boolean onReplyFromNotifier(ReplyFromNotifier replyFromNotifier) {
		// TODO Auto-generated method stub
		//if equal(ouRId, replyFromNotifier.indices().id())
		DiaLog.info("[Context][IsIdentified] replyFromNotifier : "+ replyFromNotifier.value());
		if(replyFromNotifier.value() == 0)
			return true;
		else
			return false;
	}

}
