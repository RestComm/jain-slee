package org.mobicents.slee.container.component.validator.sbb.abstracts.usage;

import javax.slee.usage.SampleStatistics;

public interface UsageThrowsOnGetterInterface {

	public SampleStatistics getSample();
	public void sampleSample(long t);
	
	
	public SampleStatistics getSampleTwo() throws Exception;
	public void sampleSampleTwo(long t);
	
	
	public void incrementSampleIncrement(long t);
	public long getSampleIncrement();
	
}
