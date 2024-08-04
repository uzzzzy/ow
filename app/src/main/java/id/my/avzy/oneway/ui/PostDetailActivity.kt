package id.my.avzy.oneway.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.my.avzy.oneway.api.ApiService
import id.my.avzy.oneway.api.PostDetailResponse
import id.my.avzy.oneway.R
import id.my.avzy.oneway.api.RetrofitClient
import id.my.avzy.oneway.model.PostDetail
import io.noties.markwon.Markwon
import io.noties.markwon.image.ImagesPlugin
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class PostDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post_detail)

        val slug = intent.getStringExtra("POST_SLUG") ?: return
        val apiService = RetrofitClient.createService<ApiService>()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        fetchPostDetail(apiService, slug)
    }

    private fun fetchPostDetail(apiService: ApiService, slug: String) {
        val call = apiService.getPostBySlug(slug)
        call.enqueue(object : retrofit2.Callback<PostDetailResponse> {
            override fun onResponse(call: retrofit2.Call<PostDetailResponse>, response: retrofit2.Response<PostDetailResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.result?.let { post ->
                        displayPostDetails(post)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<PostDetailResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun displayPostDetails(post: PostDetail) {
        findViewById<TextView>(R.id.textViewTitle).text = post.title.trim()
        findViewById<TextView>(R.id.textViewPublishedAt).text = formatDateString(post.publishedAt)
        findViewById<TextView>(R.id.textViewViews).text = getString(R.string.post_views, post.views)

        val content = post.content
        val markwon = Markwon.builder(this)
            .usePlugin(ImagesPlugin.create())
            .build()
        markwon.setMarkdown(findViewById(R.id.textViewContent), content)
    }

    private fun formatDateString(isoDate: String): String {
        val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val displayDateFormat = SimpleDateFormat("MMM d, yyyy 'at' h:mm a", Locale.getDefault())
        return try {
            val date = isoDateFormat.parse(isoDate)
            displayDateFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
            isoDate // Return the original string if parsing fails
        }
    }
}