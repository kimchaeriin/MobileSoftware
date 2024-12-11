package com.practice.android.pocketmate

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.practice.android.pocketmate.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Adapter.ViewPagerAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.friends.FriendsListActivity
import com.practice.android.pocketmate.util.ScreenUtils
import com.practice.android.pocketmate.util.FBRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.databinding.NavigationHeaderBinding
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.practice.android.pocketmate.Settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tipAdapter : ViewPagerAdapter
    private lateinit var pocketAdapter: ViewPagerAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var headerBinding: NavigationHeaderBinding
    private val pocket = false
    private val tip = true
    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentTipPosition = 0
    private var currentPocketPosition = 0

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        val toggle = ActionBarDrawerToggle(this, binding.drawerMain, binding.toolbarMain, R.string.drawer_opened, R.string.drawer_closed)
        toggle.isDrawerIndicatorEnabled = false
        binding.drawerMain.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_drawer_friends -> { ScreenUtils.switchScreen(this, FriendsListActivity::class.java)}
                R.id.nav_drawer_settings -> { ScreenUtils.switchScreen(this, SettingsActivity::class.java) }
//                R.id.nav_drawer_statistics -> {ScreenUtils.switchScreen(this, StatisticsActivity::class.java)}
            }
            binding.drawerMain.closeDrawers()
            true
        }
        val navView = binding.navigationDrawer
        val headerView = navView.getHeaderView(0)
        headerBinding = NavigationHeaderBinding.bind(headerView)

        headerBinding.navCopyBtn.setOnClickListener{
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("회원 아이디", headerBinding.navUid.text.toString())
            clipboard.setPrimaryClip(clip)
        }
        getProfile()
        
        getRecentTip()
        getRecentPocket()
        binding.pocketViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.tipViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        ScreenUtils.setBottomNavigationBar(this, binding.navigation)

//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//            Log.d("token", token)
//
//            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//        })
    }



    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        scope.cancel()
    }

    private fun startAutoTipScroll() {
        scope.launch {
            while (isActive) {
                delay(3000)
                if (tipAdapter.itemCount > 0) {
                    currentTipPosition = (currentTipPosition + 1) % tipAdapter.itemCount
                    binding.tipViewPager.setCurrentItem(currentTipPosition, true)
                }
            }
        }
    }

    private fun startAutoPocketScroll() {
        scope.launch {
            while (isActive) {
                delay(3000)
                if (pocketAdapter.itemCount > 0) {
                    currentPocketPosition = (currentPocketPosition + 1) % pocketAdapter.itemCount
                    binding.pocketViewPager.setCurrentItem(currentPocketPosition, true)
                }
            }
        }
    }

    private fun getRecentTip() {
        val tipList = mutableListOf<BoardModel>()
        val tipKeyList = mutableListOf<String>()
        FBRef.tipRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tipList.clear()
                tipKeyList.clear()
                val dataList = dataSnapshot.children.reversed()
                for (data in dataList) {
                    val tip = data.getValue(BoardModel::class.java)
                    tipList.add(tip!!)
                    tipKeyList.add(data.key.toString())
                }
                if (tipList.isEmpty()) {
                    tipKeyList.add("")
                    tipList.add(BoardModel(content = "\t\t\t\t\t\t\t\t\t\t\t\t지금은 게시글이 없어요.\n\t\t\t\t\t\t\t\t\t\t\t\t첫 게시글을 써주세요!"))
                }

                tipAdapter = ViewPagerAdapter(this@MainActivity, tipList, tipKeyList, tip)

                binding.tipViewPager.adapter = tipAdapter
                startAutoTipScroll()
            }
            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }

    private fun getRecentPocket() {
        val pocketList = mutableListOf<BoardModel>()
        val pocketKeyList = mutableListOf<String>()
        FBRef.pocketRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pocketList.clear()
                pocketKeyList.clear()
                val dataList = dataSnapshot.children.reversed()
                for (data in dataList) {
                    val pocket = data.getValue(BoardModel::class.java)
                    pocketList.add(pocket!!)
                    pocketKeyList.add(data.key.toString())
                }
                if (pocketList.isEmpty()) {
                    pocketKeyList.add("")
                    pocketList.add(BoardModel(content = "\t\t\t\t\t\t\t\t\t\t\t지금은 게시글이 없어요.\n\t\t\t\t\t\t\t\t\t\t\t\t첫 게시글을 써주세요!"))
                }
                pocketAdapter = ViewPagerAdapter(this@MainActivity, pocketList, pocketKeyList, pocket)
                binding.pocketViewPager.adapter = pocketAdapter
                startAutoPocketScroll()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }

    private fun getProfile() {
        val uid = FBAuth.getUid()
        headerBinding.navUid.text = uid
        getNickname { nickname ->
            headerBinding.navNickname.setText(nickname)
        }
    }

    private fun getNickname(callback: (String) -> Unit) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val nickname = dataSnapshot.getValue(String::class.java) ?: ""
                callback(nickname)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                callback("") // 에러가 발생한 경우 빈 문자열을 콜백으로 전달
            }
        }
        FBRef.nicknameRef.child(FBAuth.getUid()).addListenerForSingleValueEvent(postListener)
    }
}
