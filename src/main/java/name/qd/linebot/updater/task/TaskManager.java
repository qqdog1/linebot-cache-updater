package name.qd.linebot.updater.task;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.analyzer.ChipAnalyzerManager;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.TWSE.TWSEDataSource;
import name.qd.linebot.updater.CacheSender;
import name.qd.linebot.updater.config.ConfigLoader;
import name.qd.linebot.updater.task.impl.BestBuySellTask;

public class TaskManager {
	private Logger log = LoggerFactory.getLogger(TaskManager.class);
	
	private static TaskManager instance = new TaskManager();
	private Set<CacheUpdateTask> set = new HashSet<>();
	private CacheSender cacheSender = new CacheSender();
	
	private ChipAnalyzerManager analyzerManager;
	private DataSource dataSource;
	
	public static TaskManager getInstance() {
		return instance;
	}
	
	private TaskManager() {
		analyzerManager = new ChipAnalyzerManager(ConfigLoader.getInstance().getCachePath(), true);
		dataSource = new TWSEDataSource(ConfigLoader.getInstance().getTWSEDatePath());
		
		set.add(new BestBuySellTask(analyzerManager, dataSource));
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
