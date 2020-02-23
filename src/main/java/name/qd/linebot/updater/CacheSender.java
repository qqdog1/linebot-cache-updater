package name.qd.linebot.updater;

import java.util.concurrent.TimeUnit;

import name.qd.linebot.updater.config.ConfigLoader;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CacheSender {
	private final OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(10, TimeUnit.SECONDS).build();
	private Request.Builder requestBuilder;
	
	public CacheSender() {
		HttpUrl httpUrl = HttpUrl.parse(ConfigLoader.getInstance().getLinebotAddress());
		HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
		urlBuilder.addPathSegments("data/update");
		requestBuilder = new Request.Builder().url(urlBuilder.build().url().toString());
	}
	
	public void send(String key, String value) throws Exception {
		FormBody.Builder builder = new FormBody.Builder();
		builder.addEncoded("key", key);
		builder.addEncoded("value", value);
		FormBody body = builder.build();
		
		Request request = requestBuilder.post(body).build();
		Response response = okHttpClient.newCall(request).execute();
		
		int status = response.code();
		if(status != 200) {
			throw new Exception(response.body().string());
		}
	}
}
