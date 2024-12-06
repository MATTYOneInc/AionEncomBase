/*

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.TaskFromDBDAO;
import com.aionemu.gameserver.model.tasks.TaskFromDB;
import com.aionemu.gameserver.model.templates.tasks.TaskFromDBHandler;
import com.aionemu.gameserver.taskmanager.tasks.RestartTask;
import com.aionemu.gameserver.taskmanager.tasks.ShutdownTask;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Divinity Based on L2J Emulator Global Tasks System
 * @author From L2J : Layane
 */
public class TaskManagerFromDB {

	private static final Logger log = LoggerFactory.getLogger(TaskManagerFromDB.class);

	private ArrayList<TaskFromDB> tasksList;
	private HashMap<String, TaskFromDBHandler> handlers;

	public TaskManagerFromDB() {
		this.handlers = new HashMap<String, TaskFromDBHandler>();

		tasksList = getDAO().getAllTasks();
		log.info("Loaded " + tasksList.size() + " task" + (tasksList.size() > 1 ? "s" : "") + " from the database");

		registerHandlers();
		registerTasks();
	}

	/**
	 * Allow to register all tasks to the handler
	 */
	private void registerHandlers() {
		registerNewTask(new ShutdownTask());
		registerNewTask(new RestartTask());
	}

	/**
	 * Allow to register one task and check if already exists
	 * 
	 * @param shutdownTask
	 */
	private void registerNewTask(TaskFromDBHandler task) {
		if (handlers.get(task.getTaskName()) != null) {
			log.error("Can't override a task with name : " + task.getTaskName());
		}
		handlers.put(task.getTaskName(), task);
	}

	/**
	 * Launching & checking task process
	 */
	private void registerTasks() {
		// For all tasks from DB
		for (TaskFromDB task : tasksList) {
			// If the task name exist
			if (handlers.get(task.getName()) != null) {
				Class<? extends TaskFromDBHandler> tmpClass = handlers.get(task.getName()).getClass();
				TaskFromDBHandler currentTask = null;

				try {
					// Create new instance of the task
					currentTask = tmpClass.newInstance();
				} catch (InstantiationException e) {
					log.error(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					log.error(e.getMessage(), e);
				}

				// Set informations for the task
				currentTask.setId(task.getId());
				currentTask.setParam(task.getParams());

				if (!currentTask.isValid()) {
					log.error("Invalid parameter for task ID: " + task.getId());
					continue;
				}

				if (task.getType().equals("FIXED_IN_TIME")) {
					runFixedInTimeTask(currentTask, task);
				} else
					log.error("Unknow task's type for " + task.getType());
			} else
				log.error("Unknow task's name with ID : " + task.getName());
		}
	}

	/**
	 * Run a fixed in the time (HH:MM:SS) task
	 * 
	 * @param task
	 */
	private void runFixedInTimeTask(TaskFromDBHandler handler, TaskFromDB dbTask) {
		String time[] = dbTask.getStartTime().split(":");
		int hour = Integer.parseInt(time[0]);
		int minute = Integer.parseInt(time[1]);
		int second = Integer.parseInt(time[2]);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);

		long delay = calendar.getTimeInMillis() - System.currentTimeMillis();

		if (delay < 0) {
			delay += 1 * 24 * 60 * 60 * 1000;
		}
		ThreadPoolManager.getInstance().scheduleAtFixedRate(handler, delay, 1 * 24 * 60 * 60 * 1000);
	}

	/**
	 * Retuns {@link com.aionemu.gameserver.dao.TaskFromDBDAO} , just a shortcut
	 * 
	 * @return {@link com.aionemu.gameserver.dao.TaskFromDBDAO}
	 */
	private static TaskFromDBDAO getDAO() {
		return DAOManager.getDAO(TaskFromDBDAO.class);
	}

	/**
	 * Get the instance
	 * 
	 * @return
	 */
	public static final TaskManagerFromDB getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * SingletonHolder
	 */
	private static class SingletonHolder {

		protected static final TaskManagerFromDB instance = new TaskManagerFromDB();
	}
}