package name.qd.linebot.updater.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoader {
	private Logger log = LoggerFactory.getLogger(ConfigLoader.class);
	
	private String cache_path;
	private String twse_data_path;
	private String line_bot_address;
	
	private static ConfigLoader instance = new ConfigLoader();
	
	public static ConfigLoader getInstance() {
		return instance;
	}
	
	public String getCachePath() {
		return cache_path;
	}
	
	public String getTWSEDatePath() {
		return twse_data_path;
	}
	
	public String getLinebotAddress() {
		return line_bot_address;
	}
	
	private ConfigLoader() {
		loadConfig();
	}

	private void loadConfig() {
		try {
			Properties properties = new Properties();
			FileInputStream fIn = new FileInputStream("./config/config");
			properties.load(fIn);
			// 
			readCachePath(properties);
			readTWSEFilePath(properties);
			readLineBotAddress(properties);
			
			fIn.close();
		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}
	}
	
	private void readCachePath(Properties properties) {
		cache_path = properties.getProperty("file_cache_path");
	}
	
	private void readTWSEFilePath(Properties properties) {
		twse_data_path = properties.getProperty("twse_data_path");
	}
	
	private void readLineBotAddress(Properties properties) {
		line_bot_address = properties.getProperty("line_bot_address");
	}
}
