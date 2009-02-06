package org.mobicents.slee.container.component.validator.sbb.abstracts.usage;

import javax.slee.usage.SampleStatistics;

public interface UsageSetterWrongLevelInterface {

	public SampleStatistics getSample();
	public void sampleSample(long t);
	
	
	public SampleStatistics getSampleTwo();
	public void sampleSampleTwo(long t);
	
	
	 void incrementSampleIncrement(long t);
	public long getSampleIncrement();
	
}
