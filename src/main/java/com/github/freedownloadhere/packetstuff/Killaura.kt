package com.github.freedownloadhere.packetstuff

import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLiving
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class Killaura {
    @SubscribeEvent
    fun update(e : ClientTickEvent) {
        if(e.phase != TickEvent.Phase.END) return
        if(Minecraft.getMinecraft().thePlayer == null) return
        if(Minecraft.getMinecraft().theWorld == null) return

        val player = Minecraft.getMinecraft().thePlayer
        val playerVec = player.positionVector
        val queue = player.sendQueue
        val entityList = Minecraft.getMinecraft().theWorld.loadedEntityList

        for(entity in entityList) {
            if(entity !is EntityLiving) continue
            if(entity == player) continue
            if(entity.isDead) continue

            val entityVec = entity.positionVector
            val distance = entityVec.distanceTo(playerVec)
            if(distance > 3.0) continue

            val packet = C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK)
            queue.addToSendQueue(packet)
        }
    }
}