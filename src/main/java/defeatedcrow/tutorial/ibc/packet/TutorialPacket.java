package defeatedcrow.tutorial.ibc.packet;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class TutorialPacket {
	/*
	 * INSTANCEの生成
	 * "tutorial"の部分には他modと被らない名前を入れよう
	 */
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("tutorial");

	/*
	 * パケットの登録処理
	 * このメソッドをmodの初期化イベント(preInitかInit)で呼ぶ
	 * "0"はこのパケットのIDで、mod内で被らなければOK
	 * Sideはパケットをどちら向きに送信するか
	 */
	public static void init() {
		INSTANCE.registerMessage(MessageHandlerIBC.class, MessageIBC.class, 0, Side.CLIENT);
	}
}
