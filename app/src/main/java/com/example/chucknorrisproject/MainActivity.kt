package com.example.chucknorrisproject

import CustomAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // this creates a vertical layout Manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel

        recyclerView.adapter = adapter


        val progressBar = findViewById<ProgressBar>(progressBar)

        progressBar.visibility = View.VISIBLE
        repeat(15) {
            getJokes()
        }


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    getJokes()
                }
            }
        })
        progressBar.visibility = View.INVISIBLE

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(tag, "Va etre détruit:${compositeDisposable.size()}")
        compositeDisposable.clear()
        Log.i(tag, "Il en reste :${compositeDisposable.size()}")

    }

    public fun getJokes(){
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
    }
}