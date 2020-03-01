package name.qd.linebot.updater;

import org.junit.jupiter.api.Test;

public class CacheSenderTest {

	@Test
	public void sendTest() {
		CacheSender sender = new CacheSender();
		
		String msg = "[{\r\n" + 
				"  \"description\": \"每日aaa什麼什麼\",\r\n" + 
				"  \"lastUpdate\": \"2020/3/01\",\r\n" + 
				"  \"command\": \"bestsell\",\r\n" + 
				"  \"value\": \"1. 1101台 尼\\n2. 2330台機電\\n3. QQ\"\r\n" + 
				"}\r\n" + 
				"]";
		
		try {
			sender.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
