package id.my.avzy.oneway

import RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.my.avzy.oneway.dto.PostSummary
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getPosts()
    }

    private fun getPosts() {
        val apiService = RetrofitClient.getClient(baseUrl).create(ApiService::class.java)
        val call = apiService.getPosts()

        call.enqueue(object : Callback<ListPostResponse> {
            override fun onResponse(call: Call<ListPostResponse>, response: Response<ListPostResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    val posts = data?.result?.data

                    if (posts != null) {
                        setupRecyclerView(posts)
                    } else {
                        Log.d("success", "${data?.message}")
                    }
                    Log.d("success", "${data?.message}")
                    Log.d("total", data?.result?.meta?.total.toString())
                    data?.result?.data?.forEachIndexed { index, post ->
                        Log.d("post summary", "post $index: ${post.title}")
                    }
                }
            }

            override fun onFailure(call: Call<ListPostResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setupRecyclerView(posts: List<PostSummary>) {
        val recyclerViewPosts = findViewById<RecyclerView>(R.id.recyclerViewPosts)
        recyclerViewPosts.layoutManager = LinearLayoutManager(this)
        recyclerViewPosts.adapter = PostAdapter(posts) { post ->
            val intent = Intent(this, PostDetailActivity::class.java).apply {
                putExtra("POST_SLUG", post.slug)
            }
            startActivity(intent)
        }
    }
}