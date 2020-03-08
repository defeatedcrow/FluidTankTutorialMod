package defeatedcrow.tutorial.ibc.state;

import defeatedcrow.tutorial.CrowTutorial;
import defeatedcrow.tutorial.ibc.base.TileIBC;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;

public class TileIBCv2 extends TileIBC implements ITickable {

	private int last = 0;

	/*
	 * タンク内の水位が1,000mB変わるたびにBlockStateを変更する
	 */
	@Override
	public void update() {
		if (!world.isRemote) {
			int level = MathHelper.floor(inputT.getFluidAmount() / 1000);
			if (level != last) {
				last = level;

				IBlockState newState = CrowTutorial.IBC_v2.getDefaultState().withProperty(BlockLiquid.LEVEL, level);

				world.setBlockState(getPos(), newState, 3);

				world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);

			}
		}
	}
}
