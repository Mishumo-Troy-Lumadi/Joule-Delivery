package com.troy.joule.ui.adapters

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.troy.joule.Methods
import com.troy.joule.R
import com.troy.joule.repository.models.Invoice
import com.troy.joule.ui.fragments.main.InvoicesFragment
import com.troy.joule.ui.fragments.main.InvoicesFragmentDirections

class ComponentInvoiceAdapter(val invoicesFragment: InvoicesFragment) : ListAdapter<Invoice,ComponentInvoiceAdapter.ViewHolder>(ComponentInvoiceDiffCallBack()) {

    private lateinit var context: Context
    private lateinit var geocoder: Geocoder

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.compInvoiceImg)
        val status: TextView = itemView.findViewById(R.id.compInvoiceStatus)
        val destination: TextView = itemView.findViewById(R.id.compInvoiceDestinatiom)
        val date: TextView = itemView.findViewById(R.id.compInvoiceDate)
        val container: MaterialCardView = itemView.findViewById(R.id.compInvoiceContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        geocoder = Geocoder(context)
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.component_invoice, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val invoice = getItem(position)
        val address = geocoder.getFromLocation(invoice.endLatitude.toDouble(),invoice.endLongitude.toDouble(),1)
        val d  = address[0].getAddressLine(0)

        val a = d.toString().split(",")
        val destination = a[0] + " " + a[1] + " " + a[2]

        holder.date.text = invoice.createdAt.substring(0,10)
        holder.image.setImageBitmap(Methods.generateQRCode("http://jouleio.herokuapp.com/api/v1/invoices/${invoice.id}",70))
        holder.status.text = invoice.status
        holder.status.setTextColor(Methods.assignColor(context,invoice.status))
        holder.destination.text = destination

        holder.container.setOnClickListener {
            val action = InvoicesFragmentDirections.actionNavigationInvoicesToInvoiceFragment(invoice.id)
            invoicesFragment.findNavController().navigate(action)
        }

    }

}

class ComponentInvoiceDiffCallBack: DiffUtil.ItemCallback<Invoice>(){
    override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
        return oldItem.id === newItem.id
    }

    override fun areContentsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
        return  oldItem == newItem
    }

}