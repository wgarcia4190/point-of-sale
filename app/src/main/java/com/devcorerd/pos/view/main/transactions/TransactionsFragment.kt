package com.devcorerd.pos.view.main.transactions

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.StickyHeaderAdapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnUpdateToolbarListener
import com.devcorerd.pos.model.entity.Section
import com.devcorerd.pos.model.entity.Transaction
import com.devcorerd.pos.model.presenter.TransactionPresenter
import com.devcorerd.pos.view.viewholder.TransactionHeaderViewHolder
import com.devcorerd.pos.view.viewholder.TransactionItemViewHolder
import kotlinx.android.synthetic.main.transactions_fragment.*


/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
class TransactionsFragment : FragmentBase() {

    private lateinit var listener: OnUpdateToolbarListener
    private lateinit var transactionList: MutableList<Section<Transaction>>

    private val adapter: StickyHeaderAdapter<Transaction> by lazy {
        StickyHeaderAdapter(transactionList, transactionListRV.context, {
            val inflater = LayoutInflater.from(transactionListRV.context)
            val v = inflater.inflate(R.layout.transaction_item, transactionListRV, false)
            TransactionItemViewHolder(v)
        }, {
            val inflater = LayoutInflater.from(transactionListRV.context)
            val v = inflater.inflate(R.layout.transaction_header_item, transactionListRV, false)
            TransactionHeaderViewHolder(v)
        }, { viewHolder, sectionIndex, itemIndex ->
            (viewHolder as TransactionItemViewHolder).bindData(transactionList[sectionIndex], itemIndex) {
                stackFragmentToTop(TransactionDetailsFragment.newInstance(it), R.id.mainContainer, false)
            }
        }, { viewHolder, sectionIndex ->
            (viewHolder as TransactionHeaderViewHolder).bindData(transactionList[sectionIndex])
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: OnUpdateToolbarListener):
                TransactionsFragment {
            val fragmentBase = TransactionsFragment()
            val layout: Int = R.layout.transactions_fragment

            fragmentBase.listener = listener
            fragmentBase.createBundle(layout, TransactionPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener.onUpdateToolbar(getString(R.string.transactions))

        (presenter as TransactionPresenter).getTransactionsPerSection({
            transactionList = it
            toggleList(transactionList, transactionListRV)

            transactionListRV.setHasFixedSize(false)
            transactionListRV.layoutManager = LinearLayoutManager(context)
            transactionListRV.adapter = adapter
        }, { error ->
            UIHelper.showMessage(context!!, "Error cargando transacciones", error.message!!)
        })
    }
}