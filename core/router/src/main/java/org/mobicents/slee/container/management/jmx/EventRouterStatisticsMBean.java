package org.mobicents.slee.container.management.jmx;

import javax.slee.EventTypeID;
import javax.slee.management.ManagementException;

public interface EventRouterStatisticsMBean {

    public static final String OBJECT_NAME = "org.mobicents.slee:name=EventRouterStatistics";
    
	public int getActivitiesMapped() throws ManagementException;

	public int getActivitiesMapped(int executor) throws ManagementException;

	public long getAverageEventRoutingTime() throws ManagementException;

	public long getAverageEventRoutingTime(EventTypeID eventTypeID) throws ManagementException;

	public long getAverageEventRoutingTime(int executor) throws ManagementException;

	public long getAverageEventRoutingTime(int executor, EventTypeID eventTypeID) throws ManagementException;

	public long getEventsRouted(EventTypeID eventTypeID) throws ManagementException;

	public long getEventsRouted(int executor, EventTypeID eventTypeID) throws ManagementException;

	public long getExecutedTasks() throws ManagementException;

	public long getExecutedTasks(int executor) throws ManagementException;

	public long getExecutingTime(int executor) throws ManagementException;

	public long getIdleTime(int executor) throws ManagementException;

	public long getMiscTasksExecuted() throws ManagementException;
	
	public long getMiscTasksExecuted(int executor) throws ManagementException;
	
	public long getMiscTasksExecutingTime(int executor) throws ManagementException;

	public long getRoutingTime(int executor, EventTypeID eventTypeID) throws ManagementException;

	public String printAllStats() throws ManagementException;
}
