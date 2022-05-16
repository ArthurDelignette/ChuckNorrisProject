package com.example.chucknorrisproject

import CustomAdapter
import Joke
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chucknorrisproject.R.id.buttonAddJoke
import com.example.chucknorrisproject.R.id.progressBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    val compositeDisposable = CompositeDisposable()
    private val tag = "Main"
    val adapter = CustomAdapter()

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel

        recyclerview.adapter = adapter

        // This loop will create 20 Views containing

        val button = findViewById<Button>(buttonAddJoke)
        val progressBar = findViewById<ProgressBar>(progressBar)
        button.setOnClickListener(View.OnClickListener {
            val Singledejoke = JokeApiServiceFactory.createJAS().giveMeAJoke()
            progressBar.visibility = View.VISIBLE
            repeat(10){
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
            }
            progressBar.visibility = View.INVISIBLE
        })




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