package com.direwolf20.justdirethings.common.items.armors.basearmors;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.interfaces.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.direwolf20.justdirethings.util.TooltipHelpers.*;

public class BaseBoots extends ArmorItem implements ToggleableTool, LeftClickableTool {
    public static final AttributeModifier stepHeight = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "justdirestepassist"), 1.0, AttributeModifier.Operation.ADD_VALUE);

    protected final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    protected final Map<Ability, AbilityParams> abilityParams = new EnumMap<>(Ability.class);

    public BaseBoots(Holder<ArmorMaterial> pMaterial, Item.Properties pProperties) {
        super(pMaterial, Type.BOOTS, pProperties);
    }

    @Override
    public EnumSet<Ability> getAbilities() {
        return abilities;
    }

    @Override
    public Map<Ability, AbilityParams> getAbilityParamsMap() {
        return abilityParams;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }

        boolean sneakPressed = Screen.hasShiftDown();
        appendFEText(stack, tooltip);
        if (sneakPressed) {
            appendToolEnabled(stack, tooltip);
            appendAbilityList(stack, tooltip);
        } else {
            appendToolEnabled(stack, tooltip);
            appendShiftForInfo(stack, tooltip);
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (itemSlot == Inventory.INVENTORY_SIZE + EquipmentSlot.FEET.getIndex() && !getPassiveTickAbilities(itemStack).isEmpty() && entity instanceof Player player) {
            armorTick(level, player, itemStack);
        }
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers itemAttributeModifiers = super.getDefaultAttributeModifiers(stack);
        if (canUseAbility(stack, Ability.STEPHEIGHT))
            itemAttributeModifiers = itemAttributeModifiers.withModifierAdded(Attributes.STEP_HEIGHT, stepHeight, EquipmentSlotGroup.FEET);

        if (!(stack.getItem() instanceof PoweredTool poweredTool))
            return itemAttributeModifiers;

        return poweredTool.getPoweredAttributeModifiers(stack, itemAttributeModifiers);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        if (stack.getItem() instanceof PoweredTool poweredTool) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage == null) return amount;
            HolderLookup.RegistryLookup<Enchantment> registrylookup = entity.level().getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            int unbreakingLevel = stack.getEnchantmentLevel(registrylookup.getOrThrow(Enchantments.UNBREAKING));
            double reductionFactor = Math.min(1.0, unbreakingLevel * 0.1);
            int finalEnergyCost = (int) Math.max(0, amount - (amount * reductionFactor));
            energyStorage.extractEnergy(finalEnergyCost, false);
            return 0;
        }
        return amount;
    }
//TODO Review
    /*
    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        if (stack.getItem() instanceof PoweredTool)
            return super.isBookEnchantable(stack, book) && canAcceptEnchantments(book);
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (stack.getItem() instanceof PoweredTool)
            return super.canApplyAtEnchantingTable(stack, enchantment) && canAcceptEnchantments(enchantment);
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    private boolean canAcceptEnchantments(ItemStack book) {
        return !(book.getEnchantmentLevel(Enchantments.MENDING) > 0);
    }

    private boolean canAcceptEnchantments(Enchantment enchantment) {
        return enchantment != Enchantments.MENDING;
    }*/
}
