package com.example.taleteller

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taleteller.R
import com.example.taleteller.model.User


class DetailsActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val user: User = intent.getParcelableExtra(EXTRA_USER_MODEL)
        supportActionBar?.title = user.title

        imageView = findViewById(R.id.image_view)
        textView = findViewById(R.id.text_view)

        imageView.setImageResource(user?.resId)
        textView.setText(user?.content)

    }

    companion object {
        var TAG = DetailsActivity::class.java.simpleName
        const val EXTRA_USER_MODEL: String = "user"

        fun newIntent(context: Context, user: User): Intent {
            var intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(EXTRA_USER_MODEL, user)
            return intent
        }
    }
}
