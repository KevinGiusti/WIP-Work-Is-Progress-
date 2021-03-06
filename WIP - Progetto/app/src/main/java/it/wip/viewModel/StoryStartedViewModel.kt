package it.wip.viewModel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import it.wip.database.WIPDatabase
import it.wip.database.dao.StoryDao
import it.wip.database.model.Chapter
import it.wip.database.model.Story
import it.wip.utils.ScreenOffDetector
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class StoryStartedViewModel(application: Application) : AndroidViewModel(application){

    private val shoppedImgs = mutableListOf<String>()
    private val imgsInShop = mutableListOf<String>()
    val backgroundShoppedElements = mutableListOf<String>()
    val db: WIPDatabase
    val userId: Int

    private val storyDao: StoryDao
    private val userStories: Array<Story>

    lateinit var chapter: Array<Chapter>


    //              GUARDS
    /*
    * in riferimento al listener "setOnChronometerTickListener", che esegue il contenuto del
    * blocco {} ogni volta che trascorre un secondo, le variabili first, second, third e
    * fourthAlreadyExecuted garantiscono che la funzione "backgroundEvolution" venga eseguita
    * una sola volta per slot temporale
    * */
    var firstAlreadyExecuted = true
    var secondAlreadyExecuted = true
    var thirdAlreadyExecuted = true
    var fourthAlreadyExecuted = true

    // screenBlock works as a flag to detect if device's screen is block or not
    var screenBlock: Boolean = false
    // storyEnder works as a flag to detect if the user is go out his story in hardcore mode
    var storyEnder: Boolean = false

    val pauseIntentFilter = IntentFilter(Intent.ACTION_SCREEN_OFF)
    val screenOffDetector = ScreenOffDetector(this)

    init {

        val userIdPreference = application.applicationContext.getSharedPreferences("userId", Context.MODE_PRIVATE)
        val userId = userIdPreference.getInt("userId", Context.MODE_PRIVATE)
        val wipDb = WIPDatabase.getInstance(application.applicationContext)
        db = wipDb
        this.userId = userId

        storyDao = wipDb.storyDao()
        userStories = storyDao.getAllByUserWithoutCoroutines(userId)

        viewModelScope.launch {

            runBlocking {

                chapter = db.chapterDao().getAll()

                val currentShoppedElements = wipDb.shoppedDao().getAllByUser(userId)
                val allElements = wipDb.shopElementDao().getAll()

                for(i in currentShoppedElements){
                    shoppedImgs.add(i.shopElement)
                }
                for (i in allElements){
                    if(i.type == "background"){
                        imgsInShop.add(i.elementName)
                    }
                }

                for(i in shoppedImgs){
                    for(j in imgsInShop){
                        if(i==j){
                            backgroundShoppedElements.add(i)
                        }
                    }
                }
            }
        }
    }




    fun coinCalculator(studyTime: Long, breakTime: Long, actualTime: Long): String{
        var meritCoefficient = studyTime/breakTime
        if(meritCoefficient<1){
            meritCoefficient = 1
        }
        val studyPlusPause = studyTime+breakTime

        val earnedCoins: Int =
            if(actualTime>studyPlusPause){
                (meritCoefficient*(actualTime/studyPlusPause)).toInt()
            } else{
                (meritCoefficient*(actualTime/studyTime)).toInt()
            }

        viewModelScope.launch {
            val user = db.userDao().getUserById(userId)
            user.coins = user.coins+earnedCoins
            db.userDao().update(user)
        }

        return earnedCoins.toString()
    }



    //FUNCTION TO ADD A NEW STORY
    suspend fun addNewStory(newStoryName: String, myTime: String, studyTime: Int,
                            breakTime: Int, selectedMode: Int, selectedAvatar: String) {

        //take all stories to add the new one at the end of the DB
        val allStories = storyDao.getAll()
        val lastStoryIndex = allStories.lastIndex

        val lastChapterIndex = chapter.lastIndex

        val dateFormat = SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.ITALY)

        viewModelScope.launch {
            //take all user's storyName and put them in a Map with its IDs
            val userStoryNamesId = mutableMapOf<String, Int>()
            for (userStory in userStories) {
                userStoryNamesId[userStory.storyName] = userStory.id
            }

            //if the story is already in the DB, then add another chapter to that story
            if (userStoryNamesId.containsKey(newStoryName)) {
                val userChapters = db.chapterDao().getAllByStory(userStoryNamesId[newStoryName]!!)

                //add a Chapter to the story with storyName property
                db.chapterDao().insert(
                    Chapter(
                        chapter[lastChapterIndex].id + 1, "Chapter ${userChapters.size + 1}",
                        myTime, dateFormat.format(Date()).toString(),
                        studyTime, breakTime, selectedMode,
                        selectedAvatar, userStoryNamesId[newStoryName]!!
                    )
                )
            }
            //in case there isn't another story with the same name we want to create,
            // add the new one (with a chapter inside) into the DB
            else {
                val newStory = Story(
                    allStories[lastStoryIndex].id + 1, newStoryName,
                    dateFormat.format(Date()).toString(), userId
                )
                storyDao.insert(newStory) //add a new Story to the Kingdom

                //insert a first Chapter to the new story
                db.chapterDao().insert(
                    Chapter(
                        chapter[lastChapterIndex].id + 1, "Chapter 1",
                        myTime, dateFormat.format(Date()).toString(),
                        studyTime, breakTime, selectedMode,
                        selectedAvatar, newStory.id
                    )
                )
            }
        }//viewModelScope

    }


}