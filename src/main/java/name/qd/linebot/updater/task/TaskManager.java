package name.qd.linebot.updater.task;

import java.util.HashSet;
import java.util.Set;

import name.qd.linebot.updater.CacheSender;

public class TaskManager {
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
			cacheSender.send(jsonString);
		}
	}
}
