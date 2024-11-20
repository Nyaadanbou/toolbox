package cc.mewcraft.toolbox.enderdragon

import cc.mewcraft.toolbox.enderdragon.listener.EnderDragonListener
import org.koin.dsl.module

fun enderDragonModule() = module {
    single { EnderDragonListener }
}