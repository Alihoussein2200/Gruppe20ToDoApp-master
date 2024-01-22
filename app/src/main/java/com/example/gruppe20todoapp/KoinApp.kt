package com.example.gruppe20todoapp

import android.app.Application
import androidx.room.Room
import com.example.gruppe20todoapp.database.Database
import com.example.gruppe20todoapp.reps.TodoRepo
import com.example.gruppe20todoapp.reps.TodoRepoImpl
import com.example.gruppe20todoapp.utility.RetrofitInstance
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module

class KoinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(module {
                single {
                    Room
                        .databaseBuilder(this@KoinApp, Database::class.java, "db")
                        .build()
                }
                single {
                    TodoRepoImpl(database = get())
                } bind TodoRepo::class

                // Provide Retrofit and GiphyApiService
                single { RetrofitInstance.giphyApiService }
            })
        }
    }
}
