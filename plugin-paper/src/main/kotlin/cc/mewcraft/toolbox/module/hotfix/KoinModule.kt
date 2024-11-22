package cc.mewcraft.toolbox.module.hotfix

import cc.mewcraft.toolbox.module.hotfix.listener.HotfixListener
import org.koin.core.module.Module
import org.koin.dsl.module

fun hotfixModule(): Module = module {
    single<HotfixListener> { HotfixListener }
}