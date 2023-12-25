package com.project.lab3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.lab3.databinding.ActivityMainBinding
import com.project.lab3.requests.Article
import com.project.lab3.requests.ArticlesInterface
import com.project.lab3.requests.RequestArticles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val apikey = "pub_35362f3f15fa2272e52bef3edc0e0f9461fd8"
    private var keywords = ""
    private var nextPage: String? = null

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private lateinit var articles: ArticlesInterface

    private var request = RequestArticles("", 0, listOf(), null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createRecyclerView()
        updateList()

        binding.btnSearch.setOnClickListener { searchNews() }
        binding.btnNextPage.setOnClickListener { toNextPage() }

        val retrofit = Retrofit.Builder().baseUrl("https://newsdata.io")
            .addConverterFactory(GsonConverterFactory.create()).build()
        articles = retrofit.create(ArticlesInterface::class.java)
    }

    private fun createRecyclerView() {
        adapter = MainAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.rvList.layoutManager = layoutManager
        binding.rvList.adapter = adapter
    }

    private fun searchNews() {
        val text = binding.etSearch.text.toString()
        keywords = text.replace("\\s+".toRegex(), " ").replace(" ", "&")
        CoroutineScope(Dispatchers.IO).launch{
            request = articles.getArticles(apikey, keywords)
            nextPage = request.nextPage
            runOnUiThread {
                updateList()
                binding.tvCountPages.text = request.totalResults.toString()
            }
        }
    }

    private fun toNextPage() {
        if (!nextPage.isNullOrBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                request = articles.getArticlesNewPage(apikey, keywords, nextPage.toString())
                nextPage = request.nextPage
                runOnUiThread {
                    updateList()
                    binding.tvCountPages.text = request.totalResults.toString()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateList() {
        adapter.items = listOf()
        adapter.notifyDataSetChanged()
        adapter.items = request.results
        adapter.notifyDataSetChanged()
    }
}