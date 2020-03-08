package defeatedcrow.tutorial.ibc.gui;

import java.util.Map.Entry;

import defeatedcrow.tutorial.ibc.base.TileIBC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileIBCv3 extends TileIBC implements IInventory {

	/*
	 * IInventoryの実装。
	 * 1.12.2ではIContainerListener（旧CrafrtingCrater）用のパラメータがIInventoryに内包されたので、
	 * ほしいのはset/getFieldの部分のみだが、IInventoryを実装する。
	 */

	/*
	 * ダミーとしてスロット数が0個の空インベントリを実装。
	 * アイテムの出し入れは出来ないようにする。
	 */

	@Override
	public String getName() {
		return "ibc";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}

	@Override
	public void clear() {}

	/*
	 * 実際に必要なのはここから。
	 * GUIを開いているときにContainerクラスで送受信される値を設定する。
	 * ここでやり取りするint値は処理の途中でshortに丸められてしまうので、大きい値を送りたい場合は注意！
	 * （32767を超える値は分割して送る必要がある。）
	 */

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return getFluidID();
		case 1:
			return this.inputT.getFluidAmount();
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			Fluid f = getFluidByID(value);
			if (f == null)
				inputT.setFluid(null);
			else
				inputT.setFluid(new FluidStack(f, inputT.getFluidAmount()));
			break;
		case 1:
			this.inputT.setAmount(value);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 2;
	}

	/*
	 * この方法でFluidを送受信するためには、非推奨だがFluidの数値IDが必須になってしまう。
	 * つまり、FluidStackのためにIContainerListenerを利用するのは不適切である。
	 */

	private int getFluidID() {
		Fluid fluid = inputT.getFluidType();
		if (fluid != null) {
			int i = FluidRegistry.getRegisteredFluidIDs().get(fluid);
			return 1;
		}
		return -1;
	}

	public Fluid getFluidByID(int id) {
		for (Entry<Fluid, Integer> e : FluidRegistry.getRegisteredFluidIDs().entrySet()) {
			if (e.getValue() != null && e.getValue().intValue() == id) {
				inputT.setFluid(new FluidStack(e.getKey(), inputT.getFluidAmount()));
				return e.getKey();
			}
		}
		return null;
	}

}
