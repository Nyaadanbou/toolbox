package cc.mewcraft.toolbox.module

import cc.mewcraft.toolbox.module.enderdragon.enderDragonModule
import cc.mewcraft.toolbox.module.hotfix.hotfixModule
import cc.mewcraft.toolbox.module.networkfilter.networkFilterModule
import org.koin.core.module.Module
import org.koin.dsl.module

fun toolboxModules(): Module = module {
    includes(
        enderDragonModule(),
        hotfixModule(),
        networkFilterModule(),
    )
}