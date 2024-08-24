package id.my.avzy.oneway.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
import id.my.avzy.oneway.api.ApiService
import id.my.avzy.oneway.service.ForegroundService
import id.my.avzy.oneway.api.ListPostResponse
import id.my.avzy.oneway.ui.adapter.PostAdapter
import id.my.avzy.oneway.R
import id.my.avzy.oneway.api.RetrofitClient
import id.my.avzy.oneway.model.PostSummary
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val apiService by lazy { RetrofitClient.createService<ApiService>() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        askPermission()

        val serviceIntent = Intent(this, ForegroundService::class.java)
        startForegroundService(serviceIntent)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getPosts()

        addUIListener()
    }

    private fun askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Implement this method to send token to your app server.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: Implement this method to send token to your app server.
            } else {
                // TODO: Implement this method to send token to your app server.
            }
        } else {
            // exit application
            val serviceIntent = Intent(this, ForegroundService::class.java)
            startForegroundService(serviceIntent)

            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // TODO: Implement this method to send token to your app server.
                Log.d("Token", "$token")
            }
        }
    }

    private fun addUIListener() {
        findViewById<Button>(R.id.buttonToDo).setOnClickListener {
            val intent = Intent(this, ToDoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getPosts() {
        val call = apiService.getPosts()

        call.enqueue(object : Callback<ListPostResponse> {
            override fun onResponse(call: Call<ListPostResponse>, response: Response<ListPostResponse>) = onResponse(response)
            override fun onFailure(call: Call<ListPostResponse>, t: Throwable) = onFailure(t)
        })
    }

    private fun onResponse(response: Response<ListPostResponse>) {
        response.body()?.result?.data?.let { posts ->
            setupRecyclerView(posts)
        }
    }

    private fun onFailure(t: Throwable) {
        Log.e("MainActivity", "onFailure: $t")
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
