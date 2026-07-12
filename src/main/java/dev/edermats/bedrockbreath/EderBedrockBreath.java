package dev.edermats.bedrockbreath;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class EderBedrockBreath implements ModInitializer {
    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

            BlockPos pos = hitResult.getBlockPos();
            if (world.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
                return InteractionResult.PASS;
            }

            ItemStack heldStack = player.getItemInHand(hand);
            if (!heldStack.is(Items.DRAGON_BREATH)) {
                return InteractionResult.PASS;
            }

            if (world.isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            world.setBlock(pos, world.getRandom().nextBoolean()
                    ? Blocks.OBSIDIAN.defaultBlockState()
                    : Blocks.CRYING_OBSIDIAN.defaultBlockState(), 3);
            world.playSound(null, pos, SoundEvents.ENDER_DRAGON_HURT,
                    SoundSource.BLOCKS, 1.0F, 1.0F);

            if (!player.isCreative()) {
                heldStack.shrink(1);
                ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!player.getInventory().add(emptyBottle)) {
                    player.drop(emptyBottle, false);
                }
            }

            return InteractionResult.SUCCESS;
        });
    }
}
