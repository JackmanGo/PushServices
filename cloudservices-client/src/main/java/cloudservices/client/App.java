package cloudservices.client;

import cloudservices.client.packets.TextPacket;

public class App {
	protected static final String SERVER_IP = "127.0.0.1";
	protected static final int MQTT_PORT = 1883;
	protected static final String SERVER_URL = "http://127.0.0.1:8080/cloudservices-web";
	protected static final String RECEIVE_URL = SERVER_URL + "/api/receive";
	protected static final String SEND_URL = SERVER_URL + "/api/send";
	protected static final String CONNECT_URL = SERVER_URL + "/api/connect";
	
	protected static final String TOPIC = "beidou";
	protected static final String DEFAULT_PASSWORD = "kk-xtd-push";
	
	/**
	 * 
	 * @param args  <br/>
	 * 	0 -- http(1) or mqtt(2)<br/>
	 * 	1 -- username 用户名<br/>
	 * 	2 -- sendto 发送到的用户<br/>
	 *  3 -- period 时间间隔,毫秒<br/>
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 4) {
			return;
		}
		ClientConfiguration config = new ClientConfiguration(SERVER_IP, MQTT_PORT);
		config.setUsername(args[0]);
		config.setPassword(DEFAULT_PASSWORD);
		config.setTopic(TOPIC);
		config.setSendUrl(SEND_URL);
		config.setReceiveUrl(RECEIVE_URL);
		config.setConnectUrl(CONNECT_URL);
		config.setConnectType(Integer.parseInt(args[0]));

		ClientService client = ClientService.getInstance();
		try {
			client.config(config);
			client.startup();
			client.connect();
		} catch (ConfigException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			System.out.println(e1.getMessage());
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		int i = 0, circle = 1000;
		circle = Integer.parseInt(args[3]);
		while (true) {
			// client.sendPacket(new Packet());
			try {
				Thread.sleep(circle);
				TextPacket t = new TextPacket();
				t.setText(String.format("From[%s] to[%s] -- msg:%d", config.getUsername(), args[2], i++));
				client.sendPacket(t, "beidou/"+args[2]);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
