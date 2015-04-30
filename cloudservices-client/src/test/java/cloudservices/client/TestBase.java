package cloudservices.client;

public class TestBase {
	protected static final String SERVER_IP = "172.21.12.89";
	protected static final int MQTT_PORT = 1883;
	protected static final String SERVER_URL = "http://172.21.12.89:8080/cloudservices-web";
	protected static final String RECEIVE_URL = SERVER_URL + "/api/receive";
	protected static final String SEND_URL = SERVER_URL + "/api/send";
	protected static final String CONNECT_URL = SERVER_URL + "/api/connect";
	
	protected static final String TOPIC = "beidou";
	protected static final String DEFAULT_PASSWORD = "kk-xtd-push";
	
	
	public static ClientConfiguration getInitConfig() {
		ClientConfiguration config = new ClientConfiguration(SERVER_IP, MQTT_PORT);
		config = new ClientConfiguration(SERVER_IP, MQTT_PORT);
		config.setPassword(DEFAULT_PASSWORD);
		config.setTopic(TOPIC);
		config.setSendUrl(SEND_URL);
		config.setReceiveUrl(RECEIVE_URL); 
		config.setConnectUrl(CONNECT_URL);
		return config;
	}
}
