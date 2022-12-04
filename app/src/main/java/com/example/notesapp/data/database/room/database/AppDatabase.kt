package com.example.notesapp.data.database.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.data.database.room.dao.NotesDao
import com.example.notesapp.data.database.room.entities.NoteDB
// это база данных
@Database(entities = [NoteDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNotesDao(): NotesDao

    companion object {
        //это синглтон, т.е. живет в единственном экземпляре

        @Volatile private var INSTANCE: AppDatabase? = null
        // волатайл говорит что во всех потоках будет одно значение
        // т.к. бывает что в разные потоки попадают разные значения одной переменной

        fun getDatabase(context: Context) : AppDatabase {
            return INSTANCE?: synchronized(this) {
        // до ?: - возвращаем инстанс, после - если инстанс = налл
                // синхронайзд-блок делается только в одном потоке
                val instance = Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "notes-database").allowMainThreadQueries().build()
                INSTANCE = instance

                instance
            }
        }
    }
}