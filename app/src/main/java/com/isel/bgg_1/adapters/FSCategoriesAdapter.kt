package com.isel.bgg_1.adapters

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.R
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.ValueDTO
import com.isel.bgg_1.extensions.inflate
import com.isel.bgg_1.viewmodel.FeatureSetCategoriesViewModel
import kotlinx.android.synthetic.main.fs_parameter_item.view.*

private lateinit var categories: Array<ValueDTO>
private lateinit var chosenCategories: ArrayList<ValueDTO?>

class FSCategoriesAdapter(private val model: FeatureSetCategoriesViewModel,
                          private val clickListener: (Int, RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<FSCategoriesAdapter.CategoryHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryHolder {
        val inflatedView = parent.inflate(R.layout.fs_parameter_item, false)
        categories = model.games.value!!
        chosenCategories = ArrayList()
        return CategoryHolder(inflatedView)
    }

    override fun getItemCount(): Int = model.games.value?.size ?: 0

    fun addCategory(category: ValueDTO?) {
        chosenCategories.add(category)
    }
    fun removeCategory(category: ValueDTO?) {
        chosenCategories.remove(category)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val category = model.games.value?.get(position)
        holder.bindItem(category, clickListener)
    }

    class CategoryHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var category: ValueDTO? = null

        fun bindItem(category: ValueDTO?,
                     clickListener: (Int, RecyclerView.ViewHolder) -> Unit) {
            view.setOnClickListener { clickListener(adapterPosition, this) }
            this.category = category

            if (chosenCategories.contains(category)) {
                view.setBackgroundColor(Color.BLUE)
            } else {
                view.setBackgroundColor(Color.WHITE)
            }
            view.txvParameterTitle.text = category?.name
        }
    }
}