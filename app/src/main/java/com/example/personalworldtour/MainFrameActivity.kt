package com.example.personalworldtour

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.viewpager.widget.ViewPager
import com.example.personalworldtour.fragments.ExploreFragment
import com.example.personalworldtour.fragments.MapViewFragment
import com.example.personalworldtour.fragments.PersonalEntriesFragment
import com.example.personalworldtour.fragments.adapters.ViewPagerAdapter
import com.example.personalworldtour.sql_lite.DatabaseHandler
import com.example.personalworldtour.sql_lite.UserData
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout


class  MainFrameActivity : AppCompatActivity() {

    private val IMAGE_GALLERY_REQUEST_CODE : Int = 100
    private val SAVE_IMAGE_REQUEST_CODE : Int = 101
    private val CAMERA_REQUEST_CODE : Int = 110
    val CAMERA_PERMISSION_REQUEST_CODE : Int = 111
    private lateinit var currentPhotoPath : String

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var viewPager : ViewPager
    private lateinit var  tabLayout : TabLayout
    private var userID : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_frame)
        val toolBar : Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)

//        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
//        val navView : NavigationView = findViewById(R.id.navView)
//      forwarded id from signIn is used to determine current user for personalization purpose
        userID = intent.getIntExtra("userId",-1)
        val databaseHandler = DatabaseHandler(this)
        val currentUser : UserData = databaseHandler.findUser(userID!!)
        val currentUsername = currentUser.uname
        val currentEmail = currentUser.email
//        val userPicture = currentUser.pic

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
//      set user data in header
//        val hView = navView.getHeaderView(0)
//        val headerImage = hView.findViewById<ImageView>(R.id.profileimage)
//        val headerUserName = hView.findViewById<TextView>(R.id.tvHeaderUserName)
//        val headerMail = hView.findViewById<TextView>(R.id.tvHeaderEmail)
//      temp picture
//        headerImage.setImageResource(R.drawable.nissan)
//        headerUserName.text = currentUsername
//        headerMail.text = currentEmail



//        toggle = ActionBarDrawerToggle(this,drawerLayout,toolBar,R.string.open,R.string.close)
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_open_24)

        setUpTabs()



//        navView.setNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.navHome -> {
//                    viewPager.setCurrentItem(0,true)
////                    drawerLayout.closeDrawers()
//                    true
//                }
//                R.id.navEditSettings -> {
//                    //TODO edit settings
////                    drawerLayout.closeDrawers()
//                    Toast.makeText(this,"Under construction",Toast.LENGTH_LONG).show()
//                    true
//                }
//                R.id.navEditProfile -> {
//                    val v : View = View(PersonalEntriesFragment().requireContext())
//
////                    drawerLayout.closeDrawers()
//                    Toast.makeText(this,"Under construction",Toast.LENGTH_LONG).show()
//                    true
//                }
//                R.id.navLogout -> {
//                    val intentLogOut = Intent(this,StartActivity::class.java)
//                    userID = null
//                    startActivity(intentLogOut)
//                    finish()
//                    true
//                }
//                R.id.navShare -> {
////                    drawerLayout.closeDrawers()
//                    Toast.makeText(this,"Under Construction",Toast.LENGTH_LONG).show()
//                    true
//                }
//                else -> {
//                    Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
//                    false
//                }
//            }
//        }

    }

    
    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(forvardUID(PersonalEntriesFragment()), "My Feed")
        adapter.addFragment(forvardUID(ExploreFragment()), "Explore")
        adapter.addFragment(MapViewFragment(), "Map")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_travel_explore)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_personal_entry)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_map_nav_white)

    }
    fun forvardUID(f : Fragment) : Fragment{
        val bundle = Bundle()
        bundle.putInt("userID", userID!!)
        f.arguments = bundle
        return f
    }

}