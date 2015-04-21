package cloudservices.client.packets;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PacketTest {
	private Packet packet;
	
	@Before
	public void init() {
		packet = new Packet() {
			@Override
			protected void subDecode(byte[] remain) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected byte[] processSubData() {
				// TODO Auto-generated method stub
				return new byte[]{};
			}
		};
	}
	
	@Test
	public void encodingStringTest() {
		String testSring = "中文123";
		byte[] data = packet.encodingString(testSring);
		String r1 = packet.decodingString(data);
		
		ByteBuffer buffer = ByteBuffer.allocate(2 + data.length);
		buffer.putShort((short) data.length);
		buffer.put(data);
		buffer.flip();
		String r2 = packet.getString(buffer);
		
		System.out.printf("encoding-decoding - r1:%s r2:%s\n", r1, r2);
		Assert.assertEquals(testSring, r1);
		Assert.assertEquals(testSring, r2);
	}
	
	@Test
	public void getHeaderTest() {
		packet.setPacketType(Packet.TEXT);
		packet.setSub(true);
		packet.setAck(true);
		byte h1 = packet.getHeader();
		Assert.assertEquals(0x13, h1);
		
		packet.setPacketType(Packet.HTTP);
		packet.setSub(true);
		packet.setAck(false);
		byte h2 = packet.getHeader();
		Assert.assertEquals(0x22, h2);
		
		packet.setPacketType(Packet.ACK);
		packet.setSub(false);
		packet.setAck(true);
		byte h3 = packet.getHeader();
		Assert.assertEquals(0x31, h3);
		
		packet.setPacketType(Packet.FILE);
		packet.setSub(false);
		packet.setAck(false);
		byte h4 = packet.getHeader();
		Assert.assertEquals(0x40, h4);
	}
	
	
	@Test 
	public void toByteArrayTest() {
		int type = 0;
		boolean ack = false;
		boolean sub = false;
		int mId = 10;
		short total = 10;
		short no = 4;
		String username = "testUser";
		packet.setPacketType(type);
		packet.setAck(ack);
		packet.setSub(sub);
		packet.setMessageId(mId);
		packet.setTotal(total);
		packet.setNo(no);
		packet.setUsername(username);
		System.out.printf("Packet:%s\n", packet);
		
		byte[] data = packet.toByteArray();
		Packet result = PacketFactory.getPacket(ByteBuffer.wrap(data));
		
		Assert.assertEquals(type, result.getPacketType());
		Assert.assertEquals(ack, result.isAck());
		Assert.assertEquals(sub, result.isSub());
		Assert.assertEquals(mId, result.getMessageId());
		Assert.assertEquals(total, result.getTotal());
		Assert.assertEquals(no, result.getNo());
		Assert.assertEquals(username, result.getUsername());
		System.out.printf("Result:%s\n", result);
	}
	
	@Test
	public void subsectionTest() {
		Packet p = new Packet() {
			private byte[] datas = new byte[10000];;
			@Override
			protected void subDecode(byte[] remain) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected byte[] processSubData() {
				// TODO Auto-generated method stub
				return datas;
			}
		};
		p.setUsername("test");
		Packet[] ps = Packet.subsection(p, 1000);
		System.out.printf("length: %d\n", ps.length);
		Assert.assertEquals(11, ps.length);
		for (int i = 0; i < ps.length; i++) {
			System.out.printf("SubPacket:%s\n", ps[i]);
		}
	}
}
