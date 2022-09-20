package com.example.imageapi_retrafit

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imageapi_retrafit.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var recyclerViews: RecyclerView

    private var imageRVAdapter: Adapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViews = binding.recyclerView

        //Делает вид ListView
        recyclerViews.layoutManager = LinearLayoutManager(this)
        //Не изменяет высоту или ширину у RecyclerView.
        recyclerViews.setHasFixedSize(true)
        //RecyclerView требует адаптер заполнения и управления элементами
        imageRVAdapter = Adapter(listImage)
        recyclerViews.adapter = imageRVAdapter
        //Вызываем метод для получения ссылки на изображение.

        CoroutineScope(Dispatchers.IO).launch {
            parseJSON()
        }
    }

    //Передача url для Picasso
    val listImage = arrayListOf<ItemOfList>()
    var indexInt=0;

    private fun parseJSON() {
        //Запускаем цикл для получкния 13 ссылок на картинки.
        for(index in 0..20) {
            val api= Common.retrofitServices
            val call=api.getPage()
            call.enqueue(object : Callback<MainPage> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<MainPage>, response: Response<MainPage>) {

                        if (response.isSuccessful) {
                            val jsonImage = response.body()!!.file
                            Toast.makeText(this@MainActivity,listImage.size.toString(),Toast.LENGTH_LONG).show()
                            if (jsonImage != "") {
                                listImage.add(ItemOfList(jsonImage))
                                if (listImage.size == 20) {
                                    imageRVAdapter!!.notifyDataSetChanged()
                                }
                            }

                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                response.code().toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                }

                override fun onFailure(call: Call<MainPage>, t: Throwable) {
                    Toast.makeText(this@MainActivity,t.message.toString(),Toast.LENGTH_LONG).show()
                }
            })
        }

    }
}