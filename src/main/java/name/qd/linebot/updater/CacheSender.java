package name.qd.linebot.updater;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.linebot.updater.config.ConfigLoader;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CacheSender {
	private static Logger log = LoggerFactory.getLogger(CacheSender.class);
	private final OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(10, TimeUnit.SECONDS).build();
	private Request.Builder requestBuilder;
	
	public CacheSender() {
		HttpUrl httpUrl = HttpUrl.parse(ConfigLoader.getInstance().getLinebotAddress());
		HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
		urlBuilder.addPathSegments("data/update");
		requestBuilder = new Request.Builder().url(urlBuilder.build().url().toString());
	}
	
	public void send(String data) throws Exception {
		RequestBody body = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
		        .addFormDataPart("data", data)
		        .build();
		
		Request request = requestBuilder.post(body).build();
		Response response = okHttpClient.newCall(request).execute();
		
		int status = response.code();
		if(status != 200) {
			String responseBody = response.body().string();
			log.error("Send cache update failed. status:{} {}, {}", status, responseBody);
			throw new Exception(responseBody);
		}
	}
}
