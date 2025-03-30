package com.github.freedownloadhere.packetstuff

import net.minecraft.client.Minecraft
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class Nuker {
    private val l = 1

    @SubscribeEvent
    fun update(e : ClientTickEvent) {
        if(e.phase != TickEvent.Phase.END) return
        if(Minecraft.getMinecraft().thePlayer == null) return
        if(Minecraft.getMinecraft().theWorld == null) return

        val player = Minecraft.getMinecraft().thePlayer
        val queue = player.sendQueue
        val center = player.position

        for(x in -l..l) for(y in -l..l) for(z in -l..l) {
            val blockPos = BlockPos(
                center.x + x,
                center.y + y,
                center.z + z
            )

            val packet1 = C07PacketPlayerDigging(
                C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                blockPos,
                EnumFacing.DOWN
            )

            val packet2 = C07PacketPlayerDigging(
                C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                blockPos,
                EnumFacing.DOWN
            )

            Chat.send("Breaking block $blockPos")
            queue.addToSendQueue(packet1)
            queue.addToSendQueue(packet2)
        }
    }
}