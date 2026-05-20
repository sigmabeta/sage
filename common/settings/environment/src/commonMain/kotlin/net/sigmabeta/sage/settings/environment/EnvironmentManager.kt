package net.sigmabeta.sage.settings.environment

import kotlinx.coroutines.flow.map
import net.sigmabeta.sage.storage.common.Storage

class EnvironmentManager(
    private val storage: Storage,
    private val environments: List<AppEnvironment>,
    private val default: AppEnvironment,
) {
    fun setEnvironment(environment: AppEnvironment) {
        storage.saveInt(SETTING_ENVIRONMENT, environments.indexOf(environment))
    }

    fun selectedEnvironmentFlow() = storage.savedIntFlow(SETTING_ENVIRONMENT)
        .map {
            if (it == null) {
                default
            } else if (it < 0 || it >= environments.size) {
                default
            } else {
                environments[it]
            }
        }

    companion object {
        private const val SETTING_ENVIRONMENT = "setting.debug.environment"
    }
}
