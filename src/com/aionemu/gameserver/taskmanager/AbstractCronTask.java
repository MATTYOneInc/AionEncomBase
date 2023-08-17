/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.taskmanager;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

public abstract class AbstractCronTask implements Runnable
{
	private String cronExpressionString;
	private CronExpression runExpression;
	private int runTime;
	private long period;
	
	public final int getRunTime() {
		return runTime;
	}
	
	abstract protected long getRunDelay();
	protected void preInit() {
	}
	
	protected void postInit() {
	}
	
	public final String getCronExpressionString() {
		return cronExpressionString;
	}
	
	abstract protected String getServerTimeVariable();
	
	public long getPeriod() {
		return period;
	}
	
	protected void preRun() {
	}
	
	abstract protected void executeTask();
	abstract protected boolean canRunOnInit();
	
	protected void postRun() {
	}
	
	public AbstractCronTask(String cronExpression) throws ParseException {
		if (cronExpression == null)
			throw new NullPointerException("cronExpressionString");
		cronExpressionString = cronExpression;
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		runTime = dao.load(getServerTimeVariable());
		preInit();
		runExpression = new CronExpression(cronExpressionString);
		Date nextDate = runExpression.getTimeAfter(new Date());
		Date nextAfterDate = runExpression.getTimeAfter(nextDate);
		period = nextAfterDate.getTime() - nextDate.getTime();
		postInit();
		if (getRunDelay() == 0) {
			if (canRunOnInit()) {
				ThreadPoolManager.getInstance().schedule(this, 0);
			}
			else {
				saveNextRunTime();
			}
		}
		scheduleNextRun();
	}
	
	private void scheduleNextRun() {
		CronService.getInstance().schedule(this, cronExpressionString, true);
	}
	
	private void saveNextRunTime() {
		Date nextDate = runExpression.getTimeAfter(new Date());
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		runTime = (int) (nextDate.getTime() / 1000);
		dao.store(getServerTimeVariable(), runTime);
	}
	
	@Override
	public final void run() {
        if (getRunDelay() > 0) {
            ThreadPoolManager.getInstance().schedule(this, getRunDelay());
        } else {
		    preRun();
		    executeTask();
		    saveNextRunTime();
		    postRun();
        }
	}
}