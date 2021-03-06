package it.wip.database.dao

import androidx.room.*
import it.wip.database.model.Story

@Dao
interface StoryDao {

    @Query("SELECT * FROM story")
    suspend fun getAll(): Array<Story>

    @Query("SELECT * FROM story WHERE user = :userId")
    fun getAllByUserWithoutCoroutines(userId: Int): Array<Story>

    @Query("SELECT * FROM story WHERE id = :storyId")
    fun getAllByIdStoryWithoutCoroutines(storyId: Int): Array<Story>

    @Insert
    suspend fun insert(vararg stories: Story)

    @Insert
    fun insertWithoutCoroutines(vararg stories: Story)

    @Update
    suspend fun update(story: Story)

    @Delete
    suspend fun delete(story: Story)

}