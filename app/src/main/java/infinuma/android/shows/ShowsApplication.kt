package infinuma.android.shows;

import android.app.Application;

import infinuma.android.shows.data.ShowsDatabase;
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShowsApplication : Application() {

    val database by lazy { ShowsDatabase.getDatabase(this) }
    override fun onCreate() {
        super.onCreate();
        GlobalScope.launch {
            //database.showDAO().insertAllShows(emptyList())
        }

    }
}
