package cc.mewcraft.toolbox.hotfix

import cc.mewcraft.toolbox.hotfix.listener.HotfixListener
import org.koin.core.module.Module
import org.koin.dsl.module

fun hotfixModule(): Module = module {
    single<HotfixListener> { HotfixListener }
}