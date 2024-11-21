package cc.mewcraft.toolbox.module.enderdragon

import cc.mewcraft.toolbox.module.enderdragon.listener.EnderDragonListener
import org.koin.dsl.module

fun enderDragonModule() = module {
    single { EnderDragonListener }
}