package com.test.deliveries.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.test.deliveries.database.tables.Delivery

@Dao
interface DeliveriesDao {

    @Query("SELECT * FROM deliveries_table")
    fun getAll(): List<Delivery>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg delivery: Delivery)

    @Update
    suspend fun update(delivery: Delivery)

    @Delete
    suspend fun delete(delivery: Delivery)

    @Query("DELETE FROM deliveries_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM deliveries_table WHERE id =:id")
    fun getDeliveryById(id: String): Delivery

}