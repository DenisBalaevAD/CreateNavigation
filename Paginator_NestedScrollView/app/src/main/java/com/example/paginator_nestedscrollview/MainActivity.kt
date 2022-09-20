package com.example.paginator_nestedscrollview

import android.annotation.SuppressLint
import android.os.Bundle;
import android.util.Log
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.paginator_nestedscrollview.databinding.ActivityMainBinding
import org.json.JSONException
import java.util.ArrayList;
import com.android.volley.VolleyError

import org.json.JSONObject

import org.json.JSONArray





class MainActivity : AppCompatActivity() {

    // creating a variable for our array list, adapter class,
    // recycler view, progressbar, nested scroll view
    private var userModalArrayList: ArrayList<UserModal>? = null
    private var userRVAdapter: UserRVAdapter? = null
    private var userRV: RecyclerView? = null
    private var loadingPB: ProgressBar? = null
    private var nestedSV: NestedScrollView? = null

    // creating a variable for our page and limit as 2
    // as our api is having highest limit as 2 so
    // we are setting a limit = 2
    var page = 0
    var limit=2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userModalArrayList = ArrayList()

        userRV = findViewById(R.id.idRVUsers)
        loadingPB = findViewById(R.id.idPBLoading)
        nestedSV = findViewById(R.id.idNestedSV)

        getDataFromAPI(page, limit)

        nestedSV!!.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                page++
                loadingPB!!.visibility = View.VISIBLE
                getDataFromAPI(page, limit)
            }
        })
    }

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

                            // passing array list to our adapter class.
                            userRVAdapter = UserRVAdapter(userModalArrayList!!, this@MainActivity)

                            // setting layout manager to our recycler view.
                            userRV!!.layoutManager = LinearLayoutManager(this@MainActivity)

                            // setting adapter to our recycler view.
                            userRV!!.adapter = userRVAdapter
                        }
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