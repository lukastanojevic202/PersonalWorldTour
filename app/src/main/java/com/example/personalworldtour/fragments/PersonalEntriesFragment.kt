package com.example.personalworldtour.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.personalworldtour.R
import com.example.personalworldtour.sql_lite.ArticleData
import com.example.personalworldtour.sql_lite.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class PersonalEntriesFragment() : Fragment() {
    val LIST_VIEW = "LIST_VIEW"
    val STAGGERD_VIEW = "STAGGERED_VIEW"
    var currentVisibleView: String = LIST_VIEW
    lateinit var rv: RecyclerView
    lateinit var tv: TextView
    var userID = 0
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userID = arguments?.getInt("userID")!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_personal_entries, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = view.findViewById(R.id.rvPersonalItemsList)
        tv = view.findViewById(R.id.tvNoRecordsAvailable)

        val fbAdd : FloatingActionButton = view.findViewById(R.id.fbAdd)

        setupListOfDataIntoRecyclerView()
//      tablet variant has switch button
//        ifTablet(view)


        fbAdd.setOnClickListener {
            addArticle()
        }

    }

    fun setupListOfDataIntoRecyclerView() {
        currentVisibleView = LIST_VIEW
        if (getItemsList().size > 0) {
            Toast.makeText(requireContext(),"userID je $userID",Toast.LENGTH_LONG).show()
            rv.visibility = View.VISIBLE
            tv.visibility = View.GONE
            rv.layoutManager = LinearLayoutManager(requireContext())
            val rvItemAdapter = RvItemAdapter(requireActivity(), getItemsList())
            rv.adapter = rvItemAdapter
        } else {
            rv.visibility = View.GONE
            tv.visibility = View.VISIBLE
        }
    }

    fun setupStaggeredDataIntoRecyclerView() {
        currentVisibleView = STAGGERD_VIEW
        if (getItemsList().size > 0) {
            rv.visibility = View.VISIBLE
            tv.visibility = View.GONE
            val rv = view?.findViewById<RecyclerView>(R.id.rvPersonalItemsList)
            rv!!.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            val rvItemAdapter = RvItemAdapter(requireContext(), getItemsList())
            rv.adapter = rvItemAdapter
        } else {
            rv.visibility = View.GONE
            tv.visibility = View.VISIBLE

        }
    }

    fun getItemsList(): ArrayList<ArticleData> {
        val databaseHandler = DatabaseHandler(requireContext())
        return databaseHandler.viewPersonalArticles(userID)
    }
    fun addArticle() {
        val addArticle = Dialog(requireContext())
        addArticle.setCancelable(false)
        addArticle.setContentView(R.layout.add_article)
        val addTitle  = addArticle.findViewById<TextInputEditText>(R.id.etTitleAdd)
        val addText = addArticle.findViewById<TextInputEditText>(R.id.etTextAdd)
        val btnAdd = addArticle.findViewById<Button>(R.id.btnAdd)
        val btnCancel = addArticle.findViewById<Button>(R.id.btnCancel)

        btnAdd.setOnClickListener{
            val title = addTitle.text.toString()
            val text = addText.text.toString()

            val databaseHandler = DatabaseHandler(requireContext())
            if (title.isNotEmpty() && text.isNotEmpty()) {
                val status = databaseHandler.addArticle(ArticleData(userID, "", title, 0, text, 0, 0))
                if (status > -1) {
                    Toast.makeText(context, "Article added", Toast.LENGTH_LONG).show()
                    addTitle.text!!.clear()
                    addText.text!!.clear()
                    addArticle.dismiss()


                }
            } else {
                Toast.makeText(context, "Must fill all fields", Toast.LENGTH_LONG).show()
            }
        }
        btnCancel.setOnClickListener{
            addArticle.dismiss()
        }
        addArticle.show()
    }


    
private fun updateLikes(item : ArticleData){
    val likes = item.likes + 1
    val dbHandler: DatabaseHandler = DatabaseHandler(requireContext())
    if(likes > 0){
        val status = dbHandler.updateArticle(item.id,likes,item.dislikes)
        if(status >= 0){
            Toast.makeText(context,"$likes", Toast.LENGTH_SHORT).show()
            setupListOfDataIntoRecyclerView()
        } else{
            Toast.makeText(context,"NO UPDATE", Toast.LENGTH_SHORT).show()
        }
    }
}
    private fun updateDislikes(item : ArticleData){
        val dislikes = item.dislikes + 1
        val dbHandler : DatabaseHandler = DatabaseHandler(requireContext())
        if(dislikes > 0){
            val status = dbHandler.updateArticle(item.id,item.likes,dislikes)
            if(status >= 0){
                Toast.makeText(context,"$status $dislikes", Toast.LENGTH_SHORT).show()
                setupListOfDataIntoRecyclerView()
            } else{
                Toast.makeText(context,"NO UPDATE", Toast.LENGTH_SHORT).show()
            }
        }
    }



    inner class RvItemAdapter(val context : Context, private val items : ArrayList<ArticleData>) :
            RecyclerView.Adapter<RvItemAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.items_row,parent,false)
            )
        }

        override fun onBindViewHolder(holder: RvItemAdapter.ViewHolder, position: Int) {
            val item = items[position]
            holder.tvTitle.text = item.title
            holder.image.setImageResource(item.image)
            holder.textTv.text = item.text
            holder.likes.text = item.likes.toString()
            holder.dislikes.text = item.dislikes.toString()

            holder.itemLayout.background = ContextCompat.getDrawable(context, R.drawable.active_item_border)

            holder.likeBtn.setOnClickListener {
                updateLikes(item)

            }

            holder.dislikeBtn.setOnClickListener {
                updateDislikes(item)

            }
        }
        override fun getItemCount(): Int {
            return items.size
        }
        inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
            val tvTitle  : TextView = view.findViewById(R.id.tvTitle)
            val image : ImageView = view.findViewById(R.id.Image)
            val textTv : TextView = view.findViewById(R.id.tvText)
            val likes : TextView = view.findViewById(R.id.likeNum)
            val dislikes : TextView = view.findViewById(R.id.dislikeNum)
            val itemLayout : LinearLayout = view.findViewById(R.id.itemLayout)
            val likeBtn : ImageButton = view.findViewById(R.id.btnLike)
            val dislikeBtn : ImageButton = view.findViewById(R.id.btnDislike)

        }
    }
//    fun ifTablet(view : View){
//        if (view.findViewById<FloatingActionButton>(R.id.fbSwitch) != null) {
//            setupStaggeredDataIntoRecyclerView()
//            val switch = view.findViewById<FloatingActionButton>(R.id.fbSwitch)
//
//            switch.setOnClickListener {
//                if (currentVisibleView == LIST_VIEW) {
//                    switch.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_view_list))
//                    setupStaggeredDataIntoRecyclerView()
//                } else {
//                    switch.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid_view))
//                    setupListOfDataIntoRecyclerView()
//
//                }
//            }
//        }
//    }
}