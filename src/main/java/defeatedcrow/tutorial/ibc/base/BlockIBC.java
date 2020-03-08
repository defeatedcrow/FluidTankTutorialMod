package defeatedcrow.tutorial.ibc.base;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * 今回作った液体タンクすべての共通処理
 * 適宜Overrideで機能を変更して使う
 */
public class BlockIBC extends BlockContainer {

	public BlockIBC(String s) {
		super(Material.CLAY);
		this.setUnlocalizedName(s);
		this.setHardness(0.5F);
		this.setResistance(10.0F);
		this.fullBlock = false;
		this.lightOpacity = 0;
		this.setCreativeTab(CreativeTabs.DECORATIONS);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileIBC();
	}

	/* 右クリックで中身を出し入れする処理 */

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && player != null && hand == EnumHand.MAIN_HAND) {
			ItemStack heldItem = player.getHeldItem(hand);
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileIBC) {
				if (!heldItem.isEmpty()) {
					if (onActivateDCTank(tile, heldItem, world, state, side, player)) {
						world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 2.0F);
					}
				} else {
					// 右クリック時に中身をチャットメッセージで見せる
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
		return true;
	}

	public static boolean onActivateDCTank(TileEntity tile, ItemStack item, World world, IBlockState state,
			EnumFacing side, EntityPlayer player) {
		if (!item.isEmpty() && tile != null && item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
				side) && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) {
			ItemStack copy = item.copy();
			if (item.getCount() > 1)
				copy.setCount(1);
			IFluidHandlerItem dummy = copy.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			IFluidHandler intank = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
			IFluidHandler outtank = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
					EnumFacing.DOWN);

			// dummyを使った検証
			if (dummy != null && dummy.getTankProperties() != null && intank instanceof DCTank && outtank instanceof DCTank) {
				int max = dummy.getTankProperties()[0].getCapacity();
				FluidStack f1 = dummy.drain(max, false);
				DCTank dc_in = (DCTank) intank;
				DCTank dc_out = (DCTank) outtank;

				ItemStack ret = ItemStack.EMPTY;
				boolean success = false;
				// input
				if (f1 != null && dc_in.fill(f1, false) > 0) {
					int f2 = dc_in.fill(f1, false);
					FluidStack fill = dummy.drain(f2, true);
					ret = dummy.getContainer();
					if (fill != null && fill.amount > 0) {
						dc_in.fill(fill, true);
						success = true;
					}
				}
				// output
				else if (f1 == null && dc_out.drain(max, false) != null) {
					int drain = dummy.fill(dc_out.drain(max, false), true);
					ret = dummy.getContainer();
					if (drain > 0) {
						dc_out.drain(drain, true);
						success = true;
					}
				}

				if (success) {
					if (!player.capabilities.isCreativeMode) {
						item.shrink(1);
					}
					tile.markDirty();
					player.inventory.markDirty();
					if (!ret.isEmpty()) {
						EntityItem drop = new EntityItem(world, player.posX, player.posY + 0.25D, player.posZ, ret);
						world.spawnEntity(drop);
					}
					return true;
				}
			}
		}
		return false;
	}

	/* 破壊時に中身を保持する処理 */

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileIBC) {
			NBTTagCompound tag = stack.getTagCompound();
			if (tag != null) {
				((TileIBC) tile).setNBT(tag);
			}
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);
		int i = this.damageDropped(state);
		ItemStack drop = new ItemStack(this, 1, i);

		if (tile != null && tile instanceof TileIBC) {
			NBTTagCompound tag = new NBTTagCompound();
			tag = ((TileIBC) tile).getNBT(tag);
			if (tag != null)
				drop.setTagCompound(tag);
		}

		if (!world.isRemote) {
			EntityItem entityitem = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, drop);
			float f3 = 0.05F;
			entityitem.motionX = (float) world.rand.nextGaussian() * f3;
			entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.25F;
			entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
			world.spawnEntity(entityitem);
		}
		world.updateComparatorOutputLevel(pos, state.getBlock());
		super.breakBlock(world, pos, state);

	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	/* 中身の量に応じてコンパレータ出力をする処理 */

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return calcRedstone(worldIn.getTileEntity(pos));
	}

	private int calcRedstone(TileEntity te) {
		if (te != null && te instanceof TileIBC) {
			TileIBC ibc = (TileIBC) te;
			DCTank tank = ibc.inputT;
			float amo = tank.getFluidAmount() * 15.0F / tank.getCapacity();
			int lit = MathHelper.floor(amo);
			return lit;
		}
		return 0;
	}

	/* クリエイティブタブへの登録 */

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (machCreativeTab(tab, getCreativeTabToDisplayOn())) {
			List<ItemStack> itms = getSubItemList();
			list.addAll(itms);
		}
	}

	public static boolean machCreativeTab(CreativeTabs target, CreativeTabs tab) {
		return tab != null && (target == CreativeTabs.SEARCH || target == tab);
	}

	public List<ItemStack> getSubItemList() {
		List<ItemStack> list = Lists.newArrayList();
		list.add(new ItemStack(this, 1, 0));
		return list;
	}

	/* Tooltipの情報表示 */

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		if (!stack.isEmpty() && stack.getTagCompound() != null) {
			FluidStack f = null;
			NBTTagList list = stack.getTagCompound().getTagList("Tank", 10);
			NBTTagCompound nbt2 = list.getCompoundTagAt(0);
			if (!nbt2.hasKey("Empty")) {
				f = FluidStack.loadFluidStackFromNBT(nbt2);
			}
			if (f != null && f.getFluid() != null) {
				tooltip.add(TextFormatting.BOLD.toString() + "CONTAINED FLUID");
				tooltip.add("Fluid: " + f.getLocalizedName());
				tooltip.add("Amount: " + f.amount + " mB");
			} else {
				tooltip.add(TextFormatting.BOLD.toString() + "CONTAINED FLUID");
				tooltip.add("Fluid: Empty");
			}
		} else {
			tooltip.add(TextFormatting.BOLD.toString() + "CONTAINED FLUID");
			tooltip.add("Fluid: Empty");
		}
	}

	/* Blockの基本設定 */

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}

}
