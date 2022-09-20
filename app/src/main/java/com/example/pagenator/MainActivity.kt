package com.example.pagenator

import android.annotation.SuppressLint
import android.os.Bundle;
import android.os.Handler
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pagenator.databinding.ActivityMainBinding

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var layoutManager: LinearLayoutManager

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

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = userRVAdapter!!.itemCount

                if((visibleItemCount + pastVisibleItem) >= total){
                    page++
                    loadingPB!!.visibility = View.VISIBLE
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

    @SuppressLint("NotifyDataSetChanged")
    private fun getDataFromAPI(pages: Int, limit: Int) {

        if (pages > limit) {
            // checking if the page number is greater than limit.
            // displaying toast message in this case when page>limit.
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show()

            // hiding our progress bar.
            loadingPB!!.visibility = View.GONE
            return
        }

        // creating a string variable for url .
        val url = "https://reqres.in/api/users?page=$page"

        // creating a new variable for our request queue
        val queue: RequestQueue = Volley.newRequestQueue(this@MainActivity)

        // creating a variable for our json object request and passing our url to it.


            val jsonObjectRequest =
                JsonObjectRequest(Request.Method.GET, url, null,
                    { response ->
                        try {

                            // on below line we are extracting data from our json array.
                            val dataArray = response!!.getJSONArray("data")

                            // passing data from our json array in our array list.
                            for (i in 0 until dataArray.length()) {
                                val jsonObject = dataArray.getJSONObject(i)

                                // on below line we are extracting data from our json object.
                                userModalArrayList!!.add(
                                    UserModal(
                                        jsonObject.getString("first_name"),
                                        jsonObject.getString("last_name"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("avatar")
                                    )
                                )

                                /* // passing array list to our adapter class.
                                 userRVAdapter = UserRVAdapter(userModalArrayList!!, this@MainActivity)

                                 // setting adapter to our recycler view.
                                 userRV!!.adapter = userRVAdapter*/
                            }
                            userRVAdapter!!.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }) {
                    Toast.makeText(this@MainActivity, "Fail to get data..", Toast.LENGTH_SHORT)
                        .show()
                }
            // calling a request queue method
            // and passing our json object
            queue.add(jsonObjectRequest)

    }
}