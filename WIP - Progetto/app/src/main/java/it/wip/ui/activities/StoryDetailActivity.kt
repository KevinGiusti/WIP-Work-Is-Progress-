package it.wip.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import it.wip.R
import it.wip.data.DataKingdom
import it.wip.databinding.ActivityStoryDetailBinding
import it.wip.ui.fragments.MenuFragment
import it.wip.utils.KingdomListAdapter
import it.wip.viewModel.StoryDetailViewModel

class StoryDetailActivity: AppCompatActivity() {

    lateinit var viewModel: StoryDetailViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        //                              LEFTHAND MODE DETECTION
        val lefthandPreference = applicationContext.getSharedPreferences("lefthandPreference", Context.MODE_PRIVATE)
        val lefthand = lefthandPreference.getInt("lefthand", Context.MODE_PRIVATE)
        if(lefthand==1)  setTheme(R.style.RightToLefTheme) else setTheme(R.style.LeftToRighTheme)

        super.onCreate(savedInstanceState)

        val binding: ActivityStoryDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_story_detail)
        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application))[StoryDetailViewModel::class.java]

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //create local variables by taking a reference to the Views with binding
        val storyDetailTitle = binding.storyDetailTitle
        val rv = binding.recyclerVerticalDetails
        val backButton = binding.backButtonStoryDetail


        //get the story name related with the story clicked from the Intent's Extra in KingdomActivity
        val storyTitle = intent.getStringExtra("storyName")
        //set the story detail name related with the story clicked
        storyDetailTitle.text = storyTitle


        //get the story ID related with the story clicked in the Kingdom
        val storyID = intent.getIntExtra("storyID", 0)


        //create the list of Chapters
        val storyDetailList = ArrayList<DataKingdom>()


        val chapters = viewModel.getChapters(storyID)

        //iterate over all chapters inside our DB
        for (i in 0..chapters.lastIndex) {
            //check if id's story, given by the previous intent "storyID", is the same as chapters
            if (storyID == chapters[i].story) {
                //put Cards with data to the left
                if (i % 2 == 0) {

                    storyDetailList.add(
                        DataKingdom(
                            KingdomListAdapter.THE_THIRD_VIEW,
                            chapters[i].chapterName,
                            chapters[i].id,
                            chapters.size.toString(),
                            chapters[i].createdOn,
                            chapters[i].time
                        )
                    )
                    //numChapters++

                //put Cards with data to the right
                } else {

                    storyDetailList.add(
                        DataKingdom(
                            KingdomListAdapter.THE_FORTH_VIEW,
                            chapters[i].chapterName,
                            chapters[i].id,
                            chapters.size.toString(),
                            chapters[i].createdOn,
                            chapters[i].time
                        )
                    )
                    //numChapters++
                }

            }
            //numTotChapters.add(numChapters)
        }



        // method for activate the new activity ChapterInfoActivity when clicked the single story
        val itemOnClick: (Int) -> Unit = { position ->
            val intent = Intent(this, ChapterInfoActivity()::class.java)
            intent.putExtra("storyName", storyTitle)
            intent.putExtra("storyID", storyID)
            intent.putExtra("chapterID", storyDetailList[position].itemID)
            startActivity(intent)
        }

        // assign layout and adapter to the vertical (background of StoryDetailActivity) recycleView
        rv.layoutManager = LinearLayoutManager(this)
        val rvAdapter = KingdomListAdapter(this, storyDetailList, itemClickListener = itemOnClick)
        rv.adapter = rvAdapter



        //                          MENU-BAR MANAGER
        // add menu fragment on StoryDetailActivity's bottom to ensure graphical consistency
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.menu_layout, MenuFragment())
        transaction.commit()


        //                          BACKAWARDS TO KingdomActivity
        backButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("storyID", storyID)

            val intent = Intent(this, KingdomActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        backButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> backButton.setImageResource(R.drawable.back_arrow_pressed)
                MotionEvent.ACTION_UP -> backButton.setImageResource(R.drawable.back_arrow)
            }
            v?.onTouchEvent(event) ?: true
        }

    }

}