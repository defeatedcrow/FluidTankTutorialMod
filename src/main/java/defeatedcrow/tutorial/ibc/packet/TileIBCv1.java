package defeatedcrow.tutorial.ibc.packet;

import defeatedcrow.tutorial.ibc.base.TileIBC;
import net.minecraft.util.ITickable;

public class TileIBCv1 extends TileIBC implements ITickable {

	private int last = 0;
	private int count = 0;

	/*
	 * 5Tickのクールタイムを設け、水位に変更があった時だけパケットを飛ばす
	 */
	@Override
	public void update() {
		if (!world.isRemote) {
			if (count <= 0) {
				if (inputT.getFluidAmount() != last) {
					last = inputT.getFluidAmount();

					TutorialPacket.INSTANCE.sendToAll(
							new MessageIBC(pos, inputT.getFluidType(), inputT.getFluidAmount()));
				}
				count = 10;
			} else {
				count--;
			}
		}
	}

}
