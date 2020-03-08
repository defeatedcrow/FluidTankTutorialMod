package defeatedcrow.tutorial.ibc.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/*
 * 送受信するメッセージ(IMessage)のクラス
 */
public class MessageIBC implements IMessage {

	public int x;
	public int y;
	public int z;
	public int amo;
	public String id;

	public MessageIBC() {}

	public MessageIBC(BlockPos pos, Fluid fluid, int a) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		id = fluid == null ? "empty" : fluid.getName();
		amo = a;
	}

	/*
	 * 読み取り
	 * 必ず書き込んだ順に読み取りをする
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		amo = buf.readInt();
		id = ByteBufUtils.readUTF8String(buf);
	}

	/*
	 * 書き込み
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(amo);
		ByteBufUtils.writeUTF8String(buf, id);
	}

}
