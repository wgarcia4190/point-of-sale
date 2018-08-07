package com.devcorerd.pos.core.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import org.zakariya.stickyheaders.SectioningAdapter


/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
class StickyHeaderAdapter<T>(var data: MutableList<com.devcorerd.pos.model.entity.Section<T>>? = mutableListOf(), var context: Context,
                             private var viewHolderItemFactory: () -> ItemSectionViewHolder,
                             private var viewHolderHeaderFactory: () -> HeaderSectionViewHolder,
                             private var itemFactory: (viewHolder: ItemViewHolder, sectionIndex: Int, itemIndex: Int) -> Unit,
                             private var headerFactory: (viewHolder: SectioningAdapter.HeaderViewHolder?, sectionIndex: Int) -> Unit) : SectioningAdapter() {

    override fun getNumberOfSections(): Int {
        return data!!.size
    }

    override fun getNumberOfItemsInSection(sectionIndex: Int): Int {
        return data!![sectionIndex].data.size
    }

    override fun doesSectionHaveHeader(sectionIndex: Int): Boolean {
        return true
    }

    override fun doesSectionHaveFooter(sectionIndex: Int): Boolean {
        return false
    }

    override fun onCreateItemViewHolder(parent: ViewGroup?, itemType: Int): ItemViewHolder = viewHolderItemFactory()

    override fun onCreateHeaderViewHolder(parent: ViewGroup?, headerType: Int): SectioningAdapter.HeaderViewHolder = viewHolderHeaderFactory()

    @SuppressLint("SetTextI18n")
    override fun onBindItemViewHolder(viewHolder: ItemViewHolder, sectionIndex: Int, itemIndex: Int, itemType: Int) {
        itemFactory(viewHolder, sectionIndex, itemIndex)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindHeaderViewHolder(viewHolder: SectioningAdapter.HeaderViewHolder?, sectionIndex: Int, headerType: Int) {
        headerFactory(viewHolder, sectionIndex)
    }

    override fun onCreateGhostHeaderViewHolder(parent: ViewGroup?): GhostHeaderViewHolder {
        val ghostView = View(parent!!.context)
        ghostView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return SectioningAdapter.GhostHeaderViewHolder(ghostView)
    }
}