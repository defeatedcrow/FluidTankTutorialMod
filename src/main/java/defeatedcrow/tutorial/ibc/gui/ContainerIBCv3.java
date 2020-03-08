package defeatedcrow.tutorial.ibc.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * コンテナクラスの実装。
 */
public class ContainerIBCv3 extends Container {

	public final TileIBCv3 tile;
	public final EntityPlayer player;

	private int id;
	private int amount;

	public ContainerIBCv3(TileIBCv3 tile, EntityPlayer playerIn) {
		this.tile = tile;
		this.player = playerIn;
		tile.openInventory(player);

		/*
		 * GUI上に表示されるスロットの設定。
		 * このGUIの場合、テクスチャのスロット位置に合うようにプレイヤーのインベントリの内容を表示する。
		 */

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 142));
		}
	}

	/*
	 * IContainerListenerのget/setFieldの更新を実装
	 * 液体のIDや量をサーバー・クライアント間で同期する部分。
	 */

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tile);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener listener = this.listeners.get(i);

			if (this.id != this.tile.getField(0)) {
				listener.sendWindowProperty(this, 0, this.tile.getField(0));
			}

			if (this.amount != this.tile.getField(1)) {
				listener.sendWindowProperty(this, 1, this.tile.getField(1));
			}
		}

		this.id = this.tile.getField(0);
		this.amount = this.tile.getField(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		this.tile.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tile.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		this.tile.closeInventory(player);
	}

}
