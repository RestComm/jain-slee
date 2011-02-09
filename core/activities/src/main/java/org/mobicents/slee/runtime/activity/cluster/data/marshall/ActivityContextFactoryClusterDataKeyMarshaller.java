package org.mobicents.slee.runtime.activity.cluster.data.marshall;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.mobicents.cluster.data.marshall.ClusterDataMarshaller;
import org.mobicents.slee.runtime.activity.cluster.data.ActivityContextFactoryClusterDataKey;

public class ActivityContextFactoryClusterDataKeyMarshaller implements ClusterDataMarshaller<ActivityContextFactoryClusterDataKey>{

	@Override
	public Class<? extends ActivityContextFactoryClusterDataKey> getDataType() {
		return ActivityContextFactoryClusterDataKey.class;
	}

	@Override
	public Integer getMarshallerID() {
		return null;
	}

	@Override
	public ActivityContextFactoryClusterDataKey readData(ObjectInput objectInput)
			throws IOException, ClassNotFoundException {
		return new ActivityContextFactoryClusterDataKey(objectInput.readUTF());
	}

	@Override
	public void writeData(ObjectOutput objectOutput,
			ActivityContextFactoryClusterDataKey key) throws IOException {
		objectOutput.writeUTF(key.getName());
	}

}
