package name.qd.linebot.updater.task;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.linebot.updater.CacheSender;

public class TaskManager {
	private Logger log = LoggerFactory.getLogger(TaskManager.class);
	private static TaskManager instance = new TaskManager();
	private Set<CacheUpdateTask> set = new HashSet<>();
	private CacheSender cacheSender = new CacheSender();
	
	public static TaskManager getInstance() {
		return instance;
	}
	
	private TaskManager() {
	}
	
	public void doTask() {
		for(CacheUpdateTask task : set) {
			task.doTask();
			String jsonString = task.getJsonString();
			try {
				cacheSender.send(jsonString);
			} catch (Exception e) {
				log.error("do {} task failed.", task.getClass().getSimpleName(), e);
			}
		}
	}
}
