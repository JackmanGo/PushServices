package mvc.controller;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JRadioButton;

import mvc.dao.IPushUserDao;
import mvc.model.JResponse;
import mvc.model.PushUser;
import mvc.service.IPushUserService;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cloudservices.client.ClientService;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.PacketCollector;
import cloudservices.client.packets.TextPacket;
import cloudservices.client.packets.filters.PacketAckFilter;


@Controller
@RequestMapping(value="/push/*")
public class PushController {
	private static Logger logger = Logger.getLogger(PushController.class);
	@Resource
	private IPushUserService pushuserService;
	

	@RequestMapping(value = "userlist", method = RequestMethod.GET)  
	public ModelAndView userListView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("push/userlist");
		mv.addObject("title", "Spring MVC And Freemarker");
		mv.addObject("content", " Hello world ， test my first spring mvc ! ");
		return mv;
	}
	@RequestMapping(value = "userlist", method = RequestMethod.POST)
	@ResponseBody
	public Map userListPost(HttpServletRequest request, HttpServletResponse response) {
		List<PushUser> list = pushuserService.getUserList();
		
		Map map = new HashMap<String, Object>();
		map.put("total", list.size());
		map.put("rows", list);
		return map;
	}
	
	@RequestMapping(value = "send")
	@ResponseBody
	public JResponse send(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("topic") String topic,
			@RequestParam("isAck") boolean isAck,
			@RequestParam("text") String text) {
		TextPacket packet = new TextPacket();
		packet.setText(text);
		packet.setAck(isAck);
		if (isAck) {
			PacketCollector collector = ClientService.getInstance().createPacketCollector(new PacketAckFilter(packet.getMessageId()));
			ClientService.getInstance().sendPacket(packet, topic);
			Packet r = collector.nextResult(60000); // 等待超时时间设置为60秒
			collector.cancel();
			if (r != null) {
				return JResponse.success("收到回执");
			} else {
				return JResponse.success("等待消息回执超时");
			}
		} else {
			ClientService.getInstance().sendPacket(packet, topic);
			return JResponse.success("消息已下发");
		}
	}
	
	@RequestMapping(value="hello.do")
	@ResponseBody
	public PushUser handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PushUser user = new PushUser();
		user.setId(1);
		user.setUsername("中文");
		user.setPassword("12345");
		user.setResource("beidou");
		
		return user;
	}
	
}
