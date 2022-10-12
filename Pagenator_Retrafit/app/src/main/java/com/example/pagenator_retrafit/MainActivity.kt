package com.example.pagenator_retrafit

import android.annotation.SuppressLint
import android.os.Bundle;
import android.util.Log
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.pagenator_retrafit.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import java.util.ArrayList;


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var layoutManager: LinearLayoutManager

    var isLoading = false

    // creating a variable for our array list, adapter class,
    // recycler view, progressbar, nested scroll view
    private var userModalArrayList: ArrayList<UserModal>? = null
    private var userRVAdapter: UserRVAdapter? = null
    private var userRV: RecyclerView? = null
    private var loadingPB: ProgressBar? = null
    //private var nestedSV: NestedScrollView? = null

    // creating a variable for our page and limit as 2
    // as our api is having highest limit as 2 so
    // we are setting a limit = 2
    var page = 0
    var limit = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userModalArrayList = ArrayList()

        userRV = findViewById(R.id.idRVUsers)
        loadingPB = findViewById(R.id.idPBLoading)

        layoutManager = LinearLayoutManager(this@MainActivity)
        userRV!!.layoutManager = layoutManager
        userRVAdapter = UserRVAdapter(userModalArrayList!!, this@MainActivity)
        userRV!!.adapter = userRVAdapter

        getDataFromAPI(page, limit)


        userRV!!.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = layoutManager.itemCount

                if ((visibleItemCount + pastVisibleItem) >= total && !isLoading) {
                    page++
                    isLoading = true
                    getDataFromAPI(page, limit)
                }
            }
        })

        // adding on scroll change listener method for our nested scroll view.
        /*nestedSV!!.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                page++
                loadingPB!!.visibility = View.VISIBLE
                getDataFromAPI(page, limit)
            }
        })*/
    }

    private var myJob: Job? = null
    override fun onDestroy() {
        myJob?.cancel()
        super.onDestroy()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDataFromAPI(pages: Int, limit: Int) {
        loadingPB!!.visibility = View.VISIBLE
        if (pages > limit) {
            page=0
        }

        val api= Common.retrofitServices

        val call=api.getPage(page)

        //CoroutineScope(Dispatchers.Main).launch {

            call.enqueue(object : Callback<MainPage> {
                override fun onFailure(call: Call<MainPage>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
                    Log.d("TAG", t.message.toString())
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<MainPage>,
                    response: retrofit2.Response<MainPage>
                ) {
                    if (response.isSuccessful) {
                        val resp = response.body()!!.data
                        resp.forEach { children ->
                            userModalArrayList!!.add(
                                UserModal(
                                    children.first_name,
                                    children.last_name,
                                    children.email,
                                    children.avatar
                                )
                            )
                        }

                        userRVAdapter!!.notifyDataSetChanged()
                        loadingPB!!.visibility = View.GONE
                        isLoading = false

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            response.code().toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        //}
    }
}