import android.content.Context
import com.google.gson.Gson
import com.konidevteam.sitemonster.R
import com.konidevteam.sitemonster.api.HTTPRequest
import java.util.prefs.InvalidPreferencesFormatException

data class Website (var name: String, var request: HTTPRequest)

/**
 * Creates new website anf puts it on device memory
 */
fun createWebsite(context: Context, website: Website) {
    val websites = getAllWebsites(context)
    websites.forEach {
        if (it.name == website.name)
            throw Exception("Name is already taken")
    }
    pushAllWebsites(context, websites.plus(website))
}

/**
 * Returns website by name from device memory
 */
fun getWebsiteByName(context: Context, name: String): Website {
    val websites = getAllWebsites(context)
    websites.forEach {
        if (it.name == name)
            return it
    }
    throw NoSuchFieldException("Can't find website with name $name")
}

/**
 * Changes name of website and saves it to device memory
 */
fun renameWebsite(context: Context, name: String, newName: String) {
    val websites = getAllWebsites(context)
    websites.forEach {
        if (it.name == name) {
            it.name = newName
        }
    }
    pushAllWebsites(context, websites)
}

/**
 * Replaces settings of website and saves it to device memory
 */
fun updateWebsiteSettings(context: Context, name: String, newSettings: Website) {
    val websites = getAllWebsites(context)

    run loop@{
        websites.forEachIndexed { idx, value ->
            if (value.name == name) {
                websites[idx] = newSettings
                return@loop
            }
        }
    }

    pushAllWebsites(context, websites)
}

/**
 * Gets array of all websites form device memory
 */
fun getAllWebsites(context: Context): Array<Website> {
    val sharedPref = context.getSharedPreferences(context.getString(R.string.pref_file_key), Context.MODE_PRIVATE) ?: throw InvalidPreferencesFormatException("Can't load android shared prefernces")
    return Gson().fromJson(sharedPref.getString(context.getString(R.string.pref_websites_key), "[]"), Array<Website>::class.java)
}

/**
 * Saves all websites to device memory
 */
fun pushAllWebsites(context: Context, websites: Array<Website>) {
    val sharedPref = context.getSharedPreferences(context.getString(R.string.pref_file_key), Context.MODE_PRIVATE) ?: throw InvalidPreferencesFormatException("Can't load android shared prefernces")
    with (sharedPref.edit()) {
        putString(context.getString(R.string.pref_websites_key), Gson().toJson(websites))
        apply()
    }
}