package com.rwtema.funkylocomotion.items;

import com.rwtema.funkylocomotion.FunkyLocomotion;
import com.rwtema.funkylocomotion.movers.IMover;
import com.rwtema.funkylocomotion.movers.MoverEventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

public class ItemWrench extends Item {
    public static final int metaWrenchNormal = 0;
    public static final int metaWrenchEye = 1;

    public ItemWrench() {
        super();
        this.setMaxStackSize(1);
        this.setTextureName("funkylocomotion:wrench");
        this.setUnlocalizedName("funkylocomotion:wrench");
        this.setCreativeTab(FunkyLocomotion.creativeTabFrames);
        this.setHasSubtypes(true);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IIcon iconWrenchEye;
    @SideOnly(Side.CLIENT)
    public IIcon iconWrenchEye_base;
    @SideOnly(Side.CLIENT)
    public IIcon iconWrenchEye_pupil;
    @SideOnly(Side.CLIENT)
    public IIcon iconWrenchEye_outline;


    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        iconWrenchEye = register.registerIcon("funkylocomotion:wrench_eye");
        iconWrenchEye_base = register.registerIcon("funkylocomotion:wrench_eye_base");
        iconWrenchEye_pupil = register.registerIcon("funkylocomotion:wrench_eye_pupil");
        iconWrenchEye_outline= register.registerIcon("funkylocomotion:wrench_eye_outline");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        if(meta == metaWrenchEye) return iconWrenchEye;
        return super.getIconFromDamage(meta);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
        if(!player.worldObj.isRemote){
            TileEntity tileEntity = player.worldObj.getTileEntity(X, Y, Z);
            if(tileEntity instanceof IMover){
                MoverEventHandler.registerMover((IMover) tileEntity);
            }
        }
        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if(itemstack.getItemDamage() == metaWrenchEye)
            return "item.funkylocomotion:wrench_eye";
        else
            return super.getUnlocalizedName(itemstack);
    }

    @SubscribeEvent
    public void leftClick(PlayerInteractEvent event){
        if(event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) return;
        if(event.entityPlayer.getHeldItem() == null || event.entityPlayer.getHeldItem().getItem() != this) return;
        if(!event.world.isRemote){
            TileEntity tileEntity = event.world.getTileEntity(event.x, event.y, event.z);
            if(tileEntity instanceof IMover){
                MoverEventHandler.registerMover((IMover) tileEntity);
            }
        }
        event.setCanceled(true);
    }

    @Override
    public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
        return 0;
    }
}
