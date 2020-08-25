package com.example.lesson

import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.BaseView
import com.example.core.utils.CacheUtils
import com.example.lesson.entity.Lesson
import kotlinx.android.synthetic.main.activity_lesson.*
import kotlin.reflect.KProperty

class LessonActivity : AppCompatActivity(), BaseView<LessonPresenter?>, Toolbar.OnMenuItemClickListener {
    override val presenter by lazy {
        LessonPresenter(this)
    }

    var token1: String by Saver("token1")
    var token2: String by Saver("token2")

    class Saver(val key: String) {
        operator fun getValue(lessonActivity: LessonActivity, property: KProperty<*>): String {
            return CacheUtils[key]!!
        }

        operator fun setValue(lessonActivity: LessonActivity, property: KProperty<*>, value: String) {
            CacheUtils[key] = value
        }
    }

    private val lessonAdapter = LessonAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu_lesson)
        toolbar.setOnMenuItemClickListener(this)
        list.run {
            layoutManager = LinearLayoutManager(this@LessonActivity)
            adapter = lessonAdapter
            addItemDecoration(DividerItemDecoration(this@LessonActivity, LinearLayout.VERTICAL))
        }

        swipe_refresh_layout.run {
            setOnRefreshListener { presenter.fetchData() }
            isRefreshing = true
        }

        token1 = "1"
        token2 = "2"

        presenter.fetchData()
    }

    fun showResult(lessons: List<Lesson>) {
        lessonAdapter.updateAndNotify(lessons)
        swipe_refresh_layout.isRefreshing = false
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        presenter.showPlayback()
        return false
    }
}