package com.example.imageapi

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imageapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var recyclerViews:RecyclerView

    private var imageRVAdapter: Adapter? = null

    //Передача url для Picasso
    val listImage = arrayListOf<ItemOfList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViews = binding.recyclerView

        //Делает вид ListView
        recyclerViews.layoutManager = LinearLayoutManager(this)
        //Не изменяет высоту или ширину у RecyclerView.
        //recyclerView.setHasFixedSize(true)
        //RecyclerView требует адаптер заполнения и управления элементами
        imageRVAdapter = Adapter(listImage)
        recyclerViews.adapter = imageRVAdapter
        //Вызываем метод для получения ссылки на изображение.
        parseJSON("https://aws.random.cat/meow")


    }

    private  val client = OkHttpClient()

    private fun parseJSON(url:String) {
        //Запускаем цикл для получкния 13 ссылок на картинки.
        for(index in 0..20) {
            val request = Request.Builder().url(url).build()
            client.newCall(request).enqueue(object : Callback {
                //Метод для вывода ошибки
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                //Метод для преоброзование из JSON в url
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call, response: Response) {
                    val json = response.body()?.string()
                    val jsonImage=(json?.let { JSONObject(it).get("file") }).toString()
                    this@MainActivity.runOnUiThread {
                        //Проверка успешности ответа
                        if (response.isSuccessful) {
                            //Проверка на непустой url
                            Toast.makeText(
                                this@MainActivity,
                                "Получение данных ${listImage.size} ",
                                Toast.LENGTH_LONG
                            ).show()
                            if (jsonImage != "") {
                                listImage.add(ItemOfList(jsonImage))
                                if (listImage.size == 20) {
                                    imageRVAdapter!!.notifyDataSetChanged()
                                }
                            }
                        }
                    }

                }
            })
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getImage(vararg arrayImageUrl:String) {
        for(i in 0..4){
            listImage.add(ItemOfList(arrayImageUrl[i]))
        }

        imageRVAdapter!!.notifyDataSetChanged()

    }
}