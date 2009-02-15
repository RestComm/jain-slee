package org.mobicents.slee.container.component.validator.profile.abstrakt;

import javax.slee.usage.SampleStatistics;

public interface UsageOkInterface {

	public SampleStatistics getSample();
	public void sampleSample(long t);
	
	
	public SampleStatistics getSampleTwo();
	public void sampleSampleTwo(long t);
	
	
	public void incrementSampleIncrement(long t);
	public long getSampleIncrement();
	
}
