package defeatedcrow.tutorial.ibc.base;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * 液体タンクの描画クラス
 */
@SideOnly(Side.CLIENT)
public class RenderIBC extends TileEntitySpecialRenderer<TileIBC> {

	protected static final String TEX = "tutorial:textures/tiles/ibc_cage.png";
	protected static final String TEX_BODY = "tutorial:textures/tiles/ibc_body.png";
	protected static final String TEX_BOTTOM = "tutorial:textures/tiles/black.png";
	protected static final ModelIBC MODEL = new ModelIBC();

	@Override
	public void render(TileIBC te, double x, double y, double z, float partialTicks, int destroyStage, float a) {

		if (te != null && te.hasWorld()) {

			// 底板
			this.bindTexture(new ResourceLocation(TEX_BOTTOM));
			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
			GlStateManager.scale(1.0F, -1.0F, -1.0F);
			GlStateManager.rotate(0.0F, 0.0F, 0.0F, 0.0F);
			MODEL.renderBottom(null, 0);
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();

			// ケージ
			this.bindTexture(new ResourceLocation(TEX));
			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
			GlStateManager.scale(1.0F, -1.0F, -1.0F);
			GlStateManager.rotate(0.0F, 0.0F, 0.0F, 0.0F);
			MODEL.renderCage(null, 0);
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();

			// 中身の液体
			renderTank(te, x, y, z, partialTicks, destroyStage, a);

			// 半透明の本体
			// 半透明部分は、他のパーツの後に描画する
			this.bindTexture(new ResourceLocation(TEX_BODY));
			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
			GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
			GlStateManager.scale(1.0F, -1.0F, -1.0F);
			GlStateManager.rotate(0.0F, 0.0F, 0.0F, 0.0F);
			MODEL.renderBody(null, 0);
			GL11.glDisable(GL11.GL_BLEND);
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();

		}
	}

	protected void renderTank(TileIBC te, double x, double y, double z, float partialTicks, int destroyStage, float a) {
		TileIBC pot = te;
		Fluid fluid = pot.inputT.getFluidType();
		if (fluid != null && pot.inputT.getFluidAmount() > 0) {
			renderFluid(fluid, x, y, z, partialTicks, pot.inputT.getFluidAmount());
		}
	}

	/*
	 * VortexBufferで中身の液体を描画するメソッド
	 * 量によってサイズを変え、水位の変化を表現する
	 */
	protected void renderFluid(Fluid fluid, double x, double y, double z, float partialTicks, int amount) {
		GlStateManager.disableLighting();
		TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
		TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite(fluid.getStill().toString());
		GlStateManager.pushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		float f2 = 0.06F + 0.85F * amount / 15000F;
		float f = 0.45F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		int i = 0;
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);

		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		float uMin = textureatlassprite.getMinU();
		float vMin = textureatlassprite.getMinV();
		float uMax = textureatlassprite.getMaxU();
		float vMax = textureatlassprite.getMaxV();

		vertexbuffer.pos(f, f2, -f).tex(uMax, vMax).endVertex();
		vertexbuffer.pos(-f, f2, -f).tex(uMin, vMax).endVertex();
		vertexbuffer.pos(-f, f2, f).tex(uMin, vMin).endVertex();
		vertexbuffer.pos(f, f2, f).tex(uMax, vMin).endVertex();

		vertexbuffer.pos(-f, f2, -f).tex(uMax, vMax).endVertex();
		vertexbuffer.pos(-f, 0, -f).tex(uMin, vMax).endVertex();
		vertexbuffer.pos(-f, 0, f).tex(uMin, vMin).endVertex();
		vertexbuffer.pos(-f, f2, f).tex(uMax, vMin).endVertex();

		vertexbuffer.pos(f, f2, f).tex(uMax, vMax).endVertex();
		vertexbuffer.pos(f, 0.0625F, f).tex(uMin, vMax).endVertex();
		vertexbuffer.pos(f, 0.0625F, -f).tex(uMin, vMin).endVertex();
		vertexbuffer.pos(f, f2, -f).tex(uMax, vMin).endVertex();

		vertexbuffer.pos(-f, f2, f).tex(uMax, vMax).endVertex();
		vertexbuffer.pos(-f, 0.0625F, f).tex(uMin, vMax).endVertex();
		vertexbuffer.pos(f, 0.0625F, f).tex(uMin, vMin).endVertex();
		vertexbuffer.pos(f, f2, f).tex(uMax, vMin).endVertex();

		vertexbuffer.pos(f, f2, -f).tex(uMax, vMax).endVertex();
		vertexbuffer.pos(f, 0.0625F, -f).tex(uMin, vMax).endVertex();
		vertexbuffer.pos(-f, 0.0625F, -f).tex(uMin, vMin).endVertex();
		vertexbuffer.pos(-f, f2, -f).tex(uMax, vMin).endVertex();

		tessellator.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();

	}
}
