package cc.mewcraft.toolbox.module.networkfilter

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun networkFilterModule(): Module = module {
    singleOf(::NetworkFilterModule)
}