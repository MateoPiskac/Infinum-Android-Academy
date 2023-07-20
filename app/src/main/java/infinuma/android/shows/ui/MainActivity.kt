package infinuma.android.shows.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import infinuma.android.shows.R
import infinuma.android.shows.data.Show

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    lateinit var currentShow: Show
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState==null){
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<LoginFragment>(R.id.fragmentContainerView)
                addToBackStack("Fragment Login")
            }
        }
    }

    fun openShowDetails(show: Show){
        currentShow=show
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<ShowDetailsFragment>(R.id.fragmentContainerView)
            addToBackStack("Fragment Shows")
        }
    }


    fun back(){
        supportFragmentManager.popBackStack()
    }
    fun logout(){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<LoginFragment>(R.id.fragmentContainerView)
        }
        removeAllFragmentsFromBackStack()
    }
    fun login(){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ShowsFragment>(R.id.fragmentContainerView)
            addToBackStack("Logged in")
        }
    }

    private fun removeAllFragmentsFromBackStack() {
        val fragmentManager = supportFragmentManager
        val backStackEntryCount = fragmentManager.backStackEntryCount

        for (i in 0 until backStackEntryCount) {
            fragmentManager.popBackStackImmediate()
        }
    }

}