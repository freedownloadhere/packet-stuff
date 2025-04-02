package com.github.freedownloadhere.packetstuff

import net.minecraft.client.Minecraft
import net.minecraft.network.play.client.C0BPacketEntityAction
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class Sprint {
    @SubscribeEvent
    fun update(e : ClientTickEvent) {
        if(e.phase != TickEvent.Phase.START) return
        if(Minecraft.getMinecraft().thePlayer == null) return
        if(Minecraft.getMinecraft().theWorld == null) return
        //if(!Minecraft.getMinecraft().gameSettings.keyBindForward.isPressed) return

        Chat.send("pressing sprint xd")

        val player = Minecraft.getMinecraft().thePlayer
        val packet = C0BPacketEntityAction(player, C0BPacketEntityAction.Action.START_SPRINTING)
        player.sendQueue.addToSendQueue(packet)
    }
}