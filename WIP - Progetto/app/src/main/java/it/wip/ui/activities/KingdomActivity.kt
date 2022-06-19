package it.wip.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.wip.R
import it.wip.data.DataKingdom
import it.wip.databinding.ActivityKingdomBinding
import it.wip.ui.fragments.MenuFragment
import it.wip.utils.KingdomListAdapter
import it.wip.viewModel.KingdomViewModel

class KingdomActivity : AppCompatActivity() {

    private lateinit var viewModel: KingdomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kingdom)

        val binding: ActivityKingdomBinding = DataBindingUtil.setContentView(this, R.layout.activity_kingdom)

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application))[KingdomViewModel::class.java]

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val numChapters = 1
        // data list of stories settled on the vertical recyclerView inside the KingdomActivity
        val storyList = ArrayList<DataKingdom>()

        for (i in 0..viewModel.storiesName.lastIndex) {
            if(i%2==0) {
                storyList.add(
                    DataKingdom(
                        KingdomListAdapter.THE_FIRST_VIEW,
                        viewModel.storiesName[i],
                        "Chapters: $numChapters"
                    )
                )
            }
            else {
                storyList.add(
                    DataKingdom(
                        KingdomListAdapter.THE_SECOND_VIEW,
                        viewModel.storiesName[i],
                        "Chapters: $numChapters"
                    )
                )
            }
        }

        // data list of stories settled on the horizontal recyclerView inside the KingdomActivity
        val storyHorizontalList = ArrayList<DataKingdom>()
        for((i, item) in storyList.withIndex()) {
            storyHorizontalList.add(DataKingdom(
                KingdomListAdapter.THE_HORIZONTAL_VIEW, storyList[i].textData))
        }


        val rvV = findViewById<RecyclerView>(R.id.rv_vertical)
        val rvH = findViewById<RecyclerView>(R.id.rv_horizontal)

        // method to activate the new activity StoryDetailActivity when clicked the single story
        val itemOnClick: (Int) -> Unit = { position ->
            rvV.adapter!!.notifyDataSetChanged()
            val realPosition = position+1 //add 1 because the count start from 0
            Toast.makeText(this,"$realPosition. item clicked.",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, StoryDetailActivity()::class.java)

            intent.putExtra("storyPosition", "$realPosition".toInt())
            intent.putExtra("storyName", storyList[position].title)
            startActivity(intent)
        }


        // assign layout and adapter to the vertical (background of KingdomActivity) recycleView
        rvV.layoutManager = LinearLayoutManager(this)
        val rvVAdapter = KingdomListAdapter(this, storyList, itemClickListener = itemOnClick)
        rvV.adapter = rvVAdapter

        // assign layout and adapter to the horizontal (header of KingdomActivity) recycleView
        rvH.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val rvHAdapter = KingdomListAdapter(this, storyHorizontalList, itemClickListener = itemOnClick)
        rvH.adapter = rvHAdapter


        // add the menu fragment to the bottom of KingdomActivity
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.menu_layout, MenuFragment())
        transaction.commit()
    }

}