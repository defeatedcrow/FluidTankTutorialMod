package defeatedcrow.tutorial;

import defeatedcrow.tutorial.ibc.base.BlockIBC;
import defeatedcrow.tutorial.ibc.gui.BlockIBCv3;
import defeatedcrow.tutorial.ibc.packet.BlockIBCv1;
import defeatedcrow.tutorial.ibc.packet.TutorialPacket;
import defeatedcrow.tutorial.ibc.renderless.BlockIBCv4;
import defeatedcrow.tutorial.ibc.state.BlockIBCv2;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod(modid = CrowTutorial.MOD_ID, name = CrowTutorial.MOD_NAME, version = "1.0.0",
		dependencies = CrowTutorial.MOD_DEPENDENCIES, acceptedMinecraftVersions = CrowTutorial.MOD_ACCEPTED_MC_VERSIONS,
		useMetadata = true)
public class CrowTutorial {

	// modidはすべて小文字にする
	// assetsのフォルダの名前にもなるので、フォルダ名に不適な文字は含めない方がよい
	public static final String MOD_ID = "tutorial";
	public static final String MOD_NAME = "CrowTutorial";
	public static final String MOD_DEPENDENCIES = "";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.12,1.12.2]";

	// パスの記述は正確に!
	@SidedProxy(clientSide = "defeatedcrow.tutorial.ClientProxy", serverSide = "defeatedcrow.tutorial.CommonProxy")
	public static CommonProxy proxy;

	@Instance("tutorial")
	public static CrowTutorial instance;

	/* 追加ブロックのインスタンス */
	public static Block IBC_base;

	public static Block IBC_v1;
	public static Block IBC_v2;
	public static Block IBC_v3;
	public static Block IBC_v4;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		// インスタンスの生成
		// setRegistryNameが必須になったのが過去版との変更点
		IBC_base = new BlockIBC("ibc").setRegistryName(new ResourceLocation(MOD_ID, "ibc"));
		// Block、ItemBlockの登録。
		// RegistryEventを利用しなくても過去バージョンのように登録用メソッドは呼び出せる
		ForgeRegistries.BLOCKS.register(IBC_base);
		ForgeRegistries.ITEMS.register(new ItemBlock(IBC_base).setRegistryName(IBC_base.getRegistryName()));

		IBC_v1 = new BlockIBCv1("ibc_v1").setRegistryName(new ResourceLocation(MOD_ID, "ibc_v1"));
		ForgeRegistries.BLOCKS.register(IBC_v1);
		ForgeRegistries.ITEMS.register(new ItemBlock(IBC_v1).setRegistryName(IBC_v1.getRegistryName()));

		IBC_v2 = new BlockIBCv2("ibc_v2").setRegistryName(new ResourceLocation(MOD_ID, "ibc_v2"));
		ForgeRegistries.BLOCKS.register(IBC_v2);
		ForgeRegistries.ITEMS.register(new ItemBlock(IBC_v2).setRegistryName(IBC_v2.getRegistryName()));

		IBC_v3 = new BlockIBCv3("ibc_v3").setRegistryName(new ResourceLocation(MOD_ID, "ibc_v3"));
		ForgeRegistries.BLOCKS.register(IBC_v3);
		ForgeRegistries.ITEMS.register(new ItemBlock(IBC_v3).setRegistryName(IBC_v3.getRegistryName()));

		IBC_v4 = new BlockIBCv4("ibc_v4").setRegistryName(new ResourceLocation(MOD_ID, "ibc_v4"));
		ForgeRegistries.BLOCKS.register(IBC_v4);
		ForgeRegistries.ITEMS.register(new ItemBlock(IBC_v4).setRegistryName(IBC_v4.getRegistryName()));

		// モデル登録もpreInitで呼ぶ。(initでは遅い)
		// モデルなどクライアント限定の要素は、サーバー側では読み込むだけでクラッシュしてしまう
		// なので、proxyを利用したり、イベントのSideがどちらかを判別して、条件分けをする。
		CrowTutorial.proxy.registerModel();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		// EntityやTileEntityの登録はinitで行う
		CrowTutorial.proxy.registerTile();

		// v1のためのパケット登録
		TutorialPacket.init();

		// v3のためのGuiHandler登録
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

	}

}
