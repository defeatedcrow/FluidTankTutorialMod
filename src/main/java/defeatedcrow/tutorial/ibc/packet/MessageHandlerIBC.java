package defeatedcrow.tutorial.ibc.packet;

import defeatedcrow.tutorial.ibc.base.TileIBC;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/*
 * メッセージ(IMessage)を受信した後、クライアント側のTileEntityを操作するクラス
 */
public class MessageHandlerIBC implements IMessageHandler<MessageIBC, IMessage> {

	@Override
	public IMessage onMessage(MessageIBC message, MessageContext ctx) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player != null) {
			int x = message.x;
			int y = message.y;
			int z = message.z;
			String id = message.id;
			int amo = message.amo;
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tile = player.world.getTileEntity(pos);
			if (id != null && tile instanceof TileIBC) {
				TileIBC te = (TileIBC) tile;
				Fluid f1 = FluidRegistry.getFluid(id);
				if (f1 != null) {
					FluidStack stack1 = new FluidStack(f1, amo);
					te.inputT.setFluid(stack1);
				} else {
					te.inputT.setFluid(null);
				}
			}
		}
		return null;
	}

}
