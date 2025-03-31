package com.github.freedownloadhere.packetstuff

import net.minecraft.client.Minecraft
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import kotlin.math.floor

class Nuker {
    private val l = 2

    @SubscribeEvent
    fun update(e : ClientTickEvent) {
        if(e.phase != TickEvent.Phase.END) return
        if(Minecraft.getMinecraft().thePlayer == null) return
        if(Minecraft.getMinecraft().theWorld == null) return

        val player = Minecraft.getMinecraft().thePlayer
        val queue = player.sendQueue
        val center = Vec3i(
            floor(player.posX).toInt(),
            floor(player.posY).toInt(),
            floor(player.posZ).toInt(),
        )

        for(x in -l..l) for(y in -l..l) for(z in -l..l) {
            val blockPos = BlockPos(
                center.x + x,
                center.y + y,
                center.z + z
            )

            val packet = C07PacketPlayerDigging(
                C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                blockPos,
                EnumFacing.DOWN
            )

            queue.addToSendQueue(packet)
        }
    }
}