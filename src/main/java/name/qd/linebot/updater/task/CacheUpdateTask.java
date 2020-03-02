package name.qd.linebot.updater.task;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import name.qd.linebot.updater.util.JsonUtils;

public abstract class CacheUpdateTask {
	protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	protected ObjectMapper objectMapper = JsonUtils.getObjectMapper();
	
	private String command;
	private String description;
	private String value;
	private String time;
	
	public CacheUpdateTask(String command, String description, String time) {
		this.command = command;
		this.description = description;
		this.time = time;
	}
	
	public String getJsonString() {
		ArrayNode arrayNode = objectMapper.createArrayNode();
		ObjectNode node = objectMapper.createObjectNode();
		node.put("command", command);
		node.put("description", description);
		node.put("lastUpdate", time);
		node.put("value", value);
		arrayNode.add(node);
		return arrayNode.toString();
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public abstract void doTask();
}
