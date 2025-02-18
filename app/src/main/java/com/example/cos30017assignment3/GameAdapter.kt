package com.example.cos30017assignment3

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class GameAdapter(private val gameViewModel: GameViewModel) : ListAdapter<Game, GameAdapter.ViewHolder>(GamesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v){
        private val vTitle: TextView = v.findViewById(R.id.GameTitle)
        private val vRating: RatingBar = v.findViewById(R.id.ratingBar)
        val vCompleted: ImageView = v.findViewById(R.id.GameCompleted)
        val vDelete: ImageView = v.findViewById(R.id.deleteButton)
        private val vRelease: TextView = v.findViewById(R.id.GameRelease)
        private val vSummary: TextView = v.findViewById(R.id.GameSummary)
        private val vBoxArt: ImageView = v.findViewById(R.id.GameBoxArt)

        @SuppressLint("SimpleDateFormat")
        fun bind(it: Game){
            it.apply {
                vTitle.text = it.name
                vRating.rating = it.rating
                val date = Date(it.first_release_date * 1000)
                val formattedDate = SimpleDateFormat("dd/MM/yyyy").format(date)
                vRelease.text = formattedDate.toString()
                val summary = it.summary
                if (summary != null) {
                    if (summary.contains("\n")){
                        val newString = summary.substring(0, summary.indexOf('\n'))
                        vSummary.text = newString
                    } else if (summary.length > 200) {
                        vSummary.text = summary.substring(0, 200) + "..."
                    } else {
                        vSummary.text = summary
                    }
                }

                val imgUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/${it.image_id}.jpg"
                Picasso.get()
                    .load(imgUrl)
                    .into(vBoxArt)

                vCompleted.apply {
                    if (it.complete){
                        setImageResource(R.drawable.icon_check)
                    } else {
                        setImageResource(R.drawable.icon_x)
                    }
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return ViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)

        holder.vCompleted.setOnClickListener {
            gameViewModel.changeComplete(getItem(position))
            holder.bind(current)
        }

        holder.vDelete.setOnClickListener {
            gameViewModel.delete(getItem(position))
            holder.bind(current)
        }

    }

    class GamesComparator : DiffUtil.ItemCallback<Game>() {

        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.name == newItem.name
        }

    }

}
