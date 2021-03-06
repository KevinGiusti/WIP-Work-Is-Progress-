package it.wip.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import it.wip.ui.dialogs.DialogFragmentOrderDetail
import it.wip.ui.dialogs.DialogInfo
import it.wip.R
import it.wip.database.model.ShopElement
import it.wip.databinding.FragmentOrderDetailBinding
import it.wip.utils.fromShopElementNameToDescription
import it.wip.utils.fromShopElementNameToLocalizedName
import it.wip.utils.fromShopElementNameToResource

class OrderDetailFragment(val shopElement: ShopElement, private val unlocked: Boolean): Fragment() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentOrderDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_detail, container, false)

        val buyButton = binding.buyButton
        val backButton = binding.orderDetailBackButton
        val shopInfoButton = binding.shopInfoButton
        val artwork = binding.avatarDetail

        artwork.setBackgroundResource(fromShopElementNameToResource(shopElement.elementName))
        artwork.contentDescription = getString(fromShopElementNameToLocalizedName(shopElement.elementName))

        binding.avatarName.text = getString(fromShopElementNameToLocalizedName(shopElement.elementName))


        if(unlocked)
            buyButton.visibility = View.INVISIBLE
        else
            buyButton.visibility = View.VISIBLE


        backButton.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> backButton.setImageResource(R.drawable.back_arrow_pressed)
                MotionEvent.ACTION_UP -> backButton.setImageResource(R.drawable.back_arrow)
            }
            v?.onTouchEvent(event) ?: true
        }

        shopInfoButton.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> shopInfoButton.setImageResource(R.drawable.shop_info_button_pressed)
                MotionEvent.ACTION_UP -> shopInfoButton.setImageResource(R.drawable.shop_info_button)
            }
            v?.onTouchEvent(event) ?: true
        }

        buyButton.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> buyButton.setImageResource(R.drawable.buy_button_pressed)
                MotionEvent.ACTION_UP -> buyButton.setImageResource(R.drawable.buy_button)
            }
            v?.onTouchEvent(event) ?: true
        }


        backButton.setOnClickListener {

            val fragment = ShopFragment()

            val bundle = this.arguments
            fragment.arguments = bundle

            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.frame_layout, fragment)
                ?.commit()
        }

        shopInfoButton.setOnClickListener {
            val dialogHistoryInfo =
                DialogInfo(getString(fromShopElementNameToDescription(shopElement.elementName)))
            dialogHistoryInfo.show(parentFragmentManager, "historyInfo")
        }

        buyButton.setOnClickListener {
            DialogFragmentOrderDetail(shopElement).show(parentFragmentManager, "shop")
        }


        return binding.root
    }

}