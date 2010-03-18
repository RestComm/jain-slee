package org.mobicents.slee.container.component.validator.sbb.abstracts.usage;

import javax.slee.usage.SampleStatistics;

public interface UsageWrongSampleReturnTypeInterface {

	public SampleStatistics getSample();
	public void sampleSample(long t);
	
	
	public int getSampleTwo();
	public void sampleSampleTwo(long t);
	
	
	public void incrementSampleIncrement(long t);
	public long getSampleIncrement();
	
}
