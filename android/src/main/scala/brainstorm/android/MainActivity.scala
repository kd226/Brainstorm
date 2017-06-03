package brainstorm.android

import android.app.Activity
import android.os.Bundle
import android.content.res.Configuration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import TypedResource._

class MainActivity extends DrawerLayoutActivity with TypedFindView {

    override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        afterOnCreate(savedInstanceState)

        val extras : Bundle = getIntent().getExtras()
        val value = extras.getString("MAIN")
        if(value == "1") {
            setFragment(new MainFragment())
        } else {
            setFragment(new SettingsFragment())
        }
    }

}
