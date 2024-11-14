import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityFriendsListBinding


class FriendsListActivity : AppCompatActivity() {
    lateinit var binding: ActivityFriendsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFriendsListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarFriendsList)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragment = FriendsListFragment()
        val fragmentManager = supportFragmentManager

        fragmentManager.beginTransaction().replace(binding.fragmentFriendList.id,fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)

        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}

//manifest에 임시로 parent poceketBoard로 해놓음
