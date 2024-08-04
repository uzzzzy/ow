package id.my.avzy.oneway

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.my.avzy.oneway.dto.PostDetail
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class PostDetailActivity : AppCompatActivity() {

    private val baseUrl = BuildConfig.BASE_URL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post_detail)

        val slug = intent.getStringExtra("POST_SLUG") ?: return
        val apiService = RetrofitClient.getClient(baseUrl).create(ApiService::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
        findViewById<TextView>(R.id.textViewTitle).text = post.title
        findViewById<TextView>(R.id.textViewContent).text = post.content
        findViewById<TextView>(R.id.textViewPublishedAt).text = formatDateString(post.publishedAt)
        findViewById<TextView>(R.id.textViewViews).text = "Views: %d".format(post.views)
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