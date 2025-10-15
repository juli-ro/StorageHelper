package com.example.stashstuff.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.stashstuff.models.Container
import com.example.stashstuff.models.Place
import com.example.stashstuff.models.StorageItem
import com.example.stashstuff.models.SubContainer
import com.example.stashstuff.models.SubContainerWithStorageItems

@Database(
    entities = [StorageItem::class, SubContainer::class, Container::class, Place::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun StorageItemDao(): StorageItemDao
    abstract fun SubContainerDao(): SubContainerDao
    abstract fun ContainerDao(): ContainerDao
    abstract fun PlaceDao(): PlaceDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "storage_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }

        }


    }

}

val MIGRATION_2_3 = object : Migration(2,3){
    override fun migrate(db: SupportSQLiteDatabase) {
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS subContainer (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                subContainerName TEXT
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            ALTER TABLE storageItem ADD COLUMN subContainerId INTEGER
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE storageItem_new(
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                itemName TEXT,
                itemAmount INTEGER,
                subContainerId INTEGER,
                FOREIGN KEY(subContainerId) REFERENCES subContainer(id) ON DELETE CASCADE
            )
            """.trimIndent()
        )

        db.execSQL(
            """
                INSERT INTO storageItem_new(id, itemName, itemAmount, subContainerId)
                SELECT id, itemName, itemAmount, subContainerId FROM storageItem
            """.trimIndent()
        )

        db.execSQL("DROP TABLE storageItem")

        db.execSQL("ALTER TABLE storageItem_new RENAME TO storageItem")
    }
}
