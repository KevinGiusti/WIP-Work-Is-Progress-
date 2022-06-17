package it.wip.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import it.wip.database.WIPDatabase
import it.wip.database.dao.ChapterDao
import kotlinx.coroutines.launch

class StoryDetailViewModel(application: Application) : AndroidViewModel(application) {

    var chapterDao: ChapterDao = WIPDatabase.getInstance(application.applicationContext).chapterDao()

    val chaptersName = mutableListOf<String>()
    val storiesCorrelated = mutableListOf<Int>()

    init {

        val userIdPreference = application.applicationContext.getSharedPreferences("userId", Context.MODE_PRIVATE)
        val userId = userIdPreference.getInt("userId", Context.MODE_PRIVATE)

        val chapter = chapterDao.getAllByUserWithoutCoroutines(userId)
        viewModelScope.launch {

            for(singleChapter in chapter) {
                chaptersName.add(singleChapter.chapterName)
                storiesCorrelated.add(singleChapter.story) // story returns the story's id correlated to the chapter
            }
        }

    }

}