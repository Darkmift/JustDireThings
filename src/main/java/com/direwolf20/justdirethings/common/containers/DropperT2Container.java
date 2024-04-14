package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class DropperT2Container extends BaseMachineContainer {

    public DropperT2Container(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, extraData.readBlockPos());
    }

    public DropperT2Container(int windowId, Inventory playerInventory, BlockPos blockPos) {
        super(Registration.DropperT2_Container.get(), windowId, playerInventory, blockPos);
        addPlayerSlots(player.getInventory());
    }

    @Override
    public void addMachineSlots() {
        machineHandler = baseMachineBE.getMachineHandler();
        addSlotBox(machineHandler, 0, 62, -1, 3, 18, 3, 18);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, Registration.DropperT2.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return super.quickMoveStack(playerIn, index);
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
    }
}
