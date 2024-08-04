package id.my.avzy.oneway.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.my.avzy.oneway.R
import id.my.avzy.oneway.model.PostSummary
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class PostAdapter(private val posts: List<PostSummary>, private val onItemClick: (PostSummary) -> Unit) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewPublishedAt: TextView = itemView.findViewById(R.id.textViewPublishedAt)

        private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        private val displayDateFormat = SimpleDateFormat("MMM d, yyyy 'at' h:mm a", Locale.getDefault())

        init {
            itemView.setOnClickListener {
                val post = posts[adapterPosition]
                onItemClick(post)
            }
        }

        fun bind(post: PostSummary) {
            textViewTitle.text = post.title.trim()
            textViewPublishedAt.text = formatDateString(post.publishedAt)
        }

        private fun formatDateString(isoDate: String): String {
            return try {
                val date = isoDateFormat.parse(isoDate)
                displayDateFormat.format(date!!)
            } catch (e: ParseException) {
                e.printStackTrace()
                isoDate // Return the original string if parsing fails
            }
        }
    }
}
