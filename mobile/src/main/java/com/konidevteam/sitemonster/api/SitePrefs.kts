import android.content.Context
import com.google.gson.Gson
import com.konidevteam.sitemonster.R
import com.konidevteam.sitemonster.api.HTTPRequest

data class Website (val name: String, val request: HTTPRequest)

/**
 * Creates new website anf puts it on device memory
 */
fun createWebsite(context: Context, website: Website) {

}

/**
 * Returns website by name from device memory
 */
fun getWebsiteByName(context: Context, name: String): Website {

}

/**
 * Changes name of website and saves it to device memory
 */
fun renameWebsite(context: Context, name: String, newName: String) {

}

/**
 * Replaces settings of website and saves it to device memory
 */
fun updateWebsiteSettings(context: Context, name: String, newSettings: Website) {

}

/**
 * Finds and returns website in array of websites by it's name
 */
fun getWebsiteByName(context: Context, name: String, websites: Array<Website>): Website {

}

/**
 * Gets array of all websites form device memory
 */
fun getAllWebsites(context: Context): Array<Website> {

}

/**
 * Saves all websites to device memory
 */
fun pushAllWebsites(context: Context, websites: Array<Website>) {
    val sharedPref = context.getSharedPreferences(context.getString(R.string.pref_file_key), Context.MODE_PRIVATE) ?: return
    with (sharedPref.edit()) {
        putString(context.getString(R.string.pref_websites_key), Gson().toJson(websites))
        apply()
    }
}