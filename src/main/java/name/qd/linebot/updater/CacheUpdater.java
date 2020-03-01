package name.qd.linebot.updater;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.linebot.updater.task.TaskManager;

public class CacheUpdater {
	private Logger log;
	
	private CacheUpdater() {
	}

	private void init() {
		Properties prop = System.getProperties();
		prop.setProperty("log4j.configurationFile", "./config/log4j2.xml");
		log = LoggerFactory.getLogger(CacheUpdater.class);
	}
	
	private void update() {
		log.info("Run cache updater.");
		TaskManager.getInstance().doTask();
		log.info("Cache updated.");
	}
	
	public static void main(String[] s) {
		CacheUpdater cacheUpdater = new CacheUpdater();
		cacheUpdater.init();
		cacheUpdater.update();
	}
}
