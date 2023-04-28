package martin.hello.mqtt.gpt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.codec.mqtt.MqttMessage;

import java.util.List;

public class MqttWebSocketCodec extends MessageToMessageCodec<WebSocketFrame, MqttMessage> {

    private final MqttEncoder mqttEncoder = MqttEncoder.INSTANCE;
    private final MqttDecoder mqttDecoder = new MqttDecoder();

    @Override
    protected void encode(ChannelHandlerContext ctx, MqttMessage mqttMessage, List<Object> out) {
        mqttEncoder.encode(ctx, mqttMessage, out);
        for (int i = 0; i < out.size(); i++) {
            out.set(i, new BinaryWebSocketFrame((ByteBuf) out.get(i)));
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame, List<Object> out) {
        mqttDecoder.decode(ctx, webSocketFrame.content(), out);
    }
}