package com.example.psychology.romm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.psychology.romm.entity.User
import com.example.psychology.romm.entity.VideoData

@Dao
interface VideoDataDao {
    @Query("SELECT * FROM videos")
    fun getAll(): List<VideoData>

    @Query("SELECT * FROM videos WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<VideoData>

    @Query("SELECT * FROM videos WHERE id =:userIds")
    fun loadByIds(userIds: Long): VideoData

    @Query("SELECT * FROM videos WHERE type IN (:type) AND video_type = :video_type AND custom = :custom")
    fun loadAllByType(video_type: String, type: Int, custom: Int): List<VideoData>

    @Query("SELECT * FROM videos WHERE  video_type = :video_type AND custom = :custom")
    fun loadAllCustom(video_type: String, custom: Int): List<VideoData>

    @Query("SELECT * FROM videos WHERE video_title LIKE :video_title AND maker LIKE :maker LIMIT 1")
    fun findByName(video_title: String, maker: String): VideoData

    @Query("SELECT * FROM videos WHERE video_title LIKE '%' || :video_title || '%' ")
    fun findBySearch(video_title: String): List<VideoData>

    @Insert
    fun insertAll(vararg videos: VideoData)

    @Delete
    fun delete(video: VideoData)

    @Update
    fun update(video: VideoData)


}