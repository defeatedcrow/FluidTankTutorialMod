package defeatedcrow.tutorial.ibc.state;

import defeatedcrow.tutorial.ibc.base.RenderIBC;
import defeatedcrow.tutorial.ibc.base.TileIBC;
import net.minecraftforge.fluids.FluidRegistry;

public class RenderIBCv2 extends RenderIBC {

	/*
	 * タンク内の水位ではなく、Blockのメタデータを参照するように変更
	 */
	@Override
	protected void renderTank(TileIBC te, double x, double y, double z, float partialTicks, int destroyStage, float a) {
		TileIBC pot = te;
		int level = te.getBlockMetadata();
		if (level > 0) {
			renderFluid(FluidRegistry.WATER, x, y, z, partialTicks, level * 1000);
		}
	}

}
