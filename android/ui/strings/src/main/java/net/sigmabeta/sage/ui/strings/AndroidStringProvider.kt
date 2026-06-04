package net.sigmabeta.sage.ui.strings

import android.content.res.Resources
import net.sigmabeta.sage.ui.SageStringId
import net.sigmabeta.sage.ui.StringProvider

class AndroidStringProvider(
    private val resources: Resources,
    private val toResourceId: (SageStringId) -> Int,
) : StringProvider {
    override fun getString(string: SageStringId) = resources.getString(toResourceId(string))

    override fun getStringOneArg(string: SageStringId, arg: String) = resources.getString(toResourceId(string), arg)

    override fun getStringOneInt(string: SageStringId, arg: Int) = resources.getString(toResourceId(string), arg)

    override fun getStringTwoArgs(string: SageStringId, first: String, second: String) =
        resources.getString(toResourceId(string), first, second)
}
