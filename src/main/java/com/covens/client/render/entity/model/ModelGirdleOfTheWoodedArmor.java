package com.covens.client.render.entity.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGirdleOfTheWoodedArmor extends ModelGirdleOfTheWooded {

	public ModelGirdleOfTheWoodedArmor() {
		 this.textureWidth = 64;
	        this.textureHeight = 64;
	        this.barkFront01 = new ModelRenderer(this, 2, 10);
	        this.barkFront01.setRotationPoint(0.0F, 10.0F, -3.5F);
	        this.barkFront01.addBox(0.0F, -4.0F, 0.0F, 4, 4, 1, 0.0F);
	        this.setRotateAngle(barkFront01, -0.017453292519943295F, -0.17453292519943295F, -0.03490658503988659F);
	        this.barkFront03 = new ModelRenderer(this, 13, 10);
	        this.barkFront03.setRotationPoint(-0.15169166028499603F, 6.0034685134887695F, -3.430000066757202F);
	        this.barkFront03.addBox(0.0F, -4.0F, 0.0F, 4, 4, 1, 0.0F);
	        this.setRotateAngle(barkFront03, 0.05563898619815241F, -0.052650028960594016F, -0.05438139575833576F);
	        this.barkBack01 = new ModelRenderer(this, 13, 16);
	        this.barkBack01.setRotationPoint(0.0F, 10.0F, 2.5F);
	        this.barkBack01.addBox(-4.0F, -4.0F, 0.0F, 4, 4, 1, 0.0F);
	        this.setRotateAngle(barkBack01, 0.06981317007977318F, -0.05235987755982988F, 0.03490658503988659F);
	        this.leaf02 = new ModelRenderer(this, 1, 48);
	        this.leaf02.setRotationPoint(-2.0F, 7.0F, 3.0999999046325684F);
	        this.leaf02.addBox(0.0F, 0.0F, 0.0F, 0, 3, 2, 0.0F);
	        this.setRotateAngle(leaf02, -0.08726646259971647F, 0.2617993877991494F, 0.3490658503988659F);
	        this.barkBack04 = new ModelRenderer(this, 12, 22);
	        this.barkBack04.setRotationPoint(0.12009649723768234F, 6.0041937828063965F, 2.359999895095825F);
	        this.barkBack04.addBox(0.0F, -4.0F, 0.0F, 4, 4, 1, 0.0F);
	        this.setRotateAngle(barkBack04, -0.10428736594955018F, 0.20939650018771477F, 0.03739541726948492F);
	        this.root01 = new ModelRenderer(this, 2, 34);
	        this.root01.setRotationPoint(1.0F, 4.0F, 2.5F);
	        this.root01.addBox(0.0F, -2.0F, 0.0F, 2, 2, 3, 0.0F);
	        this.setRotateAngle(root01, 0.3490658503988659F, 0.4363323129985824F, 0.0F);
	        this.leaf01 = new ModelRenderer(this, 24, 42);
	        this.leaf01.setRotationPoint(3.0F, 6.0F, 2.700000047683716F);
	        this.leaf01.addBox(0.0F, 0.0F, 0.0F, 0, 3, 2, 0.0F);
	        this.setRotateAngle(leaf01, 0.3490658503988659F, 0.06981317007977318F, -0.15707963267948966F);
	        this.barkFront02 = new ModelRenderer(this, 13, 28);
	        this.barkFront02.setRotationPoint(0.0F, 10.0F, -3.5F);
	        this.barkFront02.addBox(-4.0F, -4.0F, 0.0F, 4, 4, 1, 0.0F);
	        this.setRotateAngle(barkFront02, -0.03490658503988659F, 0.17453292519943295F, 0.0F);
	        this.vine = new ModelRenderer(this, 2, 40);
	        this.vine.setRotationPoint(-3.5F, 3.0999999046325684F, -3.5F);
	        this.vine.addBox(0.0F, 0.0F, 0.0F, 7, 7, 0, 0.0F);
	        this.leaf04 = new ModelRenderer(this, 6, 48);
	        this.leaf04.setRotationPoint(-3.0971789360046387F, 1.752339243888855F, 5.570000171661377F);
	        this.leaf04.addBox(0.0F, -1.399999976158142F, 0.0F, 0, 2, 3, 0.0F);
	        this.setRotateAngle(leaf04, -0.785398096818421F, -0.43633234628809603F, 0.0F);
	        this.root02 = new ModelRenderer(this, 13, 34);
	        this.root02.setRotationPoint(-3.0F, 4.0F, 3.0F);
	        this.root02.addBox(0.0F, -2.0F, 0.0F, 2, 2, 3, 0.0F);
	        this.setRotateAngle(root02, 0.3490658503988659F, -0.4363323129985824F, 0.0F);
	        this.barkFront04 = new ModelRenderer(this, 2, 16);
	        this.barkFront04.setRotationPoint(-0.02782178483903408F, 5.991966724395752F, -3.559999942779541F);
	        this.barkFront04.addBox(-4.0F, -4.0F, 0.0F, 4, 4, 1, 0.0F);
	        this.setRotateAngle(barkFront04, 0.12217304763960307F, 0.17453292519943295F, 0.0F);
	        this.leaf03 = new ModelRenderer(this, 17, 42);
	        this.leaf03.setRotationPoint(2.953157663345337F, 2.0342469215393066F, 4.320000171661377F);
	        this.leaf03.addBox(0.0F, 0.0F, 0.0F, 0, 2, 3, 0.0F);
	        this.setRotateAngle(leaf03, 0.1662546886880034F, 0.7276389930435918F, 0.1420108014415488F);
	        this.belt = new ModelRenderer(this, 2, 3);
	        this.belt.setRotationPoint(0.0F, 10.0F, 0.0F);
	        this.belt.addBox(-4.0F, 0.0F, -2.0F, 8, 2, 4, 0.01F);
	        this.barkBack03 = new ModelRenderer(this, 2, 22);
	        this.barkBack03.setRotationPoint(0.15385213494300842F, 6.012684345245361F, 2.2200000286102295F);
	        this.barkBack03.addBox(-4.0F, -4.0F, 0.0F, 4, 4, 1, 0.0F);
	        this.setRotateAngle(barkBack03, -0.033176090011282924F, -0.03248503091732639F, 0.001290759669067236F);
	        this.barkBack02 = new ModelRenderer(this, 2, 28);
	        this.barkBack02.setRotationPoint(0.0F, 10.0F, 2.5F);
	        this.barkBack02.addBox(0.0F, -4.0F, 0.0F, 4, 4, 1, 0.0F);
	        this.setRotateAngle(barkBack02, 0.03490658503988659F, 0.13962634015954636F, 0.03490658503988659F);
	}
}
