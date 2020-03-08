package defeatedcrow.tutorial.ibc.packet;

import defeatedcrow.tutorial.ibc.base.BlockIBC;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockIBCv1 extends BlockIBC {

	public BlockIBCv1(String s) {
		super(s);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileIBCv1();
	}

}
