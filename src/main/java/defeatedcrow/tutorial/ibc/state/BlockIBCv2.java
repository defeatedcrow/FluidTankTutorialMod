package defeatedcrow.tutorial.ibc.state;

import defeatedcrow.tutorial.ibc.base.BlockIBC;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockIBCv2 extends BlockIBC {

	/*
	 * バニラ水ブロックの水位State(LEVEL)を拝借してメタデータに水位のPropertyをつける
	 */
	public BlockIBCv2(String s) {
		super(s);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLiquid.LEVEL, 0));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileIBCv2();
	}

	/* Stateの設定 */

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockLiquid.LEVEL, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockLiquid.LEVEL).intValue();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {
				BlockLiquid.LEVEL
		});
	}

	/* Stateに応じてRS動力を出す設定 */

	@Override
	public boolean canProvidePower(IBlockState state) {
		return state.getValue(BlockLiquid.LEVEL).intValue() > 0;
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return state.getWeakPower(access, pos, side);
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return state.getValue(BlockLiquid.LEVEL).intValue();
	}

}
