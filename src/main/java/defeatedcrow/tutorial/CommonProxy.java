package defeatedcrow.tutorial;

import defeatedcrow.tutorial.ibc.base.TileIBC;
import defeatedcrow.tutorial.ibc.gui.ContainerIBCv3;
import defeatedcrow.tutorial.ibc.gui.GuiIBCv3;
import defeatedcrow.tutorial.ibc.gui.TileIBCv3;
import defeatedcrow.tutorial.ibc.packet.TileIBCv1;
import defeatedcrow.tutorial.ibc.renderless.TileIBCv4;
import defeatedcrow.tutorial.ibc.state.TileIBCv2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {

	public void registerModel() {}

	public void registerTile() {
		GameRegistry.registerTileEntity(TileIBC.class, new ResourceLocation(CrowTutorial.MOD_ID, "te_ibc"));
		GameRegistry.registerTileEntity(TileIBCv1.class, new ResourceLocation(CrowTutorial.MOD_ID, "te_ibc_v1"));
		GameRegistry.registerTileEntity(TileIBCv2.class, new ResourceLocation(CrowTutorial.MOD_ID, "te_ibc_v2"));
		GameRegistry.registerTileEntity(TileIBCv3.class, new ResourceLocation(CrowTutorial.MOD_ID, "te_ibc_v3"));
		GameRegistry.registerTileEntity(TileIBCv4.class, new ResourceLocation(CrowTutorial.MOD_ID, "te_ibc_v4"));
	}

	// v1のためのクライアント側のEntityPlayer取得メソッド

	public EntityPlayer getPlayer() {
		return null;
	}

	/* v3のためのGuiハンドラの実装 */

	// サーバー側にはContainerを渡す
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			BlockPos pos = new BlockPos(x, y, z);
			if (!world.isBlockLoaded(pos))
				return null;
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileIBCv3)
				return new ContainerIBCv3((TileIBCv3) tile, player);
		}
		return null;
	}

	// クライアント側にはGuiを渡す
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			BlockPos pos = new BlockPos(x, y, z);
			if (!world.isBlockLoaded(pos))
				return null;
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileIBCv3)
				return new GuiIBCv3((TileIBCv3) tile, player);
		}
		return null;
	}

}
