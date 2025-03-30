package com.github.freedownloadhere.packetstuff

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = "packetstuff", useMetadata = true)
class Mod {
    private val ka = Killaura()
    private val nuker = Nuker()

    @Mod.EventHandler
    fun init(e : FMLInitializationEvent) {
        // MinecraftForge.EVENT_BUS.register(ka)
        MinecraftForge.EVENT_BUS.register(nuker)
    }
}