package com.github.freedownloadhere.packetstuff

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText

object Chat {
    fun send(msg : String) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(
            ChatComponentText(msg)
        )
    }
}