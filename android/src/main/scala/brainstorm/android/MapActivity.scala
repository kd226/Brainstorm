package brainstorm.android

import scala.util.Try
import scala.util.Success
import scala.util.Failure

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.res.Configuration
import android.content.Intent
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.net.URI
import android.view.Menu
import android.view.MenuItem
import android.app.FragmentTransaction 
import android.widget.Toast
import android.util.Log
import android.preference.PreferenceManager 
import android.content.SharedPreferences 
import android.graphics.Color

import brainstorm.core.Parser
import brainstorm.core.MindMap


class MapActivity extends DrawerLayoutActivity with TypedFindView with
  SaveRealTimeDialogListener {

  var fileOpt: Option[File] = None
  var mapFragment: MapFragment = _
  //lazy val sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.map)
    afterOnCreate(savedInstanceState)

    val tryUri: Try[URI] = Try(getIntent.getExtras.get("file").asInstanceOf[URI]) 

    val tryFile: Try[File] = tryUri.flatMap(x => Try(new File(x)))
    fileOpt = tryFile.toOption
    val mindMapOpt = tryFile.flatMap(x => Try(Parser.parseFile(x.toURI))).toOption

    mindMapOpt match {
      case Some(mindMap) => setTitle(mindMap.name)
      case None => setTitle(R.string.rtmap_t)
    }

    val mindMap = mindMapOpt.getOrElse(new MindMap("tmp"))
    mapFragment = new MapFragment(mindMap)

    setFragment(mapFragment)
  }

  override def onPostCreate(savedInstanceState: Bundle) {
    super.onPostCreate(savedInstanceState)
  }

  override def onCreateOptionsMenu(menu : Menu) : Boolean = {
    //Adds items to the ActionBar
    getMenuInflater().inflate(R.menu.menu_map, menu)
    return true
  }

  def saveFile(file: File) {
        val pw = new PrintWriter(file)
        mapFragment.mindMapModel.mindMap.getText(" ").foreach(pw.println)
        Toast.makeText(context, file.getName ++ " saved", 0).show
        pw.close()
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    fileOpt match {
      case Some(file) => {
        saveFile(file)
      }
      case None => {
        val dialog = new SaveRealTimeDialog(MapActivity.this)
        dialog.show(getFragmentManager(), "missiles")
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override def onPositive(name: String) = {
    val mapsRootFile: File = new File(getFilesDir, "maps/")
    val file = new File(mapsRootFile, name)
    fileOpt = Some(file)
    saveFile(file)
    mapFragment.mindMapModel.mindMap.name = name
    setTitle(name)
  }
}
