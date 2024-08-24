package id.my.avzy.oneway.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
import id.my.avzy.oneway.BuildConfig.HOME_SERVER_URL
import id.my.avzy.oneway.api.ApiService
import id.my.avzy.oneway.service.ForegroundService
import id.my.avzy.oneway.api.ListPostResponse
import id.my.avzy.oneway.ui.adapter.PostAdapter
import id.my.avzy.oneway.R
import id.my.avzy.oneway.api.FcmApi
import id.my.avzy.oneway.api.RetrofitClient
import id.my.avzy.oneway.api.SendTokenRequest
import id.my.avzy.oneway.model.PostSummary
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val apiService by lazy { RetrofitClient.createService<ApiService>() }
    private val fcmService by lazy { RetrofitClient.createService<FcmApi>(HOME_SERVER_URL) }


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
                postNotifications()
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                Toast.makeText(this, "Please grant permission to send notifications", Toast.LENGTH_LONG).show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        } else {
            postNotifications()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                postNotifications()
            } else {
                Toast.makeText(this, "Please grant permission to send notifications", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun postNotifications() {
        // exit application
        val serviceIntent = Intent(this, ForegroundService::class.java)
        startForegroundService(serviceIntent)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.d("Token", token)
            val call = fcmService.sendToken(SendTokenRequest(token))
            call.enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    Log.d("Response", response.toString())
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Log.d("Failure", t.toString())
                }
            })
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
