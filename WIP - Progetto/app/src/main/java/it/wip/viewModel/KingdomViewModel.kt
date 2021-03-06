package it.wip.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import it.wip.database.WIPDatabase
import it.wip.database.dao.ChapterDao
import it.wip.database.dao.StoryDao
import it.wip.database.model.Chapter
import it.wip.database.model.Story
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class KingdomViewModel(application: Application) : AndroidViewModel(application) {

    private var storyDao: StoryDao = WIPDatabase.getInstance(application.applicationContext).storyDao()
    var chapterDao: ChapterDao = WIPDatabase.getInstance(application.applicationContext).chapterDao()

    // list of Story sorted to be sorted by date of creation
    var sortedList = listOf<Story>()

    init {
        //get user from SharedPreferences
        val userIdPreference = application.applicationContext.getSharedPreferences("userId", Context.MODE_PRIVATE)
        val userId = userIdPreference.getInt("userId", Context.MODE_PRIVATE)
        //get all Story by userId detection
        val story = storyDao.getAllByUserWithoutCoroutines(userId)

        viewModelScope.launch {
                val format: DateFormat = SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.ITALY)
                sortedList = story.sortedByDescending { format.parse(it.createdOn) }
        }

    }

    fun getChapters(storyId: Int): Array<Chapter> {
        return chapterDao.getAllByStory(storyId)
    }

}