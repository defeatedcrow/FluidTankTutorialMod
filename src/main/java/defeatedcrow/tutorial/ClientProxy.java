package defeatedcrow.tutorial;

import defeatedcrow.tutorial.ibc.base.RenderIBC;
import defeatedcrow.tutorial.ibc.base.TileIBC;
import defeatedcrow.tutorial.ibc.renderless.RenderIBCv4;
import defeatedcrow.tutorial.ibc.renderless.TileIBCv4;
import defeatedcrow.tutorial.ibc.state.RenderIBCv2;
import defeatedcrow.tutorial.ibc.state.TileIBCv2;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerModel() {
		// jsonモデルの登録処理
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(
				CrowTutorial.IBC_base), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(CrowTutorial.MOD_ID + ":ibc", "inventory"));

		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(
				CrowTutorial.IBC_v1), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(CrowTutorial.MOD_ID + ":ibc", "inventory"));

		// v2は16種類のBlockStateを持っているが、jsonモデルとは関係ないので、持っているStateをignoreに登録する
		// IgnoreにしたStateは、Jsonモデルの登録時に無視することが出来る
		ModelLoader.setCustomStateMapper(CrowTutorial.IBC_v2, (new StateMap.Builder()).ignore(
				BlockLiquid.LEVEL).build());
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(
				CrowTutorial.IBC_v2), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(CrowTutorial.MOD_ID + ":ibc", "inventory"));

		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(
				CrowTutorial.IBC_v3), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(CrowTutorial.MOD_ID + ":ibc", "inventory"));

		// v4は異なる見た目を持っているので、モデルファイル名が異なる
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(
				CrowTutorial.IBC_v4), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(CrowTutorial.MOD_ID + ":pail", "inventory"));
	}

	@Override
	public void registerTile() {
		super.registerTile();
		ClientRegistry.bindTileEntitySpecialRenderer(TileIBC.class, new RenderIBC());
		// v2、v4は独自のTESRを設定
		ClientRegistry.bindTileEntitySpecialRenderer(TileIBCv2.class, new RenderIBCv2());
		ClientRegistry.bindTileEntitySpecialRenderer(TileIBCv4.class, new RenderIBCv4());
	}

	// v1のためのクライアント側のEntityPlayer取得メソッド

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}

}
