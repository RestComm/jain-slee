package org.mobicents.slee.container.component.validator.sbb.abstracts.usage;

import javax.slee.usage.SampleStatistics;

public interface UsageWrongSetterPrameterInterface {

	public SampleStatistics getSample();
	public void sampleSample(long t);
	
	
	public SampleStatistics getSampleTwo();
	public void sampleSampleTwo(long t);
	
	
	public void incrementSampleIncrement(Long t);
	public long getSampleIncrement();
	
}
