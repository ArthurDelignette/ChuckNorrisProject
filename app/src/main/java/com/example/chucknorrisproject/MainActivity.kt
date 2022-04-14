package com.example.chucknorrisproject

import CustomAdapter
import Joke
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    val compositeDisposable = CompositeDisposable()
    private val tag = "Main"
    val adapter = CustomAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel

        recyclerview.adapter = adapter

        // This loop will create 20 Views containing


        val Singledejoke = JokeApiServiceFactory.createJAS().giveMeAJoke()
        compositeDisposable.add(Singledejoke
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = {
                    Log.e(tag, "Je n'arrive pas a lire le Single", it)
                },
                onSuccess = {
                    Log.i(tag, it.value)
                    adapter.updateList(it)
                }
            ))


        // This will pass the ArrayList to our Adapter


        // Setting the Adapter with the recyclerview

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(tag, "Va etre détruit:${compositeDisposable.size()}")
        compositeDisposable.clear()
        Log.i(tag, "Il en reste :${compositeDisposable.size()}")

    }
}