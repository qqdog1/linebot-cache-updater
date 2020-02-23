package name.qd.linebot.updater;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheUpdater {
	private Logger log;
	
	private CacheUpdater() {
		init();
		check();
		update();
	}

	private void init() {
		Properties prop = System.getProperties();
		prop.setProperty("log4j.configurationFile", "./config/log4j2.xml");
		log = LoggerFactory.getLogger(CacheUpdater.class);
	}
	
	private void check() {

	}
	
	private void update() {
		
	}
	
	public static void main(String[] s) {
		CacheUpdater cacheUpdater = new CacheUpdater();
	}
}
