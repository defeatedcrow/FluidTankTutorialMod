package defeatedcrow.tutorial.ibc.state;

import defeatedcrow.tutorial.ibc.base.DCTank;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class DCTankv2 extends DCTank {

	public DCTankv2(int cap) {
		super(cap);
	}

	// 水以外は入らないタンクに改造
	@Override
	public boolean canFillTarget(FluidStack get) {
		if (get != null && get.getFluid() == FluidRegistry.WATER) {
			if (isEmpty()) {
				return true;
			} else if (get.isFluidEqual(fluid)) {
				return true;
			}
		}
		return false;
	}

}
