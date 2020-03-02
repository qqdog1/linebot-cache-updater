package name.qd.linebot.updater.task.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import name.qd.analysis.Constants.Action;
import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.chip.ChipAnalyzers;
import name.qd.analysis.chip.analyzer.ChipAnalyzerManager;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.utils.DateUtils;
import name.qd.linebot.updater.config.ConfigLoader;
import name.qd.linebot.updater.task.CacheUpdateTask;
import name.qd.linebot.updater.task.TaskManager;

public class BestBuySellTask extends CacheUpdateTask {
	private static Logger log = LoggerFactory.getLogger(TaskManager.class);
	
	private static String BUY_COMMAND = "bestbuy";
	private static String SELL_COMMAND = "bestsell";
	private static String BUY_DESCRIPTION = "近20交易日，績效前10分公司，買進減賣出超過1500萬";
	private static String SELL_DESCRIPTION = "近20交易日，績效前10分公司，賣出減買進超過1500萬";
	private static String TIME = sdf.format(Calendar.getInstance().getTime());
	private static int DAYS = 20;
	private static int BEST_BRANCHS = 10;
	private static double TRADE_COST = 15000000;
	
	private CacheUpdateTask buyTask = new BestBuyTask();
	private CacheUpdateTask sellTask = new BestSellTask();
	
	private ChipAnalyzerManager chipAnalyzerManager;
	private DataSource dataSource;

	public BestBuySellTask(ChipAnalyzerManager chipAnalyzerManager, DataSource dataSource) {
		super(null, null, null);
		this.chipAnalyzerManager = chipAnalyzerManager;
		this.dataSource = dataSource;
	}

	@Override
	public String getJsonString() {
		ArrayNode node = objectMapper.createArrayNode();
		try {
			JsonNode buyNode = objectMapper.readTree(buyTask.getJsonString());
			JsonNode sellNode = objectMapper.readTree(sellTask.getJsonString());
			
			for(JsonNode dataNode : buyNode) {
				node.add(dataNode);
			}
			for(JsonNode dataNode : sellNode) {
				node.add(dataNode);
			}
		} catch (JsonProcessingException e) {
			log.error("Parse json string failed.", e);
		}
		return node.toString();
	}

	@Override
	public void doTask() {
		List<Date> lstDate = DateUtils.getFromToByDays(ConfigLoader.getInstance().getTWSEDatePath(), Exchange.TWSE.name(), DAYS);
		String[] inputs = {String.valueOf(BEST_BRANCHS)};
		List<List<String>> lstResult = chipAnalyzerManager.getAnalysisResult(dataSource, ChipAnalyzers.BEST_BRANCH_BUY_SELL, "", "", lstDate.get(0), lstDate.get(1), TRADE_COST, false, inputs);
		
		StringBuilder sb = new StringBuilder();
		for(List<String> lst : lstResult) {
			for(String s : lst) {
				sb.append(s).append(",");
			}
			log.info(sb.toString());
			sb.setLength(0);
		}
		
		Set<String> buySet = new HashSet<>();
		Set<String> sellSet = new HashSet<>();
		for(List<String> lst : lstResult) {
			Action action = Action.valueOf(lst.get(2));
			switch(action) {
			case BUY:
				buySet.add(lst.get(1));
				break;
			case SELL:
				sellSet.add(lst.get(1));
				break;
			default:
				break;
			}
		}
		
		buyTask.setValue(getProductString(buySet));
		sellTask.setValue(getProductString(sellSet));
	}
	
	private String getProductString(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		for(String product : set) {
			sb.append(product).append("\n");
		}
		return sb.toString();
	}
	
	public class BestBuyTask extends CacheUpdateTask {

		public BestBuyTask() {
			super(BUY_COMMAND, BUY_DESCRIPTION, TIME);
		}

		@Override
		public void doTask() {
		}
	}
	
	public class BestSellTask extends CacheUpdateTask {

		public BestSellTask() {
			super(SELL_COMMAND, SELL_DESCRIPTION, TIME);
		}

		@Override
		public void doTask() {
		}
	}
}
