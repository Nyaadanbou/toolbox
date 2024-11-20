package cc.mewcraft.toolbox

import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

enum class RootKoinModule {
    MAIN_CONFIG,
    DATA_FOLDER,
}

fun rootModule(toolboxPlugin: ToolboxPlugin) = module {
    single<ToolboxPlugin> {
        toolboxPlugin
    }
    single(named(RootKoinModule.DATA_FOLDER)) {
        toolboxPlugin.dataFolder
    }
    single(named(RootKoinModule.MAIN_CONFIG)) {
        get<File>(named(RootKoinModule.DATA_FOLDER)).resolve("config.yml")
    }
}