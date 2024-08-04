package id.my.avzy.oneway

import RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.my.avzy.oneway.data.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var baseUrl = BuildConfig.BASE_URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val serviceIntent = Intent(this, ForegroundService::class.java)
        startForegroundService(serviceIntent)

        Log.d("main",baseUrl)

        val apiService = RetrofitClient.getClient(baseUrl).create(ApiService::class.java)
        val call = apiService.getPosts()

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    data?.message?.let { Log.d("success", it) }
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}