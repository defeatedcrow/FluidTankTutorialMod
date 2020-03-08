package defeatedcrow.tutorial.ibc.gui;

import defeatedcrow.tutorial.CrowTutorial;
import defeatedcrow.tutorial.ibc.base.BlockIBC;
import defeatedcrow.tutorial.ibc.base.TileIBC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class BlockIBCv3 extends BlockIBC {

	public BlockIBCv3(String s) {
		super(s);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileIBCv3();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && player != null && hand == EnumHand.MAIN_HAND) {
			/*
			 * ここではプレイヤーがスニークしている間、
			 * 右クリックでGUIを開く処理に分岐する。
			 */
			if (player.isSneaking()) {
				player.openGui(CrowTutorial.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
			} else {
				/*
				 * 非スニーク時には、
				 * 持っているアイテムが液体容器の場合にタンクを使用する処理に分岐する。
				 */
				ItemStack heldItem = player.getHeldItem(hand);
				TileEntity tile = world.getTileEntity(pos);
				if (tile instanceof TileIBC) {
					if (!heldItem.isEmpty()) {
						if (onActivateDCTank(tile, heldItem, world, state, side, player)) {
							world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F,
									2.0F);
						}
					} else {
						// 素手の場合、右クリック時に中身をチャットメッセージで見せる
						FluidStack fluid = ((TileIBC) tile).inputT.getFluid();
						String s = "Fluid: ";
						if (fluid == null || fluid.getFluid() == null) {
							s += "Empty";
						} else {
							s += fluid.getLocalizedName() + " " + fluid.amount + "mB";
						}
						player.sendMessage(new TextComponentString(s));
					}
					return true;
				}
			}
		}
		return true;
	}

}
