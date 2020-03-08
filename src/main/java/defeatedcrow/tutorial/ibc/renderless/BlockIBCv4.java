package defeatedcrow.tutorial.ibc.renderless;

import defeatedcrow.tutorial.ibc.base.BlockIBC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class BlockIBCv4 extends BlockIBC {

	public BlockIBCv4(String s) {
		super(s);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileIBCv4();
	}

	// jsonモデルを使用するように変更

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

}
