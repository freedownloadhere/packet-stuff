package com.github.freedownloadhere.packetstuff

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.time.Instant
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.max
import kotlin.random.Random

class Killaura {
    private var toggled = true
    private val attackCooldown = 100L
    private var currentCooldown = 0L
    private var lastTime = Instant.now().toEpochMilli()

    private fun cooldownUpdateAndCheck() : Boolean {
        val currentTime = Instant.now().toEpochMilli()
        val deltaTime = (currentTime - lastTime) * Random.nextDouble(0.12, 1.9351)
        if(Random.nextDouble() < 0.05) return currentCooldown == 0L
        currentCooldown = max(0L, currentCooldown - deltaTime.toLong())
        lastTime = currentTime
        return currentCooldown == 0L
    }

    private fun goodEntityCheck(target : Entity, player : EntityPlayerSP) : Boolean {
        if(target !is EntityLivingBase) return false
        if(target == player) return false
        if(target.isDead) return false
        if(target.isInvisible) return false
        return true
    }

    private fun distanceCheck(target : Entity, player : EntityPlayerSP) : Boolean {
        val playerVec = player.positionVector
        val entityVec = target.positionVector
        val distance = entityVec.distanceTo(playerVec)
        return if(distance > 3.0) false else true
    }

    private fun raycastCheck(target : Entity, player : EntityPlayerSP) : Boolean {
        val playerVec = player.positionVector.add(Vec3(0.0, player.eyeHeight.toDouble(), 0.0))
        val entityVec = target.positionVector.add(Vec3(0.0, player.eyeHeight.toDouble(), 0.0))
        val mop = Minecraft.getMinecraft().theWorld.rayTraceBlocks(playerVec, entityVec)
        return mop == null || (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit == target)
    }

    private fun fovCheck(target : Entity, player : EntityPlayerSP) : Boolean {
        val playerVec = player.positionVector
        val entityVec = target.positionVector
        val deltaVec = entityVec.subtract(playerVec)

        val posYaw = -atan2(deltaVec.xCoord, deltaVec.zCoord).toDegrees().toFloat().cropAngle180()
        val posPitch = -atan2(deltaVec.yCoord, hypot(deltaVec.xCoord, deltaVec.zCoord)).toDegrees().toFloat()

        val currentYaw = player.rotationYaw.cropAngle180()
        val currentPitch = player.rotationPitch

        val deltaYaw = (posYaw - currentYaw).cropAngle180()
        val deltaPitch = posPitch - currentPitch

        val passed = abs(deltaYaw) <= 70.0f && abs(deltaPitch) <= 70.0f

        return passed
    }

    private fun simulateAttack(target : Entity, player : EntityPlayerSP) {
        val packet = C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK)
        player.swingItem()
        player.sendQueue.addToSendQueue(packet)
        currentCooldown = attackCooldown
    }

    @SubscribeEvent
    fun update(e : ClientTickEvent) {
        if(!toggled) return

        if(e.phase != TickEvent.Phase.START) return
        if(Minecraft.getMinecraft().thePlayer == null) return
        if(Minecraft.getMinecraft().theWorld == null) return

        val player = Minecraft.getMinecraft().thePlayer
        val entityList = Minecraft.getMinecraft().theWorld.loadedEntityList

        for(entity in entityList) {
            if(!cooldownUpdateAndCheck()) continue
            if(!goodEntityCheck(entity, player)) continue
            if(!distanceCheck(entity, player)) continue
            if(!raycastCheck(entity, player)) continue
            if(!fovCheck(entity, player)) continue

            simulateAttack(entity, player)
        }
    }
}